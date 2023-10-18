# jpa-query-builder

## 2단계 - QueryBuilder DDL

### 요구사항 1
```java
@Entity
public class Person {
    
    @Id
    private Long id;
    
    private String name;
    
    private Integer age;
    
}
```
- @Entity 어노테이션이 달린 클래스를 가지고 create 쿼리를 만들 수 있다.
- 필드 타입과 명을 받아 추가할 수 있다.
- @Id 어노테이션이 붙는 경우 primary key로 지정할 수 있다.

### 요구사항 2
```java
@Entity
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nick_name")
    private String name;

    @Column(name = "old")
    private Integer age;
    
    @Column(nullable = false)
    private String email;

}
```
- @Column 어노테이션이 붙는 경우 옵션에 따라 쿼리문이 수정된다.
  - name 옵션이 있는 경우 해당 column의 이름은 해당값으로 지정된다.
  - nullable 옵션이 있는 경우 해당 column의 nullable 여부는 해당값으로 지정된다.
- @GeneratedValue 어노테이션이 붙는 경우 옵션에 따라 쿼리문이 수정된다.
  - IDENTITY 타입이 입력될 경우, auto increment로 id가 생성된다.

### 요구사항 3
```java
@Table(name = "users")
@Entity
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nick_name")
    private String name;

    @Column(name = "old")
    private Integer age;

    @Column(nullable = false)
    private String email;

    @Transient
    private Integer index;

}
```
- @Table 어노테이션이 붙는 경우 테이블의 이름이 name으로 지정된다.
  - name이 지정되지 않는 경우 기존대로 클래스명을 따른다.
- @Transient 어노테이션이 붙는 경우 column에서 제외한다.

### refactor
- EntityClass
  - @Entity 어노테이션이 없는 경우 생성 시 예외가 발생한다.
  - 테이블 이름을 반환할 수 있다.
    - 테이블 이름은 요구사항과 동일하게 @Table의 name이 있는 경우 해당 이름을 반환한다.
- EntityField
  - @Column 어노테이션에서 정보를 받을 수 있다.
    - Column name이 있는 경우 해당 이름이 field 명이 된다.
    - Column nullable을 가진다.
  - Field의 타입을 받아 ColumnType으로 변환하여 받을 수 있다.
- EntityId
  - @Id 어노테이션이 없는 경우 예외가 발생한다.
  - @GeneratedValue 어노테이션을 받아 저장한다.
- EntityColumnFactory
  - EntityColumn을 생성할 수 있는 지 확인한다.
    - @Transient이 걸린 필드는 생성할 수 없다.
  - @Id 어노테이션이 달린 경우 EntityId를, 아닌 경우 EntityField 구현체를 생성한다.
- ColumnOptionGenerateStrategy
  - 컬럼 옵션 전략에 맞는지 확인하고, 컬럼 옵션을 생성한다.
