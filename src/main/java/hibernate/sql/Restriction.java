package hibernate.sql;

/**
 * SQL query 조건문을 나타내는 인터페이스
 */
public interface Restriction {

    String render();
}
