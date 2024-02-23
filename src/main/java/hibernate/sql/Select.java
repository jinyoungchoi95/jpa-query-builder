package hibernate.sql;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @see org.hibernate.sql.SimpleSelect
 */
public class Select {
    private final String tableName;
    private final List<String> columns;
    private final Map<String, String> columnAliases;
    private final List<Restriction> restrictions;

    private final Dialect dialect;

    private Select(String tableName,
                   List<String> columns,
                   Map<String, String> columnAliases,
                   List<Restriction> restrictions,
                   Dialect dialect) {
        checkNotNull(tableName, "tableName must not be null");
        checkNotNull(dialect, "dialect must not be null");
        this.tableName = tableName;
        this.columns = columns;
        this.columnAliases = columnAliases;
        this.restrictions = restrictions;
        this.dialect = dialect;
    }

    public String toQuery() {
        StringBuilder sqlBuffer = new StringBuilder(
                tableName.length() +
                        columns.size() * 10 +
                        restrictions.size() * 10 +
                        10
        );

        applySelectClause(sqlBuffer);
        applyFromClause(sqlBuffer);
        applyWhereClause(sqlBuffer);

        return sqlBuffer.append(";").toString();
    }

    private void applySelectClause(StringBuilder sqlBuffer) {
        sqlBuffer.append("select ").append(parseColumns());
    }

    private String parseColumns() {
        return columns.stream()
                .map(this::parseColumn)
                .collect(Collectors.joining(", "));
    }

    private String parseColumn(String column) {
        String tableColumn = String.format("%s.%s", tableName, column);
        String alias = columnAliases.get(column);
        if (alias == null) {
            return tableColumn;
        }
        return String.format("%s as %s", tableColumn, alias);
    }

    private void applyFromClause(StringBuilder sqlBuffer) {
        sqlBuffer.append(" from ").append(tableName);
    }

    private void applyWhereClause(StringBuilder sqlBuffer) {
        sqlBuffer.append(" where ").append(parseRestriction());
    }

    private String parseRestriction() {
        return restrictions.stream()
                .map(Restriction::render)
                .collect(Collectors.joining(" and "));
    }

    public static class Builder {
        private String tableName;
        private List<String> columns = new ArrayList<>();
        private Map<String, String> columnAliases = new HashMap<>();
        private List<Restriction> restrictions = new ArrayList<>();
        private Dialect dialect;

        public Builder tableName(String tableName) {
            this.tableName = tableName;
            return this;
        }

        public Builder columns(List<String> columns) {
            this.columns = columns;
            return this;
        }

        public Builder columnAliases(Map<String, String> columnAliases) {
            this.columnAliases = columnAliases;
            return this;
        }

        public Builder restrictions(List<Restriction> restrictions) {
            this.restrictions = restrictions;
            return this;
        }

        public Builder dialect(Dialect dialect) {
            this.dialect = dialect;
            return this;
        }

        public Select build() {
            return new Select(tableName, columns, columnAliases, restrictions, dialect);
        }
    }
}
