package hibernate.sql;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class SelectTest {

    @Nested
    class init {
        @Test
        void tableName이_null이면_예외가_발생한다() {
            NullPointerException actual = assertThrows(NullPointerException.class, () -> new Select.Builder()
                    .tableName(null)
                    .dialect(new MySqlDialect())
                    .build());
            assertThat(actual).hasMessage("tableName must not be null");
        }

        @Test
        void dialect가_null이면_예외가_발생한다() {
            NullPointerException actual = assertThrows(NullPointerException.class, () -> new Select.Builder()
                    .tableName("tableName")
                    .dialect(null)
                    .build());
            assertThat(actual).hasMessage("dialect must not be null");
        }
    }

    @Nested
    class toQuery {
        @Test
        void 조건이_단순한_select문을_생성한다() {
            // given
            Select select = new Select.Builder()
                    .tableName("member")
                    .columns(List.of("id", "name", "age"))
                    .restrictions(List.of(new EqualsRestriction("id", "1")))
                    .dialect(new MySqlDialect())
                    .build();

            // when
            String actual = select.toQuery();

            // then
            assertThat(actual).isEqualTo("select member.id, member.name, member.age from member where id=1;");
        }

        @Test
        void column의_alias가_있는_경우_추가된_select문을_생성한다() {
            // given
            Select select = new Select.Builder()
                    .tableName("member")
                    .columns(List.of("id", "name", "age"))
                    .columnAliases(Map.of("id", "id"))
                    .restrictions(List.of(new EqualsRestriction("id", "1")))
                    .dialect(new MySqlDialect())
                    .build();

            // when
            String actual = select.toQuery();

            // then
            assertThat(actual).isEqualTo("select member.id as id, member.name, member.age from member where id=1;");
        }

        @Test
        void where조건문이_있는_경우_and가_추가된_select문을_생성한다() {
            // given
            Select select = new Select.Builder()
                    .tableName("member")
                    .columns(List.of("id", "name", "age"))
                    .restrictions(List.of(new EqualsRestriction("id", "1"), new EqualsRestriction("age", "20")))
                    .dialect(new MySqlDialect())
                    .build();

            // when
            String actual = select.toQuery();

            // then
            assertThat(actual).isEqualTo("select member.id, member.name, member.age from member where id=1 and age=20;");
        }
    }
}
