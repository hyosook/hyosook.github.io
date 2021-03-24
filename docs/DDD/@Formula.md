동적으로 값을 제어하기 때문에 FK나 ENUM을 사용하지 못한 경우 혹은 공통코드 값을 써야 하면 String 컬럼으로 매핑된다.

ibatis 사용시엔 항상 쿼리를 작성하기 때문에 쿼리 작성시에 조회하면 됐지만, JPA에서 굳이 동적 컬럼 한두개만을 위해서 직접 쿼리를 작성하기엔 부담스럽다. 

이런 경우 `@Formula` 를 사용할 수 있다. 
`@Formula`는 Hibernate 기능이다.

### 사용 방법 

```java
@Entity
public class Document {
	..
	@Column(name="ITEM_CODE")
	private String itemCode; // 공통코드

	@Formula("CODEVAL('ITEM', ITEM_CODE)")
	private String itemName; 
}
```

- 네이티브 문법만 사용 가능 
- 함수 사용 가능 
- 간단한 수식 사용 가능 (예: salaries * 10)
- `@Basic(fetch = FetchType.LAZY)` 설정 가능

### 결론
- 자주 사용하는 가공값 엔터티에 구현하여 편하게 사용 가능
- 쿼리를 실행하는 것임으로 이런 컬럼이 너무 많아지면 좋지 않을 것


