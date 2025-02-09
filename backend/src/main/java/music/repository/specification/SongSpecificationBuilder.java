package music.repository.specification;

import music.model.Song;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class SongSpecificationBuilder {

    public final List<SearchSpecification> params;

    public SongSpecificationBuilder() {
        params = new ArrayList<>();
    }

    public SongSpecificationBuilder with(final String key, final String operator,
                                         final Object value, final String prefix, final String suffix) {
        return with(null, key, operator, value, prefix, suffix);
    }

    public SongSpecificationBuilder with(final String andOrLogic, final String key, final String operator,
                                         final Object value, final String prefix, final String suffix) {
        SearchOperator searchOperator = SearchOperator.getSimpleOperator(operator);
        if (searchOperator != null) {
            params.add(new SearchSpecification(andOrLogic, key, searchOperator, value));
        }
        return this;
    }

    public Specification<Song> build() {
        if (params.isEmpty()) return null;

        Specification<Song> result = new SongSpecification(params.get(0));

        for (int i = 1; i < params.size(); i++) {
            String andOrLogic = params.get(i).getAndOrLogic();
            if (andOrLogic != null) {
                if (andOrLogic.equals(SearchOperator.AND_OPERATOR)) {
                    result = Specification.where(result).and(new SongSpecification(params.get(i)));
                }
                else if (andOrLogic.equals(SearchOperator.OR_OPERATOR)) {
                    result = Specification.where(result).or(new SongSpecification(params.get(i)));
                }
            }
        }

        return result;
    }

    public SongSpecificationBuilder with(SongSpecification spec) {
        params.add(spec.getSpecification());
        return this;
    }

    public SongSpecificationBuilder with(SearchSpecification specification) {
        params.add(specification);
        return this;
    }
}
