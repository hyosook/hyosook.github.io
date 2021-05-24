---
title: "DDD설계 개념과 구현"
tags: ["DDD", "JPA"]
---



# 개념정의

## 도메인 모델의 엔티티

* 속성이 아닌 **식별성을 기준**으로 정의되는 도메인 객체.
  반드시 식별자가 있어야 하고, 식별자가 같으면 같은 Object 인 것이 보장되어야 한다.
* 단순히 데이터를 담고 있는 데이터 구조라기 보다 , **데이터**와 함께 **기능을 제공하는 객체**
  * 도메인 로직
* **두개이상의 데이터가 개념적으로 하나인 경우**, **벨류 타입**(Value Object )을 이용해서 표현할수 있다 



## Value Object 

* **식별자가 없으며** 영속성이 필요없는 객체

  * 값이 변경되면, 원본데이터를 찾기 어렵다 
  * 모든 데이터를 삭제하고, 모든 데이터를 다시 저장해야한다
  * 실무에서 값 타입 컬렉션이 매핑된 테이블에 데이터가 많다면, **일대다 관계를 고려(엔티티로만들어라)**

* 식별자 없는 물리테이블로 표현될수도 , 칼럼으로 표현될수도 있다 

* **불변타입**

  * 데이터 변경 기능을 제공하지 않는다 
  * set 없다 >>  생성자  등 사용 

* 벨류 타입은 **개념적으로 완전한 하나**를 표현할 때 사용한다. 

  예를들어  밸류타입인 File를 다음과 같이 작성할 수 있다.

  * ```java
    public class File {

      private String originName;
      private String path;
      private String size;
     ...
    }
    ```

* 벨류 타입을 사용할 때의 또다른 장점은 **밸류타입을 위한 기능을 추가** 할 수 있다. 

  *  s3파일 path가 붙은 전체 path 추가 가능 



## Aggregate

- `관련`된 모델을 `하나로 모은것`

- 애그리거트의 루트는 엔티티 므로 @Entity로 매핑 설정한다

- 애그리거트에 속한 객체는 유사하거나 ,**동일한 라이프 사이클**을 가진다 

  - 애그리거트에 속한 구성요소는 대부분 함께 생성하고 함께 제거한다 

    - ex) `상품` 과 `리뷰`  는 함께 생성되지 않고, 함께 변경되지 도 않는다 

      또한 변경 주체도 다르다

- 애그리거트는 **독립된 객체 군**이며, 각 에그리거트는 자기 자신을 관리할 뿐 다른 애그리거트를 관리하지 않는다 

- 애그리거트 내의 엔티티 및 밸류 객체의 데이터 수정은 애그리것 **루트**를 통해서만 수행

  - 다른 애그리것의 엔티티를 참조할 때 객체 직접 참조 대신 ID 참조 방식을 적용
    - 애그리것에 포함된 객체의 정보 수정은 애그리것 내에서만 가능하도록 제한

- 애그리거트에서 루트 엔터티를 뺀 나머지 구성요서는 **대부분 밸류**

  - 루트 엔터티 외 다른 밸류가 있다면 진짜 엔터티인지 의심 필요
  - 단지 별도 테이블에 데이터 저장한다고 해서 엔터티는 아님
  - 엔터티가 확실하다면 다른 애그리거트는 아닌지 확인 필요

  \- 예를 들어 쇼핑몰 사이트에서 주문 Entity내에 배송주소 정보를 우편번호 주소1, 주소2, 상세주소 이런식으로 각 컬럼으로 정의하는 것이 아니라, 주소라는 Value Object를 별도로 작성하고 주문 Entity는 주소 Value Object를 포함하는 방식으로 관계 일관성 및 단순화를 유지한다.

* AGGREGATE 1개당 REPOSITORY 1개
  - AGGREGATE ROOT를 통해서 밖에서 AGGEGATE 안의 객체로 접근함



### AGGREGATE 식별시 의식할 점

- CUD + 단순R(findById)에 집중
  - 모든 R을 다 포용하려고 한다면 깊은 객체 그래프가 나옴
- (JPA를 쓴다면) Cascade를 써도 되는 범위인가?

## Repository

- 리포지터리는 애그리거트의 저장소이다.

- 애그리거트를 저장, 조회, 삭제 > 리포지터리의 기본 기능


---

# 매핑 구현

## 1. 엔티티와 기본 벨류 매핑  

## Embeddable

