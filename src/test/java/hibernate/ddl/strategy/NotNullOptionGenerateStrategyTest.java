package hibernate.ddl.strategy;

import hibernate.entity.column.EntityField;
import jakarta.persistence.Column;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;

class NotNullOptionGenerateStrategyTest {

    private final NotNullOptionGenerateStrategy strategy = new NotNullOptionGenerateStrategy();

    @Test
    void nullable이_false인_경우_acceptable하다() throws NoSuchFieldException {
        Field givenField = TestEntity.class.getDeclaredField("name1");
        EntityField entityField = new EntityField(givenField);

        boolean actual = strategy.acceptable(entityField);
        assertThat(actual).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"name2", "name3"})
    void nullable이_false가_아닌_경우_acceptable하지_않다(final String input) throws NoSuchFieldException {
        Field givenField = TestEntity.class.getDeclaredField(input);
        EntityField entityField = new EntityField(givenField);

        boolean actual = strategy.acceptable(entityField);
        assertThat(actual).isFalse();
    }

    @Test
    void Identity_컬럼옵션을_반환한다() {
        String actual = strategy.generateColumnOption();
        assertThat(actual).isEqualTo("not null");
    }

    static class TestEntity {
        @Column(nullable = false)
        private String name1;

        private String name2;

        @Column
        private String name3;
    }
}
