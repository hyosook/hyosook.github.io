## `@Where` 어노테이션
테이블 정의부분에 정의하면 쿼리메소드를 사용할 때 기본으로 where조건으로 들어간다. 

### 사용
```java java
@Entity
@Table(name = "USER")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Where(clause = "STATUS <> 'WITHDRAW'")
public class User extends AbstractBaseEntity {
	..	
}
```
