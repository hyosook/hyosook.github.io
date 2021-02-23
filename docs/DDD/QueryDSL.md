# QueryDSL



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

### 중복 조건 함수추출

```JAVA
@Override
    public List<Academy> findDynamicQueryAdvance(String name, String address, String phoneNumber) {
        return queryFactory
                .selectFrom(academy)
                .where(eqName(name),
                        eqAddress(address),
                        eqPhoneNumber(phoneNumber))
                .fetch();
    }

    private BooleanExpression eqName(String name) {
        if (StringUtils.isEmpty(name)) {
            return null;
        }
        return academy.name.eq(name);
    }

    private BooleanExpression eqAddress(String address) {
        if (StringUtils.isEmpty(address)) {
            return null;
        }
        return academy.address.eq(address);
    }

    private BooleanExpression eqPhoneNumber(String phoneNumber) {
        if (StringUtils.isEmpty(phoneNumber)) {
            return null;
        }
        return academy.phoneNumber.eq(phoneNumber);
    }
```



```JAVA
return query.selectFrom(coupon)
            .where(
                 coupon.type.eq(typeParam),
                 isServiceable()
       )
       .fetch();
​
private BooleanExpression isServiceable() {
    return coupon.status.wq("LIVE")
        .and(marketing.viewCount.lt(markting.maxCount));
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
 QueryResults<ApplicationSerachProjection> result =
                from(appl)
                    appl.part.majorCode.eq(major.id)
                    )
                   
                    .limit(pageable.getPageSize()).offset(pageable.getOffset())
                    .select(Projections.constructor(ApplicationSerachProjection.class,
                            appl.id,
                            recruitPart.korName,
                            cors.korName,
                            major.korName,
                            appl.applicantInfo.korName,
                            appl.applicantInfo.engName,
                            appl.applicant.userId,
                            appl.applicantInfo.phoneNum,
                            appl.applicantInfo.email,
                            appl.status,appl.applId))
                    .fetchResults();
        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
```









```java
@Entity
@Table(name="APPL_REC")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Recommend extends AbstractBaseEntity {

    @Id
    @Column(name = "APPL_REC_NO")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "APPL_NO")
    private Appl appl;

}
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