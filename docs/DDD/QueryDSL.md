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
public Page<ApplicationSerachProjection> findApplAllListBySearchProjection(
                                            Long applNo,
                                            String email,
                                            String phoneNumber,
                                            String userId,
                                            String korName,String engName,Pageable pageable) {
        List<ApplicationSerachProjection> result=
     from(appl)
                
               .limit(pageable.getPageSize()).offset(pageable.getOffset())
            
                .select(Projections.constructor(ApplicationSerachProjection.class, appl.id,recruitPart.korName,cors.korName,major.korName,
                        appl.applicantInfo.korName,appl.applicantInfo.engName,appl.applicant.userId,appl.applicantInfo.phoneNum,appl.applicantInfo.email,
                        appl.status,appl.applId))
                .fetch();
    
        return new PageImpl<>(result,pageable,result.size());
```

