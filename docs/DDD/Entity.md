# Entity

- 반드시 레코드를 구분하는 식별자를 가지고 있다.
- 도메인 기능을 가지고 있다.

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

## 기본 형태

```
    @Entity
    @Table(name = "POST")
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    public class Post extends AbstractBaseEntity {

    	@Id
    	@GeneratedValue(strategy = GenerationType.IDENTITY)
    	@Column(name = "POST_NO")
    	private Long id;

      @Column(name = "TITLE")
    	private String title;

      @Column(name = "CONTENT")
    	private String content;

    	// 도메인 기능
    	public void changeAuthor(User author) {
    		this.author = author;
    	}

    }
```

- `AbstractBaseEntity`를 상속

### 기본키 정의 3가지

- 단일 기본키 - `@Id`

- `@GeneratedValue(strategy = GenerationType.IDENTITY)`로 기본키 자동 생성 (Auto Increment) 설정 방법 (인조식별자에 사용)

- 두개 이상의 복합키 -`@EmbeddedId`, `@IdClass`

  - 물리적인 차이점은 없음

  - `EmbeddedId`로 하면 재사용성이 높아지며, 의미부여가 가능하다.

  - 일회적인 복합키의 경우 `IdClass`로 사용

  - `@EmbeddedId`

    > 재사용성이 높아지며, 의미 부여 가능

    - Entity 복합키 선언 부분

    ```
        @EmbeddedId
         private CommCodeId commCodeId;
    ```

    - 복합키 객체

    ```
       @Embeddable
       public class CommCodeId implements Serializable {
        private String codeGrp;
        private String code;
       }
    ```

  - `@IdClass`

    > 일회적 사용

    - Entity 복합키 선언 부분

    ```
        @Entity
        @Table(name = "COMM_CODE")
        @Getter
        @AllArgsConstructor
        @NoArgsConstructor(access = AccessLevel.PROTECTED)
        @IdClass(CommCodeIds.class)
        public class CommonCode {
            /* 공통코드 그룹 */
            @Id
            @Column(name = "CODE_GRP")
            private String codeGrp;
    
            @Id
            @Column(name = "CODE")
            private String code;
    
            /* 코드값 */
            @Column(name = "CODE_EN_VAL")
            @NonNull private String codeEnValue;
        }
    ```

    - 복합키 객체

    ```
        public class CommCodeId implements Serializable {
         private String codeGrp;
         private String code;
        }
    ```

### 생성자

- AccessLevel이 PROTECTED인 기본 생성자 필수 필요 `@NoArgsConstructor(access = AccessLevel.PROTECTED)` JPA 프로바이더가 DB에서 읽어온 데이터를 객체에 매핑할 때 기본 생성자를 사용한다. 기본 생성자를 다른 코드에서 사용하면 값이 온전하지 못한 객체를 만들게 됨으로, JPA 프로바이더만 사용할수 있도록 protected로 선언한다.

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