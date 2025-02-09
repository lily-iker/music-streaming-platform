package music.repository.search;

import music.dto.response.PageResponseCriteria;
import music.dto.response.UserDetailsResponse;
import music.mapper.UserMapper;
import music.model.User;
import music.repository.criteria.SearchCriteria;
import music.repository.criteria.UserSearchCriteriaQueryConsumer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Repository
@RequiredArgsConstructor
public class UserSearchRepository {
    @PersistenceContext
    private EntityManager entityManager;

    private final UserMapper userMapper;

    public PageResponseCriteria<?> criteriaSearch(int offset, int pageSize, String sortBy, String... search) {

        List<SearchCriteria> criteriaList = new ArrayList<>();

        if (search != null) {
            for (String s : search) {
                Pattern pattern = Pattern.compile("^(\\w+)(!:|!~|!=|>=|<=|[:~=><])(\\w+)$");
                Matcher matcher = pattern.matcher(s);
                if (matcher.find()) {
                    criteriaList.add(new SearchCriteria(matcher.group(1), matcher.group(2), matcher.group(3)));
                }
            }
        }

        List<Long> userIds = getUsersIds(offset, pageSize, criteriaList);

        List<User> users = getUsersByIds(userIds, sortBy);

        List<UserDetailsResponse> result = users.stream()
                .map(userMapper::toUserDetailsResponse)
                .toList();

        int totalElement = getTotalElement(criteriaList).intValue();

        return PageResponseCriteria.builder()
                .offset(offset)
                .pageSize(pageSize)
                .totalElement(totalElement)
                .items(result)
                .build();
    }

    public Page<Long> findIdsBySpecification(Specification<User> specification, Pageable pageable) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        Root<User> root = query.from(User.class);

        Predicate predicate = specification.toPredicate(root, query, builder);
        query.select(root.get("id")).where(predicate);

        if (pageable.getSort() != null) {
            List<Order> orders = new ArrayList<>();
            for (Sort.Order order : pageable.getSort()) {
                String columnToSort = order.getProperty();
                if (order.isAscending()) {
                    orders.add(builder.asc(root.get(columnToSort)));
                } else if (order.isDescending()){
                    orders.add(builder.desc(root.get(columnToSort)));
                }
            }
            query.orderBy(orders);
        }

        List<Long> ids = entityManager.createQuery(query)
                            .setFirstResult((int) pageable.getOffset())
                            .setMaxResults(pageable.getPageSize())
                            .getResultList();

        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        Root<User> countRoot = countQuery.from(User.class);

        Predicate countPredicate = specification.toPredicate(countRoot, query, builder);
        countQuery.select(builder.count(countRoot)).where(countPredicate);

        long total = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(ids, pageable, total);
    }

    private List<Long> getUsersIds(int offset, int pageSize, List<SearchCriteria> criteriaList) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        Root<User> root = query.from(User.class);

        Predicate predicate = builder.conjunction();
        UserSearchCriteriaQueryConsumer queryConsumer = new UserSearchCriteriaQueryConsumer(builder, predicate, root);

        criteriaList.forEach(queryConsumer);
        predicate = queryConsumer.getPredicate();

        query.select(root.get("id")).where(predicate);

        return entityManager.createQuery(query)
                .setFirstResult(offset)
                .setMaxResults(pageSize)
                .getResultList();
    }

    private List<User> getUsersByIds(List<Long> ids, String sortBy) {
        if (ids.isEmpty()) {
            return new ArrayList<>();
        }
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = builder.createQuery(User.class);
        Root<User> root = query.from(User.class);

        root.fetch("addresses", JoinType.LEFT);
        root.fetch("roles", JoinType.INNER);

        query.where(root.get("id").in(ids));

        if (StringUtils.hasLength(sortBy)) {
            Pattern pattern = Pattern.compile("^(\\w+)(:)(asc|desc)$");
            Matcher matcher = pattern.matcher(sortBy);
            if (matcher.find()) {
                String columnToSort = matcher.group(1);
                String sortDirection = matcher.group(3);
                if (sortDirection.equalsIgnoreCase("asc")) {
                    query.orderBy(builder.asc(root.get(columnToSort)));
                }
                else if (sortDirection.equalsIgnoreCase("desc")) {
                    query.orderBy(builder.desc(root.get(columnToSort)));
                }
            }
        }

        return entityManager.createQuery(query).getResultList();
    }

    private Long getTotalElement(List<SearchCriteria> criteriaList) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        Root<User> root = query.from(User.class);

        Predicate predicate = builder.conjunction();
        UserSearchCriteriaQueryConsumer queryConsumer = new UserSearchCriteriaQueryConsumer(builder, predicate, root);

        criteriaList.forEach(queryConsumer);
        predicate = queryConsumer.getPredicate();

        query.select(builder.count(root)).where(predicate);

        return entityManager.createQuery(query).getSingleResult();
    }
}
