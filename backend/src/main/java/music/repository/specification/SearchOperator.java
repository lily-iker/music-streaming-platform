package music.repository.specification;

public enum SearchOperator {
    EQUAL,
    NOT_EQUAL,
    GREATER_THAN,
    GREATER_THAN_OR_EQUAL_TO,
    LESS_THAN,
    LESS_THAN_OR_EQUAL_TO,
    LIKE,
    NOT_LIKE,
    CONTAIN,
    NOT_CONTAIN,
    STARTS_WITH,
    ENDS_WITH;

    public static final String[] SIMPLE_OPERATOR_SET = { "=", "!=", ">", ">=", "<", "<=", ":", "!:", "~", "!~"};

    public static final String ZERO_OR_MORE = "*";

    public static final String OR_OPERATOR = "'";

    public static final String AND_OPERATOR = ",";

    public static final String LEFT_PARENTHESIS = "(";

    public static final String RIGHT_PARENTHESIS = ")";

    public static SearchOperator getSimpleOperator(final String operator) {
        return switch (operator) {

            case "=" -> EQUAL;

            case "!=" -> NOT_EQUAL;

            case ">" -> GREATER_THAN;

            case ">=" -> GREATER_THAN_OR_EQUAL_TO;

            case "<" -> LESS_THAN;

            case "<=" -> LESS_THAN_OR_EQUAL_TO;

            case ":" -> LIKE;

            case "!:" -> NOT_LIKE;

            case "~" -> CONTAIN;

            case "!~" -> NOT_CONTAIN;

            default -> throw new IllegalStateException("Unexpected operator: " + operator);
        };
    }

}
