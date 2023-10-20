package hibernate;

import hibernate.entity.EntityClass;
import hibernate.entity.column.EntityColumn;

import java.util.Map;
import java.util.stream.Collectors;

public class EntityObject {

    private final EntityClass entityClass;
    private final Object object;

    public EntityObject(Object object) {
        this.entityClass = new EntityClass(object.getClass());
        this.object = object;
    }

    public String getTableName() {
        return entityClass.tableName();
    }

    public Map<EntityColumn, Object> getFieldValues() {
        return entityClass.getEntityColumns()
                .stream()
                .collect(Collectors.toMap(entityColumn -> entityColumn, entityColumn -> entityColumn.getFieldValue(object)));
    }
}