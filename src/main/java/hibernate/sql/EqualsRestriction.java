package hibernate.sql;

/**
 * SQL query 조건문에서 일치하는 조건을 나타내는 클래스
 */
public class EqualsRestriction implements Restriction {

    private static final String EQUALS_OPERATOR = "=";

    private final String column;
    private final String value;

    public EqualsRestriction(String column, String value) {
        this.column = column;
        this.value = value;
    }

    @Override
    public String render() {
        return column + EQUALS_OPERATOR + value;
    }
}
