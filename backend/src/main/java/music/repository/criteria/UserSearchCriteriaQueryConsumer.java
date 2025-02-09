package music.repository.criteria;

import music.model.User;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.function.Consumer;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserSearchCriteriaQueryConsumer implements Consumer<SearchCriteria> {
    private CriteriaBuilder builder;
    private Predicate predicate;
    private Root<User> root;

    @Override
    public void accept(SearchCriteria search) {
        String key = search.getKey();
        String operator = search.getOperator();
        Object value = search.getValue();

        switch (operator) {

            case ">" -> predicate = builder.and(predicate, builder.greaterThan(root.get(key), value.toString()));

            case ">=" -> predicate = builder.and(predicate, builder.greaterThanOrEqualTo(root.get(key), value.toString()));

            case "<" -> predicate = builder.and(predicate, builder.lessThan(root.get(key), value.toString()));

            case "<=" -> predicate = builder.and(predicate, builder.lessThanOrEqualTo(root.get(key), value.toString()));

            case "=" -> predicate = builder.and(predicate, builder.equal(root.get(key), value.toString()));

            case "!=" -> predicate = builder.and(predicate, builder.notEqual(root.get(key), value.toString()));

            case ":" -> predicate = builder.and(predicate, builder.like(root.get(key), value.toString()));

            case "!:" -> predicate = builder.and(predicate, builder.notLike(root.get(key), value.toString()));

            case "~" -> predicate = builder.and(predicate, builder.like(root.get(key), "%" + value.toString() + "%"));

            case "!~" -> predicate = builder.and(predicate, builder.notLike(root.get(key), "%" + value.toString() + "%"));

            default -> throw new IllegalStateException("Unexpected value: " + operator);
        }
    }
}
