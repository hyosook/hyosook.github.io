# QueryDSL

- 코드로 작성한다 .IDE의 도움을 받을수있다 
- 컴파일 시점에 문법 오류를 잡을수 있다
- 동적 쿼리
- 쿼리를 코드로 리팩토링 할수있다.

-  QueryDSL 전용 객체를 만들어서 사용한다 

  

## build.gradle 설정 

> `build`시 빌드 폴더에 Entity 클래스에 매핑되는 QEntity 클래스가 생성된다

```java
plugins {
    id 'com.ewerk.gradle.plugins.querydsl' version '1.0.10'
}



dependencies {
    implementation 'com.querydsl:querydsl-jpa'
}


def querydslDir = 'build/generated/querydsl'

querydsl {
    library = 'com.querydsl:querydsl-apt'
    jpa = true
    querydslSourcesDir = querydslDir
}

sourceSets {
    main.java.srcDirs += [querydslDir]
}

configurations {
    querydsl.extendsFrom compileClasspath
}

compileQuerydsl {
    options.annotationProcessorPath = configurations.querydsl
}
```

> * querydsl-apt: Querydsl 관련 코드(ex: QHello) 생성 기능 제공
>
> * querydsl-jpa: Querydsl JPA 관련 기능 제공



## Repository 구성



### 사용자정의 인터페이스 생성

```java
public interface UserRepositoryCustom {
    
}
```

* 이전에 만든 도메인 Repository와 같은 이름으로 시작해야한다

  

### 스프링 데이터 JPA 리포지토리에 사용자 정의 인터페이스 상속

````java
public interface UserRepository extends JpaRepository<User,Long>,UserRepositoryCustom{
    
}
````

* 사용자 정의 인터페이스를 상속하도록 수정 

### 사용자정의 쿼리 구현 클래스

```java
public class UserRepositoryImpl extends QuerydslRepositorySupport implements UserRepositoryCustom {

    public UserRepositoryImpl() {
        super(User.class);
    }
```

* 구현 클래스이름은 반드시  `인터페이스이름+Impl`이어야 한다.
* ` @Repository ` 어노테이션  없이, 스프링에서 클래스 이름을 보고  자동으로 `bean` 등록을 해줌 



## Q-Type 인스턴스 사용 

1. 직접 QClass 인스턴스를 생성해서 사용하기

```
QTest qTest = new QTest("t");
```

 

2. QClass에 이미 생성된 기본 인스턴스 사용하기

```
QTest qTest = QTest.test;
```

> 해당 방법 사용 



### 검색 조건 

* eq
* ne
* eq("단어").not()
* isNotNull()
* in()
* notIn()
* between(a,b)
* like("단어%")          // like "단어%"
* contains("단어")   // like "%단어%"
* startsWith()

### AND 연산자 사용 

1. 체이닝 기법 

   ``` java
   .where(user.username.eq("test").and(user.id.eq("test1")))
   ```

   

2. 파라미터로 적용

   ```java
   .where(user.username.eq("test"), user.id.eq("test1"))
   ```

   * Predicate 인자를 받는 곳에 null 값을 넘기면 조건절을 만들 때 무시

```


```

### 결과 

#### featch()

* 조회 대상이 여러건 일경우 
* 컬렉션 반환
* 데이터가 없으면 빈 리스트 

### featchOne()

* 단건 조회 
* generic에 지정한 타입으로 반환
* 결과 없으면 null 

### fetchFirst ()

* return limit(1).featchOne()

### fetchResults ()

* 결과 `QueryResults ` 
* 페이징 정보와 목록 있음 
* count 쿼리가 추가로 실행된다

### fetchCount ()

* 개수 조회. long 타입 반환



### 조인

join, innerJoin, leftJoin, rightJoin 을 지원한다.
개인적으로 from절에 multiple arguments를 주는것보다 이게 더 좋다.(SQL에서도…)

