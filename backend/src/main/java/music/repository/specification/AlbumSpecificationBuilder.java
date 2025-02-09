package music.repository.specification;

import music.model.Album;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class AlbumSpecificationBuilder {

    public final List<SearchSpecification> params;

    public AlbumSpecificationBuilder() {
        params = new ArrayList<>();
    }

    public AlbumSpecificationBuilder with(final String key, final String operator,
                                           final Object value, final String prefix, final String suffix) {
        return with(null, key, operator, value, prefix, suffix);
    }

    public AlbumSpecificationBuilder with(final String andOrLogic, final String key, final String operator,
                                           final Object value, final String prefix, final String suffix) {
        SearchOperator searchOperator = SearchOperator.getSimpleOperator(operator);
        if (searchOperator != null) {
            params.add(new SearchSpecification(andOrLogic, key, searchOperator, value));
        }
        return this;
    }

    public Specification<Album> build() {
        if (params.isEmpty()) return null;

        Specification<Album> result = new AlbumSpecification(params.get(0));

        for (int i = 1; i < params.size(); i++) {
            String andOrLogic = params.get(i).getAndOrLogic();
            if (andOrLogic != null) {
                if (andOrLogic.equals(SearchOperator.AND_OPERATOR)) {
                    result = Specification.where(result).and(new AlbumSpecification(params.get(i)));
                }
                else if (andOrLogic.equals(SearchOperator.OR_OPERATOR)) {
                    result = Specification.where(result).or(new AlbumSpecification(params.get(i)));
                }
            }
        }

        return result;
    }

    public AlbumSpecificationBuilder with(AlbumSpecification spec) {
        params.add(spec.getSpecification());
        return this;
    }

    public AlbumSpecificationBuilder with(SearchSpecification specification) {
        params.add(specification);
        return this;
    }
}