- value object
- 기간(시작 날짜, 끝 날짜)이나 좌표(X, Y) 등 **항상 같이 다니는 값들**을 **객체**로 묶어서 **Entity의 속성**으로 지정
  - 테이블이 생기지 않고, **칼럼만 추가**된다 
- 객체의 클래스에 `@Embeddable` 어노테이션을 붙여서 선언
- Entity에서 `@Embedded`어노테이션을 붙여서 사용

```java
@Embeddable 
public class EmploymentPeriod {
  @Column(name="START_DATE")
  private java.sql.Date startDate;
 
  @Column(name="END_DATE")
  private java.sql.Date endDate;
  ....
}
```

```java
 @Embedded
  private EmploymentPeriod period;
```

## 2.**두개 이상의 프로퍼티**를 가진 밸류타입을  **한개의 칼럼**에 매핑



## @Converter

* 데이터를 변환해서 데이터베이스에 저장 

  * **두개 이상의 프로퍼티**를 가진 밸류타입을  **한개의 칼럼**에 **매핑**하기 위해서 사용
  * 사용 편의상 변환 사용 


### 자바의 boolean 사용 하고, 데이터베이스에 `y` `n` 으로 저장하기 

```java
@Converter
public class BooleanToYNConverter implements AttributeConverter<Boolean, Character> {
    @Override
    public Character convertToDatabaseColumn(Boolean attribute) {
        return (attribute != null && attribute) ? 'Y' : 'N';
    }

    @Override
    public Boolean convertToEntityAttribute(Character dbData) {
        return dbData == 'Y';
    }
}
```

- `AttributeConverter` 인터페이스 구현 
  - `convertToDatabaseColumn` : 엔티티의 데이터를 데이터베이스 칼럼에 저장할 데이터로 변환한다
  - `convertToEntityAttribute` :  데이터베이스에서 조회한 칼럼 데이터를 엔티티의 데이터로 변환 
- 제네릭에 현재타입과 변환할 타입을 지정해야한다 



### 칼럼 레벨에 적용

```java
 @Column(name = "MULTI_YN")
 @Convert(converter = BooleanToYNConverter.class)
 private boolean multi;
```

### 글로벌 설정

```java
@Converter(autoApply=true)
public class BooleanToYNConverter implements AttributeConverter<Boolean, Character> {
    @Override
    public Character convertToDatabaseColumn(Boolean attribute) {
        return (attribute != null && attribute) ? 'Y' : 'N';
    }

    @Override
    public Boolean convertToEntityAttribute(Character dbData) {
        return dbData == 'Y';
    }
}
```

- 모든 Boolean 타입에 컨버터를 적용한다 
- 따로  `@Convert`를 지정하지 않아도 된다 



##  3.Value Collection 

> 값 타입을 하나 이상 저장 하려면, 컬렉션에  보관해야하고  보관 방법은 두가지가 있다 



### 3-1.별도 `테이블` 매핑

>  밸류 컬렉션을 별도 테이블로 매핑하는 방법이다.
>
>  `@ElementCollection`을 통해 변경이 발생할 시, 전체를 지우고 다시 입력하는 이슈가 있음. 이를 완화시키기 위해서 `@OrderColumn`을 추가하는 방법이 있긴하나 완벽하지는 않음

#### Entity

```java java
@Entity
public class Order {
  @ElementCollection
  @CollectionTable(name = "order_line",
                  joinColumns= @JoinColumn(name = "order_number"))
  @OrderColumn(name = "line_idx")
  private List<OrderLine> orderLines;
}
```

- 밸류 컬렉션을 별도 테이블로 매핑할 땐, `@ElementCollection`, `@CollectionTable` 함께 사용

#### Value Object

```java java
@Embeddable
public class OrderLine {
  ..
}
```

- `List` 타입 자체가 인덱스를 가지기 때문에, 인덱스 값을 저장하기 위한 프로퍼티가 존재하지 않음
- Entity에 `@OrderColumn` 이용해서 지정한 칼럼에 인덱스 저장

> 사회혀신타운 프로젝트에서 `Portfolio`엔터티에 첨부인 `Attachment`밸류 컬렉션을 매핑한 방법



### 3-2.한 개 `칼럼` 매핑 (@Converter)

> 별도 테이블 아닌 한 개 칼럼에 저장해야 할때 방법이다.
>
> 예를 들어, 도메인 모델에는 이메일 주소 목록을 `Set`으로 보관하고 DB에는 한개 컬럼에 콤마로 구분해서 저장해야 할 때가 있다. 이때 `AttributeConverter`를 사용하면 밸류 컬렉션을 한 개 칼럼에 쉽게 매핑할 수 있다.

