---
title : JPA 
---



# JPA(Java Persistence API)

> 자바 ORM 기술에 대한 표준 명세로, `JAVA`에서 제공하는 API이다
>
> ORM이기 때문에 자바 클래스와 **DB테이블**을 매핑한다

### SQL Mapper

- `SQL` ←mapping→ Object 필드
- SQL 문으로 직접 디비를 조작한다.
- Mybatis, jdbcTemplate

### ORM(Object-Relation Mapping/객체-관계 매핑)

- `DB 데이터`

  ←mapping→ Object 필드

- 객체를 통해 간접적으로 디비 데이터를 다룬다.

- 객체와 디비의 데이터를 자동으로 매핑해준다.

  - SQL 쿼리가 아니라 **메서드로 데이터를 조작할 수 있다**.
  - 객체간 관계를 바탕으로 **sql을 자동으로 생성**한다

- JPA, Hibernate

![](https://media.vlpt.us/images/adam2/post/7e6928cd-2537-45b4-a9f9-afd7c8a5e908/Untitled%202.png)



### 기능 
1. 엔터티와 DB 테이블 매핑하는 설계 부분 (DDL 자동 생성)
2. 매핑한 엔터티를 사용하는 부분 (SQL 자동 생성)

### 지연 로딩(Lazy Loading)

객체가 실제로 사용될 때 로딩하는 전략

![img](https://media.vlpt.us/images/adam2/post/c34d236e-6375-40c3-86a9-211dcda2c0e3/Untitled%209.png)
memberDAO.find(memberId)에서는 Member 객체에 대한 SELECT 쿼리만 날린다.

Team team = member.getTeam()로 Team 객체를 가져온 후에 team.getName()처럼 실제로 team 객체를 건드릴 때!

즉, 값이 실제로 필요한 시점에 JPA가 Team에 대한 SELECT 쿼리를 날린다.

Member와 Team 객체 각각 따로 조회하기 때문에 네트워크를 2번 타게 된다.

Member를 사용하는 경우에 대부분 Team도 같이 필요하다면 즉시 로딩을 사용한다.

**즉시 로딩**

JOIN SQL로 한 번에 연관된 객체까지 미리 조회하는 전략

![img](https://media.vlpt.us/images/adam2/post/a4818fa8-c7cc-49c4-9329-df5a6a200e7f/Untitled%2010.png)
Join을 통해 항상 연관된 모든 객체를 같이 가져온다.

애플리케이션 개발할 때는 모두 지연 로딩으로 설정한 후에, 성능 최적화가 필요할 때에 옵션을 변경하는 것을 추천한다.

### 영속성 컨텍스트 (Persistence context)
JPA에서는 영속성 컨텍스트가 존재한다. 

* 개념적으로 존재하여 눈에 보이지 않는다. 

* 영속성 컨텍스트에 엔터티가 저장되고, 저장된 엔터티를 엔터티 매니저가 생명주기에 따라 관리한다. 
    * 영속성 컨텍스트 내의 엔터티 변경이 감지되면 엔터티매니저가 SQL을 날린다. (엔터티를 불변 타입으로 만들어야 하는 중요한 이유)

* SQL이 자동 전송되기 때문에, 트랜잭션 범위 설정을 주의해야 한다. 
	* 스프링 컨테이너는 트랜잭션 범위와 영속성 컨텍스트의 생존 범위가 같다. 
	* `@Transactional` 선언 > 트랜잭션 시작

## Aggregate 
Entity와 Value 타입을 설계하기 위해서 Aggregate 개념은 필수이다. 

완벽한 이해를 위해서는 도메인 모델 개념과 도메인 주도 설계에 대한 이해가 필요하다. 

* 애그리거트는 관련 객체를 하나로 묶은 군집이다. 

* 애그리거트에서 엔터티는 **루트 엔터티 하나**이다. 그러나, 필요에 따라 루트 엔터티 외에 다른 엔터티가 나올 수도 있다. 
    * 이론상으로 루트 엔터티만 두도록 권장하기 때문에 다른 엔터티가 있다면, 진짜 엔터티인지 혹은 애그리거트가 잘못 구성되었는지 꼭 의심해야 한다.

* 애그리거트에 속한 객체는 유사하거나 **동일한 라이프 사이클**을 가진다. 

* 애그리거트 내의 엔티티 및 밸류 객체의 데이터 수정은 애그리거트 루트를 통해서만 수행한다.