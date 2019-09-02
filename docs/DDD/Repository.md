# Repository
리포지터리는 애그리거트 단위이다. 

애그리거트를 저장, 조회, 삭제 하는 것이 리포지터리의 기본 기능이다.

## 기본 형태 
스프링 데이터 JPA를 상속받아서 interface 형태로 만든다.

```java 
public interface PostRepository extends JpaRepository<Post, Long> {  
  Optional<Post> findById(Long id);  
  
  Optional<Post> findByIdAndAuthor(Long id, User author);  
  
  Page<Post> findAllByPostTypeAndTitleContainingOrderByLastModifiedDateTimeDesc(Post.PostType postType, String title, Pageable pageable);  
  
  int deletePostById(Long id);  
  
  Post findTopByPostTypeOrderByLastModifiedDateTimeDesc(Post.PostType postType);  
}
```

## query method
스프링 데이터 JPA가 제공하는 기능

### Repository 제공 메소드

| method 이름 | 기능                                                    |
| ----------- | ------------------------------------------------------- |
| save()      | 레코드 저장 (insert, update)                            |
| findOne()   | primary key로 레코드 한건 찾기                          |
| findAll()   | 전체 레코드 불러오기. 정렬(sort), 페이징(pageable) 가능 |
| count()     | 레코드 갯수                                             |
| delete()    | 레코드 삭제                                             |



### query mehtod 이름 생성 전략
아래 이름으로 시작하는 메서드는 query method 임을 스프링에게 알린다.

| method 이름    | 설명                                          |
| -------------- | --------------------------------------------- |
| findBy로 시작  | 쿼리를 요청하는 메서임을 알림                 |
| countBy로 시작 | 쿼리 결과 레코드 수 요청하는 메서드 임을 알림 |



### 필드 쿼리 표현식
위의 findBy… 에 이어 아래와 같이 해당 repository 도메인의 필드이름을 입력하면 검색 쿼리를 실행한 결과를 전달한다. 
SQL의 where 절을 메서드이름을 통해서 전달한다.
메서드의 반환형이 도메인 객체이면 하나의 결과만 전달, 반환형이 List 이면 쿼리에 해당하는 객체 리트를 전달한다.

#### query method에 포함할 수 있는 키워드

| 메서드이름 키워드 | 샘플                                               | 설명                               |
| ----------------- | -------------------------------------------------- | ---------------------------------- |
| And               | findByEmailAndUserId(String email, String userId)  | 여러필드를 and 로 검색             |
| Or                | findByEmailOrUserId(String email, String userId)   | 여러필드를 or 로 검색              |
| Between           | findByCreatedAtBetween(Date fromDate, Date toDate) | 필드의 두 값 사이에 있는 항목 검색 |
| LessThan          | findByAgeGraterThanEqual(int age)                  | 작은 항목 검색                     |
| GreaterThanEqual  | findByAgeGraterThanEqual(int age)                  | 크거나 같은 항목 검색              |
| Like              | findByNameLike(String name)                        | like 검색                          |
| IsNull            | findByJobIsNull()                                  | null 인 항목 검색                  |
| In                | findByJob(String … jobs)                           | 여러 값중에 하나인 항목 검색       |
| OrderBy           | findByEmailOrderByNameAsc(String email)            | 검색 결과를 정렬하여 전달          |


#### Example
```java
List<User> findFirst10ByNameAndAgeGreaterThanEqualOrderByBirthday(String name, int age);
```



### 페이징



### Controller 

```java 
@GetMapping  
public ResponseEntity<Page<Post>> findPostAll( @PageableDefault Pageable pageable) { 

}
```

* 매개변수 `Pageable`
	
	* 요청 쪽에서 size, page 를 정해서 보내면  `Pageable`로 받아진다.  
	* `@PageableDefault` 사용시 , size, page를 보내지 않아도 default 값으로 `Pageable`를 받는다
	
	

### Repository

```java
Page<Post> findAllByPostTypeAndTitleContainingOrderByLastModifiedDateTimeDesc( Pageable pageable);
```
* 매개변수로 `Pageable` 필수



## JPQL

쿼리메소드에서 해결되지 않는 부분은 jpql을 직접 작성할 수 있다. 
jpql은 JPA에서 쿼리문이다. 
* 테이블 명은 객체 명으로 사용한다. 
* 객체 선언할 때 별칭이 필수이다 (`from ReqEvalInfo a`)
* 몇몇 가지 메소드를 제공한다. (예: max, sum 등)

```java
@Query(value = "select max(a.evalOrderSeq) 
				from ReqEvalInfo a 
				where a.req.reqNo =:reqNo and a.evalTypeCode =:evalTypeCode")  
Integer findMaxOrderSeq(@Param("reqNo") int reqNo, @Param("evalTypeCode") String evalTypeCode);
```

#### 쿼리를 통해 가공한 데이터 매핑 - NEW 명령어 

조회 쿼리를 작성하며 가공된 데이터가 포함된 객체를 매핑하기 위해서는 해당 객체에 가공 데이터를 포함한 생성자를 만들고 **NEW 명령어**를 사용해서 매핑한다.

```java
@Query(value = "select new kr.co.dto.UserDto(u.role.id,u.name)from " +
            "User u,role r)
 List<UserDto> userList();
```

```java
@Getter
public class UserDto {

    private String name;
    private Object id;
   

    public FestaTeamDto(Long id, String name) {
        this.id = id;
        this.name = name;

    }
}
```

* **생성자  순서 일치 필수 **



## native query

JPQL에서 제공하지 않는 DB 기능 등이 필요하면 직접 쿼리를 작성할 수 있다. 

#### Repository

> 기본은 리턴결과가  `List<Object[]>` 로 온다 

```java
@Query(value = "select u.name as name , r.id as id  #AS 명 일치 필수
       from user u join role r on u.userId =r.userId ", 
       nativeQuery = true)  
List<UserDto> userList();
```

* nativeQuery = true
* DTO `명` 일치 해야한다 

#### DTO

```JAVA
public interface UserDto {

    String getName();

    Object getId();

}
```





