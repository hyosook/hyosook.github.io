# Lombok 어노테이션



## Getter / Setter

* `@Getter`
  * boolean 타입인 경우, `isXxx()`
* ` @Setter`



## 생성자

* `@NoArgsConstructor`

  * 파라미터 없는 기본 생성자

* `@AllArgsConstructor`

  * 모든 필드 값을 파라미터

* `@RequiredArgsConstructor` 

  * `final`이나 `@NonNull`인 필드 값만 파라미터로
  * 초기화 되지 않는 final 필드를 매개 변수로 취하는 생성자를 생성 
  * @Nonnull 필드는 null 체크 실행후  nullpointException 발생

  

## equals, hashCode 자동 생성

- `@EqualsAndHashCode(callSuper = true)`
  - 부모 클래스 필드 값 동일 한지 체크 
- `@EqualsAndHashCode(callSuper = false)`

```java
@EqualsAndHashCode(callSuper = true)
public class Child extends Parent {
  private String childName;
}
```

```java
Child child1 = new Child("자식1이름","부모id변형");

Child child2 = new Child("자식2이름","부모id");

child1.equals(child2);
// callSuper = true 이면 false, callSuper = false 이면 true
```





## ToString 메소드 자동 생성

* `@ToString` 어노테이션만 클래스에 붙여주면 자동으로 생성

*  `exclude` 속성을 사용하면, 특정 필드를 `toString()` 결과에서 제외

  ```ㅓㅁㅍㅁ
  @ToString(exclude = "password")
  public class domain {
    private Long id;
    private String username;
    private String password;
  }
  ```

  

  