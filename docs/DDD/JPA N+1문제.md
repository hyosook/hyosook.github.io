# JPA N+1문제
연관관계에서 한 엔티티만 조회하는 기능인데, 연관 엔티티의 갯수만큼 조회쿼리를 날리는 문제이다.

### 예시
Order가 OderItem을 가지고 있다. 
 Order 테이블엔 10개의 Order 레코드가 들어있다.  

Order를 조회 시, select 문 1개가 날아갈 것 으로 예상된다.
그러나, Order select문 1개와 OrderItem select문 10개가 발생한다. 

페치전략을 LAZY (지연로딩) 으로 설정하는게 일반적인 방법이다. 

그러나, 지연로딩을 설정해도 N+1 쿼리 문제가 발생하기도 한다. 

## 해결방법
*  `@BatchSize`를 조정
* `@Fetch`를 SUBSELECT로 설정
> 가장 간단한 방법은 `@BatchSize`를 조정하거나 `@Fetch`를 SUBSELECT로 설정하는 해결방법이 있다. 
> 하이버네이트 기술에 의존하는 방법이다. 

> 기술에 의존적이며 정적인 방법임으로 다른방식의 조회가 생겼을 때 문제가 될 수 있다.

* 성능에 문제가 될수 있는 부분은 Fetch Join 사용

### Fetch Join
성능 문제를 해결하기 위해서 JPQL에서만 존재한다.

```java
@Query(value = "select distinct p from Post p left join fetch p.attachments " +  
  "where p.postType =:postType and p.title like concat('%', :title, '%') order by p.lastModifiedDateTime desc",  
  countQuery = "select count(p) from Post p")  
Page<Post> findAllByPostType(@Param("postType") Post.PostType postType, @Param("title") String title, Pageable pageable);
```
* join 뒤에 fetch를 붙힌다.
* `countQuery`가 필요하다.

## 프레임워크 내 연관관계 페치 전략 
* 글로벌 전략은 FetchType.LAZY (지연로딩) 으로 설정한다.
* 상황에 따라 필요한 경우 FetchType.EAGER (즉시로딩) 으로 설정한다.
* 성능저하가 걱정되는 부분은 fetch join을 사용한다. 