# Entity
* 반드시 레코드를 구분하는 식별자를 가지고 있다.
* 도메인 기능을 가지고 있다.

## 기본 형태

```java
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)  
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends AbstractBaseEntity { 
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "post_no")  
	private Long id;
	
	private String title;
	
	private String content;
	
	// 도메인 기능
	public void changeAuthor(User author) {  
		this.author = author;  
	}  
  
	public void addAttachment(Attachment attachment) {  
		this.attachments.add(attachment);  
	}
}
```
* `AbstractBaseEntity`를 상속

#### 기본키 정의
* 단일 기본키 - `@Id` 
* 두개 이상의 복합키 - `@EmbeddedId`
	* Entity 복합키 선언 부분

		```java
		@EmbeddedId  
		 private CommCodeId commCodeId;
		```
	
	* 복합키 객체

		```java
		@Embeddable  
		public class CommCodeId implements Serializable {
		 private String codeGrp;
		 private String code;
		}
		```

* `@GeneratedValue(strategy = GenerationType.IDENTITY)`로 기본키 자동 생성 (Auto Increment) 설정 방법 (인조식별자에 사용)

#### 생성자
* AccessLevel이 PROTECTED인 기본 생성자 필수 필요
	```java 
	@NoArgsConstructor(access = AccessLevel.PROTECTED)  
	```
	 JPA 프로바이더가 DB에서 읽어온 데이터를 객체에 매핑할 때 기본 생성자를 사용한다. 
	기본 생성자를 다른 코드에서 사용하면 값이 온전하지 못한 객체를 만들게 됨으로, JPA 프로바이더만 사용할수 있도록 protected로 선언한다. 
	
#### `@Column` 속성
* name : 컬럼명 지정. 정의하지 않으면 변수명이 컬럼명이 된다. 
* columnDefinition : 데이터베이스 컬럼 정보 설정 (예 : 크기)

#### `@ColumnDefault`
* 컬럼의 기본값 설정 방법 
* `@Coulmn`의 `columnDefinition` 속성으로 기본값을 정의하는 것 외 다른 방법 

ex) `@ColumnDefault(value = "false")`

#### `@Transient`
데이터베이스에 저장하지 않지만, 객체에 어떤 값을 보관하고 싶을 때 사용한다.