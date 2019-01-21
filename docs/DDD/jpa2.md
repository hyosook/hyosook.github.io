---
title : JPA 개념
tags: ["JPA","DB","java"] 
---



## Hibernate

EJB 엔티티빈 기술을 대체

오픈소스 ORM 프레임워크

## JPA (Java Persistence API)

EJB 3.0에서 하이버네이트를 기반으로 새로운 자바 ORM 기술표준 (JPA를 하이버네이트로 구현)

자바애플리케이션과 JDBC 사이에서 동작 

테이블을 객체 지향적으로 만들어서 사용 (상속이 가능함)

### 제공하는 기능 (대분류)

1. 엔티티와 테이블을 매핑하는 설계 부분
2. 매핑한 엔티티를 실제 사용하는 부분

### 장점

1. 생산성 향상 : SQL 작성하고, JDBC api 사용하는 반복적인 일들을 대신 처리해줌.

   ​			나아가서 DDL 문을 자동으로 생성해주는 기능도 있음.

2. 유지보수

### 궁금증

Q. 매우 복잡한 SQL은 어떻게 하나요? 

A.통계와 같은 복잡한 쿼리는 SQL 작성이 더 쉬울수도. 마이바티스나 다른 SQL 매퍼 형태의 프레임워크를 혼용할 수도 있음.

Q. 하이버네이트 사용?

A. 국내에서 유독 마이바티스를 많이 사용. 전 세계적으로 조사하면 하이버네이트 프레임워크 사용 비중이 더 높음.

### JPQL

엔티티 객체를 조회하는 객체지향 쿼리

### 네이티브 SQL

JPQL을 사용해도 특정 데이터베이스에 의존하는 기능 (예: 오라클/MySQL만의 기능) 을 지원

## 스프링 데이터 JPA

스프링 프레임워크에서 JPA를 편리하게 사용할 수 있도록 지원하는 프로젝트.

Repository를 개발할 때 인터페이스만 작성하면 실행 시점에서 스프링 데이터가 JPA가 구현 객체를 동적으로 생성해서 주입해줌. (데이터 접근 객체 구현 시, 구현 클래스 없이 인터페이스만 작성)

책에서 JpaRepository<(객체), (데이터타입)>을 상속 받아서 사용하라고 함.

* 쇼핑몰 프로젝트에서 모든 Repository는 CrudRepository<(객체), (데이터타입)>을 상속 받음

CrudRepository<(객체), (데이터타입)> : 스프링 데이터 프로젝트가 공통으로 사용하는 인터페이스

JpaRepository<(객체), (데이터타입)> : 스프링 데이터 프로젝트 인터페이스에 JPA 특화된 기능들을 제공

**위 인터페이스들을 상속 받아 Repository를 구현하면 엔티티를 제어하는 주요 메소드들을 구현 클래스 작성 없이 사용 가능.**

#### 특정 조건에 만족하는 쿼리 수행하는 방법

1. 쿼리를 유추할 수 있는 메소드 이름을 통해 쿼리를 정의. 

- 메소드 이름 규칙 : findBy + (조건 필드이름) + (And) / (Or)

```java java
public interface UserRepository extends Repository<User, Long> {
  List<User> findByEmailAddressAndLastname(String emailAddress, String lastname);
}
```

### 쿼리 메소드 기능

스프링 데이터 JPA가 제공하는 기능

대표적인 기능은 메소드 이름만으로 쿼리를 생성하는 기능. Repository 인터페이스에서 메소드만 선언하면 해당 메소드 이름(정해진 규칙에 따라 메소드 이름을 지어야 함)으로 적절한 JPQL 쿼리를 생성하여 실행.

* 쇼핑몰 프로젝트 내 해당 부분

```java java
@Repository
public interface CartItemRepository extends CrudRepository<CartItem, CartItemId> {

    List<CartItem> findCartItemsByCart(Cart cart);
}
```

그 외 다양한 기능들 포함함. 

## 엔티티의 생명주기

1. 비 영속 : 엔티티를 new 한 시점
2. 영속 : 엔티티를 new 한 후 persist(삽입), find(조회), query 메소드를 사용하여 저장, 조회한 경우
3. 삭제 : 영속 객체를 remove 메소드로 삭제한 경우
4. 준 영속 : 트랜잭션이 commit되었거나 clear, flush 메소드가 실행된 경우. 이걸 다시 영속 객체 상태로 만들기 위한  merge 메소드가 존재함.

## 영속성 컨텍스트

Entity Manager가 엔티티들을 관리할 때 이곳에 보관하고 관리함.

관리되는 모든 엔티티 인스턴스의 집합. 

## EntityManager

엔티티의 생명주기, 영속성 컨텍스트, 트랜잭션을 관리함

### EntityManagerFactory

EntityManager를 관리
