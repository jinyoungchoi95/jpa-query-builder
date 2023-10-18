package hibernate.strategy;

import hibernate.entity.column.EntityField;
import hibernate.entity.column.EntityId;
import jakarta.persistence.Id;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;

class PrimaryKetOptionGenerateStrategyTest {

    private final PrimaryKetOptionGenerateStrategy strategy = new PrimaryKetOptionGenerateStrategy();

    @Test
    void id인_경우_acceptable하다() throws NoSuchFieldException {
        Field givenField = TestEntity.class.getDeclaredField("id1");
        EntityId entityId = new EntityId(givenField);

        boolean actual = strategy.acceptable(entityId);
        assertThat(actual).isTrue();
    }

    @Test
    void id가_아닌_경우_acceptable하지_않다() throws NoSuchFieldException {
        Field givenField = TestEntity.class.getDeclaredField("id2");
        EntityField entityId = new EntityField(givenField);

        boolean actual = strategy.acceptable(entityId);
        assertThat(actual).isFalse();
    }

    @Test
    void primary_key_컬럼옵션을_반환한다() {
        String actual = strategy.generateColumnOption();
        assertThat(actual).isEqualTo("primary key");
    }

    static class TestEntity {
        @Id
        private Long id1;

        private Long id2;
    }
}
