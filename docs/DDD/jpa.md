---
title : JPA 엔티티 구성
tags: ["JPA","DB","java"]
---

# JPA 엔티티 구성

쇼핑몰 소스/중요도 위주로 작성함

## 레퍼런스

### @Entity

테이블과 매핑할 클래스는 필수로 붙혀야 함

### @Table

엔티티와 매핑할 테이블을 지정

생략하면 매핑한 엔티티 이름을 테이블 이름으로 사용.

1. name : 매핑할 테이블 이름

### @Id

기본키 직접 할당

### @GeneratedValue

### @Column

객체 필드를 테이블 컬럼에 매핑

1. name : 필드와 매핑할 테이블의 컬럼 이름. 기본값이 필드 이름

```java java
@Column(name = "cart_id")
private Long id;
```

2. columnDefinition : 데이터베이스 컬럼 정보를 직접 설정.
3. nullable : null 값 허용 여부 false일 경우, not null 제약조건이 됨. 기본값이 true

### @JoinColumn

관계를 나타낼 때 사용. 외래 키를 매핑할 때 사용.

1. name : 매핑할 외래 키 이름

### @JsonIdentityInfo

?

### @RequiredArgsConstructor

생성자를 자동으로 만들어주고, 옵션에 따라 null 체크도 해줌

## 엔티티 관계 설정 (관계를 가진 엔티티끼리 조인할 때 사용)

### @ManyToOne

다대일 관계 설정에서 사용

1. fetch : 글로벌 페치 전략을 설정

- FetchType.EAGER : 관계된 엔티티의 정보를 미리 읽어오는 것
- FetchType.LAZY : 관계된 엔티티의 정보를 실제 요청하는 순간 가져오는 것

```java java
public class CartItem extends BaseEntity implements Serializable {
	..
  	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cart_id")
	private Cart cart;
  	..
}
```

### @OneToMany

일대다 관계 설정에서 사용

1. mappedBy : 양방향 관계 설정시 관계의 주체가 되는 쪽에서 정의
2. fetch : 관계 엔티티의 데이터 읽기 전략을 결정. 

- FetchType.EAGER : 관계된 엔티티의 정보를 미리 읽어오는 것
- FetchType.LAZY : 관계된 엔티티의 정보를 실제 요청하는 순간 가져오는 것

```java java
public class Cart extends BaseEntity implements Serializable {
	..
    @OneToMany(mappedBy = "cart", fetch = FetchType.EAGER)
    @OrderBy("created_at asc")
    private List<CartItem> cartItems;
    ..
}
```
**@ManyToOne과 @OneToMany 두가지가 크게 다르지 않음. 어느쪽을 기준으로 작성하느냐에 따라 다름**

# JPA는 다양한 쿼리 방법을 지원

* JPQL
* JPA Criteria
* QueryDSL
* 네이티브 SQL
* JDBC API 직접 사용, MyBatis, SpringJdbcTemplate 함께 사용

## JPQL로 쿼리 사용

SQL과 유사한 문법을 가지는 쿼리. 엔티티 객체를 대상으로 쿼리

**두 개의 엔티티를 조인해야 하는 경우, 이런 방법을 쓰는 것 같음.**

```java java
EntityManagerFactory emf = Persistence.createEntityManagerFactory("(db정보가 들어갈 듯?)");
EntityManager em = emf.createEntityManager();

String jpql = "select m from Member as m where m.username = 'kim'";
List<Member> resultList = em.createQuery(jpql, Member.class).getResultList();
```

실제 실행된 쿼리

```sql sql
select
	member.id as id,
	member.age as age
	member.team_id as team,
	member.name as name
from 
	Member member
where
	member.name = 'kim'
```