```
QTeam team = QTeam.team;

List<Member> foundMembers = 
    queryFactory.selectFrom(member)
    .innerJoin(member.team, team)
    .fetch();
```

join의 첫번쨰 인자로는 join할 대상, 두번쨰 인자로는 join할 대상의 쿼리 타입을 주면 된다. on 절은 자동으로 붙는다.

- 추가적인 on 절도 사용할 수 있다.

  ```
  List<Member> foundMembers = 
      queryFactory.selectFrom(member)
      .innerJoin(member.team, team)
      .on(member.username.eq("joont"))
      .fetch();
  ```

### 조건

```
List<Member> foundMembers = 
    queryFactory.selectFrom(member)
    .where(member.username.eq("joont")) // 1. 단일 조건  
    .where(member.username.eq("joont"), member.homeAddress.city.eq("seoul")) // 2. 복수 조건. and로 묶임  
    .where(member.username.eq("joont").or(member.homeAddress.city.eq("seoul"))) // 3. 복수 조건. and나 or를 직접 명시할 수 있음  
    .where((member.username.eq("joont").or(member.homeAddress.city.eq("seoul"))).and(member.username.eq("joont").or(member.homeAddress.city.eq("busan"))))
    .fetch();
```

`(E1 and E2) or (E3 and E4)` 같은 형태도 가능하다. 그냥 괄호로 묶어주면 된다.

```
List<Member> foundMembers = 
    queryFactory.selectFrom(member)
    .where((member.username.eq("joont").or(member.homeAddress.city.eq("seoul"))).and(member.username.eq("joont").or(member.homeAddress.city.eq("busan"))))
    .fetch();
```

두가지 조건이 괄호로 묶이게 되었을때, or 이면 합집합이고 and 이면 교집합이다.
참고로 `(E1 and E2) or (E3 and E4)` 는 괄호가 생략되고 `(E1 or E2) and (E3 or E4)` 는 잘 동작한다.

### 그룹핑

group by도 가능하다.

```
List<String> foundCities = 
    queryFactory.from(member)
    .select(member.homeAddress.city)
    .groupBy(member.homeAddress.city)
    .fetch();
```

city로 group by 한 뒤 city만 출력하게 된다.

- having도 가능하다. 집계함수도 쓸 수 있다.

  ```
  List<String> foundItems = 
      queryFactory.select(item.category) // category가 그냥 String이라고 가정
      .from(item)
      .groupBy(item.category)
      .having(item.price.avg().gt(1000)) // 집계함수 사용
      .fetch();
  ```

### 정렬

```
List<Member> foundMembers = 
    queryFactory.selectFrom(member)
    .orderBy(member.id.asc(), member.username.desc())
    .fetch();
```





## 동적 조건

`com.querydsl.core.BooleanBuilder` 를 사용하면 동적 조건을 생성할 수 있다.

```
BooleanBuilder builder = new BooleanBuilder();
if(param.getId() != null){
    builder.and(member.id.eq(param.getId()));
}
if(param.getName() != null){
    builder.and(member.name.contains(param.getName()));
}

List<Member> list = 
    queryFactory.selectFrom(member)
    .where(booleanBuilder)
    .fetch();
```



### 동적쿼리 BooleanBuilder

* 조건이 있으면 넣고, 없으면 안넣고 
* 이걸로 동적 쿼리가 됨 
* 단 문제점이 , 쿼리 형태 예측이 힘들다 
* 

### BooleanExpression

BooleanExpression은 `where`에서 사용할 수 있는 값인데, 이 값은 `,`를 `and`조건으로 사용합니다.
여기에 Querydsl의 `where`는 **null이 파라미터로 올 경우 조건문에서 제외**합니다.

BooleanExpression을 리턴하는데, 각 메소드에서 **조건이 맞지 않으면 null을 리턴**합니다.
null을 리턴하니 `where`에서는 상황에 따라 조건문을 생성하게 됩니다.



### Where

