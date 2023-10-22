package hibernate.dml;

import hibernate.entity.EntityClass;
import hibernate.entity.column.ColumnType;
import hibernate.entity.column.EntityColumn;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InsertQueryBuilder {

    private static final String INSERT_QUERY = "insert into %s (%s) values (%s);";

    private static final String INSERT_COLUMN_QUERY_DELIMITER = ", ";
    private static final String INSERT_COLUMN_STRING_VALUE_FORMAT = "'%s'";

    public String generateQuery(final EntityClass<?> entityClass, final Object entity) {
        Map<EntityColumn, Object> fieldValues = entityClass.getFieldValues(entity);
        List<EntityColumn> entityColumns = new ArrayList<>(fieldValues.keySet());
        return String.format(INSERT_QUERY,
                entityClass.tableName(), parseColumnQueries(entityColumns), parseColumnValueQueries(entityColumns, fieldValues));
    }

    private String parseColumnQueries(final List<EntityColumn> entityColumns) {
        return entityColumns.stream()
                .map(EntityColumn::getFieldName)
                .collect(Collectors.joining(INSERT_COLUMN_QUERY_DELIMITER));
    }

    private Object parseColumnValueQueries(final List<EntityColumn> entityColumns, final Map<EntityColumn, Object> fieldValues) {
        return entityColumns.stream()
                .map(column -> parseFieldValue(column, fieldValues.get(column)))
                .collect(Collectors.joining(INSERT_COLUMN_QUERY_DELIMITER));
    }

    private String parseFieldValue(final EntityColumn entityColumn, final Object entity) {
        if (entityColumn.getColumnType() == ColumnType.VAR_CHAR) {
            return String.format(INSERT_COLUMN_STRING_VALUE_FORMAT, entity);
        }
        return entity.toString();
    }
}
