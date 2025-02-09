package music.repository.specification;

import music.model.Artist;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.jpa.domain.Specification;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ArtistSpecification implements Specification<Artist> {
    private SearchSpecification specification;
    @Override
    public Predicate toPredicate(@NonNull Root<Artist> root,
                                 CriteriaQuery<?> query,
                                 @NonNull CriteriaBuilder builder) {

        String key = specification.getKey();
        SearchOperator operator = specification.getOperator();
        Object value = specification.getValue();

        return switch (operator) {

            case GREATER_THAN -> builder.greaterThan(root.get(key), value.toString());

            case GREATER_THAN_OR_EQUAL_TO -> builder.greaterThanOrEqualTo(root.get(key), value.toString());

            case LESS_THAN -> builder.lessThan(root.get(key), value.toString());

            case LESS_THAN_OR_EQUAL_TO -> builder.lessThanOrEqualTo(root.get(key), value.toString());

            case EQUAL -> builder.equal(root.get(key), value.toString());

            case NOT_EQUAL -> builder.notEqual(root.get(key), value.toString());

            case LIKE -> builder.like(root.get(key), value.toString());

            case NOT_LIKE -> builder.notLike(root.get(key), value.toString());

            case CONTAIN -> builder.like(root.get(key), "%" + value.toString() + "%");

            case NOT_CONTAIN -> builder.notLike(root.get(key), "%" + value.toString() + "%");

            default -> throw new IllegalStateException("Unexpected operator: " + operator);

        };
    }
}