where 메서드는 Predicate를 여러 개 받을 수 있도록 선언되어 있다. **null 값을 전달하면 무시**하므로 검색 조건에 해당하는 값을 각 파라미터에 맞게 조건절을 생성하고 동적 쿼리를 쉽게 만들 수 있다. usernameEq 메서드의 경우에 username 값이 null 이면 null을 반환한다. 값이 있는 경우에는 조건절을 생성하고 BooleanExpression(Predicate 인터페이스를 구현한 클래스)를 반환한다.





### extract method , 쿼리를 코드로 리팩토링 가능

```JAVA
return 
          .where(
                user.name.eq(name),
                isServiceable()
      )
      .fetch();

private BooleanExpression isServiceable() {
   return user.status.eq("ACTIVE");
}
```



```java
 private BooleanExpression containsUserName(String applicantName) {
        return StringUtils.isEmpty(applicantName) ? null :
                (applCancelHistory.korName.contains(applicantName).or(applCancelHistory.engName.contains(applicantName)));
    }
```





## 서브쿼리

com.mysema.query.jpa.JPASubQuery를 생성해서 사용

- unique() : 결과 한건일 때 사용
- list() : 여러건일때 사용

```JAVA
QItem item = QItem.item;
QItem itemSub = new Qitem("itemSub");

query.from(item)
    .where(item.price.eq(
        new JPASubQuery().from(itemSub).unique(itemSub.price.max())
     ))
     .list(item);

QItem item = QItem.item;
QItem itemSub = new Qitem("itemSub");

query.from(item)
        .where(item.in(
                new JPASubQuery().from(itemSub).where(item.name.eq(itemSub.name)).list(itemSub)
          ))
        .list(item);
```

```java
QMember member = QMember.member;
QMember subQueryMember = new QMember("subQueryMember"); // 추가로 생성해줘야 함  

List<Tuple> foundMembers = 
    queryFactory.select(member.name, member.homeAddress.city)
        .from(member)
        .where(member.name.in(
                JPAExpressions.select(memberForSubquery.name)
                .from(memberForSubquery)
        ))
        .fetch();
```



## case when  사용

```java

  new CaseBuilder()
                        .when(tbUser.userType.eq("1")).then("일반")
                        .when(tbUser.userType.eq("2")).then("관리자")
                        .otherwise("").as("rolename")
 

```

CaseBuilder : CASE WHEN 문법 시작
when : 조건문
then : when절이 true일 경우 실행
otherwise : when절이 false 일 경우 실행
`otherwise`가 끝나면 결과 물에 대한 alias(`as`) 적용

- 일반적으로는 Entity의 필드명이 자동으로 as 적용됨
- 하지만 `CaseBuilder`를 통해 계산한 결과물은 필드명이 없음
- alias 하지 않으면 결과물 필드가 어떤 필드로 가야할지 명시되지 않아 **오류가 발생**



## DTO로 리턴

위의 `bean` 메서드를 호출하면 전달받은 인자와 동일하게 MemberDTO의 setter를 호출한다.
`field` 메서드를 사용하면 필드에 직접 접근하고(private도 가능),
`constructor` 메서드를 사용하면 생성자를 사용한다. 지정한 프로젝션과 파라미터 순서가 같은 생성자가 필요하다.



```JAVA
@Getter
@Setter
@AllArgsConstructor
public class MemberDTO {
    private String memberName;
    private int age;
}

List<MemberDTO> resultList =
      query.from(member).list(
              Projections.bean(MemberDTO.class, member.name.as("memberName"), member.age) // 프로퍼티 setter 접근
              // Projections.fields(MemberDTO.class, member.name.as("memberName"), member.age) // 필드 직접 접근
              // Projections.constructor(MemberDTO.class, member.name, member.age) // 생성자 사용
      );
```







## 페이징

```JAVA
 QueryResults<Appl> result =
                from(appl)
                    .limit(pageable.getPageSize()).offset(pageable.getOffset())
                    .select(appl)
                    .fetchResults();
        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
```

