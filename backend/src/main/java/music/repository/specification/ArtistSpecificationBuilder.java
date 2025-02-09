package music.repository.specification;

import music.model.Artist;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ArtistSpecificationBuilder {

    public final List<SearchSpecification> params;

    public ArtistSpecificationBuilder() {
        params = new ArrayList<>();
    }

    public ArtistSpecificationBuilder with(final String key, final String operator,
                                         final Object value, final String prefix, final String suffix) {
        return with(null, key, operator, value, prefix, suffix);
    }

    public ArtistSpecificationBuilder with(final String andOrLogic, final String key, final String operator,
                                         final Object value, final String prefix, final String suffix) {
        SearchOperator searchOperator = SearchOperator.getSimpleOperator(operator);
        if (searchOperator != null) {
            params.add(new SearchSpecification(andOrLogic, key, searchOperator, value));
        }
        return this;
    }

    public Specification<Artist> build() {
        if (params.isEmpty()) return null;

        Specification<Artist> result = new ArtistSpecification(params.get(0));

        for (int i = 1; i < params.size(); i++) {
            String andOrLogic = params.get(i).getAndOrLogic();
            if (andOrLogic != null) {
                if (andOrLogic.equals(SearchOperator.AND_OPERATOR)) {
                    result = Specification.where(result).and(new ArtistSpecification(params.get(i)));
                }
                else if (andOrLogic.equals(SearchOperator.OR_OPERATOR)) {
                    result = Specification.where(result).or(new ArtistSpecification(params.get(i)));
                }
            }
        }

        return result;
    }

    public ArtistSpecificationBuilder with(ArtistSpecification spec) {
        params.add(spec.getSpecification());
        return this;
    }

    public ArtistSpecificationBuilder with(SearchSpecification specification) {
        params.add(specification);
        return this;
    }
}