package com.example.demo.repository.search;

import com.example.demo.model.Song;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class SongSearchRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public Page<Long> findIdsBySpecification(Specification<Song> specification, Pageable pageable) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        Root<Song> root = query.from(Song.class);

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
        Root<Song> countRoot = countQuery.from(Song.class);

        Predicate countPredicate = specification.toPredicate(countRoot, query, builder);
        countQuery.select(builder.count(countRoot)).where(countPredicate);

        long total = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(ids, pageable, total);
    }
}