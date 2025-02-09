package music.repository.specification;

import lombok.Getter;

@Getter
public class SearchSpecification {
    private String key;
    private SearchOperator operator;
    private Object value;
    private String andOrLogic;

    public SearchSpecification(final String key, final SearchOperator operator, final Object value) {
        super();
        this.key = key;
        this.operator = operator;
        this.value = value;
    }

    public SearchSpecification(final String andOrLogic, final String key, final SearchOperator operator, final Object value) {
        super();
        if (andOrLogic != null && (andOrLogic.equals(SearchOperator.AND_OPERATOR) || andOrLogic.equals(SearchOperator.OR_OPERATOR))) {
            this.andOrLogic = andOrLogic;
        }
        this.key = key;
        this.operator = operator;
        this.value = value;
    }

    public SearchSpecification(String key, String operator, String prefix, String value, String suffix) {
        SearchOperator searchOperator = SearchOperator.getSimpleOperator(operator);

        if (searchOperator != null) {
            final boolean startWithAsterisk = prefix != null && prefix.contains(SearchOperator.ZERO_OR_MORE);
            final boolean endWithAsterisk = suffix != null && suffix.contains(SearchOperator.ZERO_OR_MORE);

            if (searchOperator == SearchOperator.CONTAIN) {
                if (!(startWithAsterisk && endWithAsterisk)) {
                    if (startWithAsterisk) {
                        searchOperator = SearchOperator.ENDS_WITH;
                    } else if (endWithAsterisk) {
                        searchOperator = SearchOperator.STARTS_WITH;
                    }
                }
            }
            else if (searchOperator == SearchOperator.NOT_CONTAIN) {
                if (!(startWithAsterisk && endWithAsterisk)) {
                    if (startWithAsterisk) {
                        searchOperator = SearchOperator.STARTS_WITH;
                    } else if (endWithAsterisk) {
                        searchOperator = SearchOperator.ENDS_WITH;
                    }
                }
            }
        }

        this.key = key;
        this.operator = searchOperator;
        this.value = value;
    }
}
