package hibernate.sql;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EqualsRestrictionTest {

    @Nested
    class render {
        @Test
        void 컬럼명과_값이_주어지면_쿼리_조건을_반환한다() {
            String actual = new EqualsRestriction("name", "huni").render();
            assertThat(actual).isEqualTo("name=huni");
        }
    }
}
