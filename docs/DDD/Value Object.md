# Value Object

값을 담는 그릇 식별자를 가지고 있지 않다. 두개 이상의 변수가 개념적으로 하나인 경우 밸류 타입을 이용할 수 있다. 엔터티의 데이터와 상관없을 경우, 밸류 기능을 가질 수도 있다

## 밸류 타입 매핑 방법

> 1. 엔터티에 여러 컬럼으로 매핑

1. 테이블에 매핑
2. 한 개 컬럼에 매핑
3. enum 매핑

### 1.엔터티에 여러 컬럼으로 매핑 - Embeddable

두개이상의 변수가 개념적으로 하나인 경우 보통사용함 테이블이 생기지 않고, **칼럼만 추가**된다

- 밸류타입

  ```java
  @Embeddable
  @Getter
  public class Grade {
      @Column(name = "GRAD_FORM_CODE", nullable = false)
      private String formCode;
  
      @Column(name = "GRAD_AVR", nullable = false)
      private String average;
  
      @Column(name = "GARD_FULL", nullable = false)
      private String full;
  }
  ```

- 엔터티 내부

  ```java
  @Embedded
  private Grade grade;
  ```

### 2.테이블에 매핑

**식별자 속성을 은닉 하여 구현

- `@CollectionTable` 로 매핑하여 구현 가능 하지만,** 그렇게 생성된 밸류 테이블은 엔터티가 아니여서 PK가 없다 (apex 사용하지 않음)
- PK가 없는 테이블이 가질수 있는 여러 문제점 때문에, 식별자를 은닉시켜 엔터티처럼 구성하지만, 구현하는 입장에서는 객체로 보이도록 구현한다

####`IdentifiedValueObject`

> 식별자를 은닉시켜 엔터티처럼 구성하지만, 구현하는 입장에서는 객체로 보이도록 구현한다

```java
@MappedSuperclass
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public abstract class IdentifiedValueObject extends AbstractBaseEntity{

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Getter(AccessLevel.PACKAGE)
@Setter(AccessLevel.PACKAGE)
@Column(name = "ID")
private Long id;
}
```

#### 적용주의 

#### 1. 밸류타입과 엔티티는 다대일 양방향 연관관계 설정

일대다 단방향 연관관계로 구현하는게 구현체가 간단하지만, **성능, 관리 문제**로 **다대일 양방향 연관관계**를 맺는게 마땅하다.

- 성능 문제 : 수정이 이루어질 때 예측보다 쿼리가 2배 날라간다.
- 관리 문제 : 테이블과 매핑된 테이블이 아닌 다른 엔티티에서 외래키를 관리한다
- 구현적으로 보았을때 엔티티와 엔티티 관계임으로 연관관계를 맺어준다.

#### 2. parent 안에 child의 getter를 private하게 만들거나, 만들지 않는다.

parent에서 child의 존재를 알지 못하거나 접근하지 못하도록 한다

### 3.  Child에서 parent  데이터 변경할수없게한다

> 읽기 전용 매핑 `(@JoinColumn(insertable=false, updatable=false))`를 이용해서 mappedBy 처럼  사용하게한다.

```java
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "POST_NO", insertable = false, updatable = false)
private Post post ;
```

#### 4. parent 삭제시, child도 삭제

```java
@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
@JoinColumn(name = "POST_NO")
private Set<Attachment> attachments;
```

### EX )

게시글(Post)  , 첨부파일(Attachment) 관계시

- parent

```java
@Entity
@Table(name = "POST")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Post extends AbstractBaseEntity {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(name = "POST_NO")
private Long id;

@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
@JoinColumn(name = "POST_NO")
private Set<Attachment> attachments;

}
```

- child

```java
@Entity
@Table(name = "ATTACH")
@Where(clause="DEL_YN ='N'")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Attachment extends IdentifiedValueObject {

    @ManyToOne(fetch = FetchType.LAZY )
    @JoinColumn(name = "POST_NO", insertable = false, updatable = false)
    private Post post;

    @Getter
    @Column(name = "FILE_ORIGIN_NAME")
    private String fileName;

    @Getter
    @Column(name = "FILE_PATH")
    private String filePath;

    @Getter
    @Column(name = "DEL_YN")
    @ColumnDefault(value = "false")
    private Boolean deleted;

}
```





### 3.한 개 컬럼에 매핑  `@Converter`

> 밸류타입을 한개 컬럼에 저장해야 할 때 방법이다.



### 4. **Enum `@Enumerated`**

> 자바의 enum 타입을 매핑할 때 사용



```
 @Enumerated(EnumType.STRING)
 @NonNull private FileType fileType;
```