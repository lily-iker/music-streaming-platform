package music.repository.specification;

import music.model.User;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class UserSpecificationBuilder {

    public final List<SearchSpecification> params;

    public UserSpecificationBuilder() {
        params = new ArrayList<>();
    }

    public UserSpecificationBuilder with(final String key, final String operator,
                                         final Object value, final String prefix, final String suffix) {
        return with(null, key, operator, value, prefix, suffix);
    }

    public UserSpecificationBuilder with(final String andOrLogic, final String key, final String operator,
                                         final Object value, final String prefix, final String suffix) {
        SearchOperator searchOperator = SearchOperator.getSimpleOperator(operator);
        if (searchOperator != null) {
            params.add(new SearchSpecification(andOrLogic, key, searchOperator, value));
        }
        return this;
    }

    public Specification<User> build() {
        if (params.isEmpty()) return null;

        Specification<User> result = new UserSpecification(params.get(0));

        for (int i = 1; i < params.size(); i++) {
            String andOrLogic = params.get(i).getAndOrLogic();
            if (andOrLogic != null) {
                if (andOrLogic.equals(SearchOperator.AND_OPERATOR)) {
                    result = Specification.where(result).and(new UserSpecification(params.get(i)));
                }
                else if (andOrLogic.equals(SearchOperator.OR_OPERATOR)) {
                    result = Specification.where(result).or(new UserSpecification(params.get(i)));
                }
            }
        }

        return result;
    }

    public UserSpecificationBuilder with(UserSpecification spec) {
        params.add(spec.getSpecification());
        return this;
    }

    public UserSpecificationBuilder with(SearchSpecification specification) {
        params.add(specification);
        return this;
    }
}
