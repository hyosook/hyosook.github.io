# Value Object
* 값을 담는 그릇
* 엔터티와 다르게 식별자를 가지고 있지 않다. 
* 두개 이상의 변수가 개념적으로 하나인 경우 밸류 타입을 이용할 수 있다.
* 엔터티의 데이터와 상관없을 경우, 밸류 기능을 가질 수도 있다. 



## 밸류 타입을 매핑하는 방법 

1. 엔터티에 여러 컬럼으로 매핑
2. 테이블에 매핑
3. 한 개 컬럼에 매핑
4. enum 매핑



## 1.엔터티에 여러 컬럼으로 매핑 - Embeddable 

> 테이블이 생기지 않고, **칼럼만 추가**된다 

#### 밸류타입
```java
@Embeddable   
public class EmploymentPeriod {  
 @Column(name="START_DATE")  
 private LocalDate startDate;  
    
 @Column(name="END_DATE")  
 private LocalDate endDate;  
 ....  
}
```
####  엔터티 내부 
```java
@Embedded  
private EmploymentPeriod period;
```



## 2.테이블에 매핑 

* `@Embeddable`로 구현한 후 `@CollectionTable`로 매핑하여 구현할수 있다. 

* 하지만, 그렇게 생성된 밸류 테이블은 엔터티가 아니여서 **PK가 없다.** 
  * PK가 없는 테이블이 가질수 있는 여러 문제점 때문에, 식별자를 은닉시켜 엔터티처럼 구성하지만, 구현하는 입장에서는 객체로 보이도록 구현한다. 
  * 상속을 이용해서 **식별자 속성을 은닉**시킨다.

```java
@Entity  
@Table(name = "Child")  
@NoArgsConstructor(access = AccessLevel.PROTECTED)  
public class Child extends IdentifiedValueObject {
  // 밸류타입 구현
  ...  
}
```
* `IdentifiedValueObject` 를 상속받는다. 

### 밸류타입과 엔티티는 다대일 양방향 연관관계
구현적으로 보았을때 엔티티와 엔티티 관계임으로 연관관계를 맺어준다. 

일대다 단방향 연관관계로 구현하는게 구현체가 간단하지만, 성능, 관리 문제로 **다대일 양방향 연관관계**를 맺는게 마땅하다.

  * 성능 문제 : 수정이 이루어질 때 예측보다 쿼리가 2배 날라간다. 
  * 관리 문제 : 테이블과 매핑된 테이블이 아닌 다른 엔티티에서 외래키를 관리한다. 

```java
@Entity  
@Getter 
@NoArgsConstructor(access = AccessLevel.PROTECTED)    
public class Parent extends AbstractBaseEntity {  
	@Id  
	@GeneratedValue(strategy = GenerationType.IDENTITY)  
	@Column(name = "parent_no")  
	private Long id;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_no")
	private Set<Child> childs = new HashSet<>();
}
```
* parent 안에 child의 getter를 private하게 만들거나, 만들지 않는다. 
  * parent에서 child의 존재를 알지 못하거나 접근하지 못하도록 한다.

```java
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)   
public class Child extends IdentifiedValueObject {
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_no", insertable = false, updatable = false)
	private Parent parent;

	@Getter
	private String childname;
}
```



## 3.한 개 컬럼에 매핑 - `@Converter`

밸류타입을 한개 컬럼에 저장해야 할 때 방법이다. 

#### Example
#### EmailSet
```java
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
#### 엔터티 내부 
```java
@Column(name = "emails")  
@Convert(converter = EmailSetConverter.class)  
private EmailSet emailSet;
```
#### EmailSetConverter
```java
@Converter
public class EmailSetConverter implements AttributeConverter<EmailSet, String> {
	@Override
	public String convertToDatabaseColumn(EmailSet attribute) {
		if (attribute == null) return null;
		return attribute.getEmails().stream()
				.map(Email::toString)
				.collect(Collectors.joining(","));
	}

	@Override
	public EmailSet convertToEntityAtrribute(String dbData) {
		if (dbData == null) return null;
		String[] emails = dbData.split(",");
		Set<Email> emailSet = Arrays.stream(emails)
				.map(value -> new Email(value))
				.collec(toSet());
		return new EmailSet(emailSet);
	}
}
```
* emails 한 컬럼에 콤마로 구분된 데이터들이 들어간다. (test@d.com, test@t.com)
* 콤마로 구분된 데이터들을 객체에서 컬렉션으로 변환된다.



##  4. Enum  `@Enumerated`

> 자바의 enum 타입을 매핑할 때 사용 

```java

    @Enumerated(EnumType.STRING)
    @NonNull private FileType fileType;
```

- EnumType.STRING : enum `이름`을 db에 저장
- EnumType.ORDINAL: enum `순서'`를 db에 저장

```java
 public enum FileType {
        IMAGE, MOVIE, DOC, ETC
    }
```



