# Entity



반드시 레코드를 구분하는 식별자를 가지고 있다. 도메인 기능을 가지고 있다.

## 기본 형태

```java
@Entity
@Table(name = "APPL")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)  #생성자
@AllArgsConstructor
public class Appl extends AbstractBaseEntity {

#기본키 정의

#칼럼

#ENUM

#도메인로직

}
```

### 생성자

- `@NoArgsConstructor(access = AccessLevel.PROTECTED)`  필수
  - JPA 프로바이더가  DB에서 읽어온 데이터를 객체에 매핑할 때 기본 생성자를 사용한다.
  - 기본 생성자를 다른 코드에서 사용하면 값이 온전하지 못한 객체를 만들게 됨으로, JPA 프로바이더만 사용할수 있도록 protected로 선언

### 기본키 정의

1. 단일 기본키

   - 기본키 직접 할당

   ```java
   @Id
   @Column(name = "NO")
   private Long id;
   ```

2. 단일 기본키 _ 자동생성

   - `@GeneratedValue(strategy = GenerationType.IDENTITY)`로 기본키 자=동 생성 (Auto Increment) 설정 방법 (인조식별자에 사용)

   ```java
   @I
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "NO")
   private Long id;
   ```

3. 두개 이상의 복합키 -`@EmbeddedId`, `@IdClass`

- 물리적인 차이점은 없음

- `EmbeddedId`로 하면 재사용성이 높아지며, 의미부여가 가능하다.

- 일회적인 복합키의 경우 `IdClass`로 사용

- `@EmbeddedId`

  > 객체지향적 방식  ( 재사용성이 높아지며, 의미 부여 가능 )

  - Entity 복합키 선언 부분

  ```
  @Entity
  @Data
  public class Parent {
  
    @EmbeddedId
    private ParentId id;
  
    private String name;
  
  }
  ```

  - 복합키 객체

  ```
  @Data
  @Embeddable
  public class ParentId implements Serializable {
  
    @Column(name = "PARENT_ID1")
    private String id1;
  
    @Column(name = "PARENT_ID2")
    private String id2;
  }
  ```

  - id 객체 사용시

    `parent.id.id1`

- `@IdClass`

  > 일회적 사용

  - Entity 복합키 선언 부분

  ```
  @Entity
  @Data
  @IdClass(ParentId.class)
  public class Parent {
  
    @Id
    @Column(name = "PARENT_ID1")
    private String id1;
  
    @Id
    @Column(name = "PARENT_ID2")
    private String id2;
  
    private String name;
  }
  ```

  - 복합키 객체

  ```
    public class ParentId implements Serializable{
  
    private String id1;
  
    private String id2;
  }
  ```

### 칼럼

```java
@Column(name = "APPL_STS_CODE")
    @Enumerated(EnumType.STRING)
    private Status status;
```

`@Column` 속성

- name : 컬럼명 지정. 정의하지 않으면 변수명이 컬럼명이 된다.
- columnDefinition : 데이터베이스 컬럼 정보 설정 (예 : 크기)

```
@ColumnDefault
```

- 컬럼의 기본값 설정 방법
- `@Coulmn`의 columnDefinition 속성으로 기본값을 정의하는 것 외 다른 방법

ex) `@ColumnDefault(value = "false")`



### `@Transient`

데이터베이스에 저장하지 않지만, 객체에 어떤 값을 보관하고 싶을 때 사용한다.

### `@Formula`

```
@Formula("IF(SCHL_CODE = '999', SCHL_NAME, (SELECT S.SCHL_KOR_NAME FROM SCHL S WHERE S.SCHL_CODE = SCHL_CODE))")
private String korName;
```

### 명명 규칙 ( 엔터티,컬럼)

> 정의하는 그대로 사용하도록 설정

```bash
jpa:
    hibernate
      naming:
        physical-strategy: org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
        implicit-strategy: org.hibernate.boot.model.naming.ImplicitNamingStrategyLegacyJpaImpl
```

### ddl-auto 설정

- 초기 : create, update로 설정해서 프로토타입 DB 테이블 생성
- 프로토타입 완성 후 : validate로 변경해서 DB에 테이블 생성 후 엔터티 작성해서 매핑
- 운영 : none