#### EmailSet

```java java
public class EmailSet {
  private Set<Email> emails = new HashSet<>();
  
  private EmailSet() {}
  private EmailSet(Set<Email> emails) {
    this.emails.addAll(emails);
  }
  
  public Set<Email> getEmails() {
    return Collections.unmodifiableSet(emails);
  }
}
```

#### Entity

```java java
@Column(name = "emails")
@Convert(converter = EmailSetConverter.class)
private EmailSet emailSet;
```

> 크라우드 프로젝트 `EvalAns`부분에 답변이 여러개인 경우 때문에, `items`에 콤마로 구분해서 여러값이 들어갈수 있도록 매핑한 방법
>
> `AttributeConverter` 를 구현해서 만든 `ListToStringConverter`가 매핑을 도와준다.
>
> 차이점 : 책에서는 `Set`타입 사용했지만 책임님은 `List<Object>` 사용, 코드집합 (예: itemSet 타입)이 필요하다고 했는데, 책임님은 만들지 않으셨다.



## Value Collection을 테이블로 저장할지, 컬럼으로 저장할지 결정하는 기준은?

책에 의하면,  `List`는 순서가 있는 타입이며 순서가 필요한 경우 `List<밸류컬렉션>` 으로 선언하여 테이블로 저장한다고만 짧게 언급했다. 



#  @OneToMany

* **@Entity** 끼리의  **매핑**을 위해서 사용된다

* 대부분의 다른 애그리거트에 있는,  엔티티 간의 관게를 표현 할때 사용된다 

  * 애그리거트는 ROOT 엔티티를 한개를 가진다 

    * ROOT 엔티티 끼리의 관계를 표현


* 간혹 개념적으로 벨류인데, 여러이유로  @Entity를 사용해야 할때도 사용된다 





# Enumerated

> 자바의 enum 타입을 매핑할 때 사용 

- Enum클래스

```JAVA
   public enum FileType {
        IMAGE, MOVIE, DOC, ETC
    }
```

- 매핑
  - EnumType.STRING : enum `이름`을 db에 저장
  - EnumType.ORDINAL: enum '순서' 를 db에 저장

```JAVA
/*첨부유형구분코드*/
    @Enumerated(EnumType.STRING)
    @NonNull private FileType fileType;
```



# @MappedSuperclass

- 추상 클래스와 유사

  - 단순히 매핑 정보를 상속할 목적으로만 사용된다

- 부모클래스는 테이블과 매핑하지않고, 부모클래스를 상속받는 자식클래스에게 매핑 정보만 제공한다 

- 여러엔티티에서 공통으로 사용하는 속성을 효과적으로 관리


```java
@EntityListeners(AuditingEntityListener.class)  // 생성 수정 자동 설정
@MappedSuperclass  // 부모설정 
@Data
public class AbstractBaseEntity implements Serializable {

    @CreatedDate
    @Column(name = "CREATED_AT", updatable = false)
    @JsonIgnore
    private LocalDateTime createdDateTime;

    @LastModifiedDate
    @Column(name = "LAST_MODIFIED_AT", updatable = true)
    @JsonIgnore
    private LocalDateTime lastModifiedDateTime;
}
```

```java
@Entity
@Table(name = "USER")
public class User extends AbstractBaseEntity implements Serializable {
    @Id
    @Column(name = "USER_ID")
    private String userId;

}
```





# jakson 

jackson은 자바진영 json 라이브러리로 잘 알려져 있지만 json 뿐만 아니라 XML, YAML, CSV 등 다양한 형식의 데이타를 지원하는 data-processing 툴이다.

스트림 방식이므로 속도가 빠르며 유연하며 다양한 third party 데이터 타입을 지원하며 annotation 방식으로 메타 데이타를 기술할 수 있으므로 JSON 의 약점중 하나인 문서화와 데이타 validation 문제를 해결할 수 있다.

- @JsonIgnore : VO의 멤버변수 위에 선언해서 제외처리 (비밀번호 같은거)
- @JsonProperty : json으로 변환 시에 사용할 이름이다. (DB 칼럼과 이름이 다르거나 api 응답과 이름이 다르지만 매핑시켜야 할 때)
- @JsonGetter : 어떤 필드값을 가져올 떄 이 메소드로 접근해서만 가져오라고 jackson에게 명시적으로 알린다.
- @JsonSetter : 위와 동일, Deserialize 때