- limit : 최대 결과 수
- offset : 결과 시작행
- `fetchResult`를 호출했기 때문에 목록 내용과 총 개수를 얻기 위해서 2개의 쿼리를 실행한다.



### Count 쿼리 최적화

- 첫 번째 페이지이면서 컨텐츠 개수가 페이지 크기보다 작은 경우
- 마지막 페이지인 경우 (현재 위치 값과 컨텐츠 개수를 합치면 전체 크기를 구할 수 있음)

`PageableExecutionUtils` 유틸 클래스의 `getPage` 메서드 사용함으로써 경우에 따라 Count 쿼리를 생략할 수 있다.

```java
 
        List<Appl> contents = 
            from(appl)
             .select(appl)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                
                .fetch();

        JPQLQuery<Appl> countQuery = 
            from(appl)
                .select(appl);
        return PageableExecutionUtils.getPage(contents, pageable, countQuery::fetchCount);
    }

```

* 전체 데이터의 갯수를 가져 오는 쿼리를 별도로 만들어서 쓴다
* 별도 데이터 컬럼등은 가져올 필요없음 









## Dialect

### yml 설정

```bash
spring:
  jpa:
    show-sql: false
    open-in-view: false
    hibernate:
      database-platform: kr.co.apexsoft.granet2.admin_api._config.MysqlCustomDialect
  datasource:
```

### CustomDialect

```java
public class MysqlCustomDialec extends MySQL57Dialect {
    public MysqlCustomDialect() {
        super();
        registerFunction("PART_CODEVAL", new StandardSQLFunction("PART_CODEVAL", StandardBasicTypes.STRING));

}
```

### functino 호출

```java
Expressions.stringTemplate("PART_CODEVAL('KR',{0},{1})", appl.part.schoolCode, appl.part.courseCode)
```





## 잘못된 사용

* Querydsl

```java
from(recommend)
                    .innerJoin(appl).on(appl.id.eq(recommend.appl.id))
                    .where(recommend.id.eq(recNo)
                    )
                    .select(Projections.constructor(RecommendProfProjectionKDI.class,
                            recommend.appl.id,recommend.appl.applicantInfo.engName,recommend.email
                           )
                    )
                    .fetchOne()
```

* sql

```sql
select
        recommend0_.APPL_NO as col_0_0_,
        appl2_.ENG_NAME as col_1_0_,
        recommend0_.PROF_EMAIL as col_2_0_ 
    from
        APPL_REC recommend0_ cross 
    join
        APPL appl2_ 
    inner join
        APPL appl1_ 
            on 1=1 
    left outer join
        APPL_GEN appl1_1_ 
            on appl1_.APPL_NO=appl1_1_.APPL_NO 
    left outer join
        APPL_FORN appl1_2_ 
            on appl1_.APPL_NO=appl1_2_.APPL_NO 
    left outer join
        APPL_PART_DETAIL appl1_3_ 
            on appl1_.APPL_NO=appl1_3_.APPL_NO 
    where
        recommend0_.APPL_NO=appl2_.APPL_NO 
        and (
            appl1_.APPL_NO=recommend0_.APPL_NO
        ) 
        and recommend0_.APPL_REC_NO=1
```



## 옳은 방법

* Querydsl

```java
 from(recommend)
                    .where(recommend.id.eq(recNo)
                    )
                    .select(Projections.constructor(RecommendProfProjectionKDI.class,
                            recommend.appl.id,recommend.appl.applicantInfo.engName,recommend.email
                           )
                    )
                    .fetchOne()
```

* sql

```bash
select
    recommend0_.APPL_NO as col_0_0_,
    appl1_.ENG_NAME as col_1_0_,
    recommend0_.PROF_EMAIL as col_2_0_ 
from
    APPL_REC recommend0_ cross 
join
    APPL appl1_ 
where
    recommend0_.APPL_NO=appl1_.APPL_NO 
    and recommend0_.APPL_REC_NO=1
```


​		

