---
title : lombok
tags : ["java"]
---



## @Builder

* 필수 인자들을 생성자(또는 정적 팩터리 메서드)에 전달하여 빌더 객체를 만들고, 선택적 인자들을 추가한 뒤, 마지막에 `build()`를 호출하여 Immutable 객체를 만드는 방법.
* Class에 대한 복잡한 Builder API들을 자동으로 생성해 준다. 
* Class 뿐만 아니라, 생성자, 메서드에도 사용할 수 있다. 
* 추가적으로 @Singular Annotation도 제공한다.
* @Singular는 @ Builder Annotation을 사용한 Class/Method/Constructor에서 Collection 객체에만 사용할 수 있다. 
* Collection 변수에 @Singular을 설정하면 setter 함수 대신 두 종류의 Adder 함수와 Clear함수를 생성해 준다. 



* 빌더 패턴을 적용한 객체 생성 메소드/클래스를 만들어준다.
* builderClassName 파라미터로 nested 빌더 클래스의 이름을 (클래스명Builder가 기본), 
* builderMethodName으로 빌더 클래스를 반환하는 static 메소드의 이름을 (builder()가 기본), 
* buildMethodName으로 객체를 반환하는 빌드 메소드의 이름 (build()가 기본)을 설정할 수 있다.
* 필드에 **@Builder.Default** 어노테이션을 붙여 기본 값을 설정할 수 있고,

**@Singular** 어노테이션을 붙여 빈 collection을 자동으로 만들 수 있다. **@Singluar** 어노테이션은 파라미터로 builder에서 값을 추가할 때 사용되는 메소드의 이름을 입력받는다. 또, **@Singular** 어노테이션으로 만들어진 collection은 수정할 수 없다.

* 데이터 일관성, 객체불변성 등을 만족시킨다. 또한 코드 가독성 역시 올라간다.

  ​

```java java
@Builder
@ToString
public static class LombokClass {
  private String name;
  private int value;
  @Builder.Default private int value2 = 999; // 기본 값 설정
  @Singular("addVal") private List<Integer> values;
}
```

```java java
val lombokClass = LombokClass.builder()
                              .name("Hello")
                              .value(500)
                              .addVal(100)
                              .addVal(200)
                              .addVal(300)
                              .build();

System.out.println(lombokClass); // LombokClass(name=Hello, value=500, value2=999, values=[100, 200, 300])


val val0 = lombokClass.values.get(0);
System.out.println(val0); // 100
```

### 자바 빈즈 패턴

자바빈즈 패턴은 코드량이 늘어나는 단점이 존재한다. 

하지만 그것보다 더욱 문제가 되는 것은 객체 일관성이 깨진다.

객체 일관성이 깨진다는 것이란, 한번 객체를 생성할때, 그 객체가 변할 여지가 존재한다는 뜻이다.

즉, set메서드는 언제 어디서든 사용 할 수 있다는 장점이 있지만, 객체의 불변성이 깨진다.

스레드 작업에 큰 단점이 될 수 있을 뿐더러, 컴파일러 오류는 아니지만, 우리가 원하지 않는

결과물이 만들어 질 수 있는것이다.

점층적 생성자패턴과 자바빈즈 패턴의 장점을 섞은것이 빌더패턴이다.

정보들은 자바 빈즈 패턴처럼 받되, 데이터 일관성을 위해 정보들을 다 받은후에 객체생성을 해준다.

```java java
PersonInfo personInfo = new PersonInfo( );
personInfo.setName("Mommoo");         // 이름을 넣는 메서드
personInfo.setAge(12);                       // 나이를 넣는 메서드
personInfo.setPhonNumber(119);          // 전화번호를 넣는 메서드

```



## 사용 에러

```bash console
org.springframework.web.util.NestedServletException:
Request processing failed; 
nested exception is org.springframework.orm.jpa.JpaSystemException:
No default constructor for entity:  : 
kr.co.apexsoft.GCSVC.domain.FileUpLoad;
nested exception is org.hibernate.InstantiationException:
No default constructor for entity:  :
kr.co.apexsoft.GCSVC.domain.FileUpLoad
```

## 해결 

```java java
@AllArgsConstructor
@RequiredArgsConstructor
```



## XXXXArgsConstructor

위의 어노테이션은 생성자를 생성해주는 어노테이션이다. 

첫 번째는 디폴트 생성자를 생성해주는 `@NoArgsConstructor` 

두 번째는 모든 필드의 생성자를 생성해주는 `@AllArgsConstructor` 

마지막으로 필수 생성자를 생성해주는 `@RequiredArgsConstructor` 가 있다.

속성
1. `staticName` : 위에서 `@Data` 어노테이션의 `staticConstructor` 와 동일하다. static한 생성자를 만들어 준다.
2. `access` : 접근제한을 할 수 있다. `PUBLIC`, `MODULE`, `PROTECTED`, `PACKAGE`, `PRIVATE` 등으로 설정가능 하다.
3. `onConstructor` : 생성자에 어노테이션을 작성할 수 있다.
  공통적인 속성으로는 위와 같이 3가지가 존재한다.

```java java
@RequiredArgsConstructor(staticName = "of", onConstructor = @__(@Inject))
public class ConstructorObject {
  private final Long id;
  private final String name;
}

```

만약 위와 같은 어노테이션을 작성할 경우에는 다음과 같은 코드가 나올 것이라고 예상해본다.

```java java
class ConstructorObjectNot {
  private final Long id;
  private final String name;

  @Inject
  private ConstructorObjectNot(Long id, String name) {
    this.id = id;
    this.name = name;
  }
  public static ConstructorObjectNot of(Long id, String name) {
    return new ConstructorObjectNot(id, name);
  }
}

```

`staticName`가 존재해 `access`는 별루 의미가 없어 사용하지 않았다.





## @Data

lombok을 사용한다면 제일 많이 사용하는 어노테이션이다. 이 어노테이션은 다재다능한 기능이다. 사용하는 사람은 알겠지만 getter, setter, toString, hasCode, equals, constructor 등 많은 부분을 자동으로 생성해준다.
각각 부분적으로는 밑에서 설명하도록 하겠다.
`@Data` 어노테이션에는 속성이 한개 있는데 `staticConstructor` 라는 속성이다. 말그대로 static한 생성자? 를 만들어 주는 것이다.

```java java
@Data(staticConstructor = "of")
public class DataObject {
  private final Long id;
  private String name;
}

```

위와 같이 선언한다면 다음과 같이 사용가능하다.

```
DataObject dataObject = DataObject.of(1L);

```

`id` 경우에는 final이라 필수 생성자에 포함되어 있다. 만일 위와 같이 사용한다면 `new`로 생성할 수 없다.

```
DataObject dataObject2 = new DataObject(); // compile error

```

위와 같이 new 를 이용해 생성할 시에는 컴파일 에러가 발생한다.

위의 여러 메서드를 제외하고 한개의 메서드가 더 생성이 되는데 그 메서드는 `canEqual`이라는 메서드이다. 해당 메서드의 역할은 `instanceof`로 타입정도만 체크 한다. 하지만 메서드의 접근제한자는 `protected` 이다. 필자도 사용할 일이 없었다.



## @Getter와 @Setter

어노테이션 이름 그대로 getter와 setter를 생성해준다. 클래스 레벨에도 사용가능하며 필드 레벨에도 사용가능하다.
공통 속성으로는 `value`, `onMethod` 속성이 존재한다. `value`의 경우에는 접근 제한을 할 수 있으며 `onMethod` 메서드의 어노테이션을 작성할 수 있다.

```java java
public class GetSetObject {

  @Getter(value = AccessLevel.PACKAGE, onMethod = @__({@NonNull, @Id}))
  private Long id;
}

```

만약 위와 같은 코드를 작성하였을 경우에는 다음과 같은 코드가 작성 될 것이다.

```java java
class GetSetObjectOnMethod {
  private Long id;

  @Id
  @NonNull
  Long getId() {
    return id;
  }
}

```

물론 @Setter 어노테이션에도 onMethod를 사용할 수 있다.

```java java
@Setter(onMethod = @__({@NotNull}))

```

그리고 @Getter, @Setter 각각이 다른 속성들을 한개씩 가지고 있는데. @Getter인 경우에는 `lazy` 속성이고 @Setter의 경우에는 `onParam` 이라는 속성이다.
@Getter 의 lazy 속성은 속성명 그대로 필드의 값은 지연시킨다는 것이다.

```java java
@Getter(value = AccessLevel.PUBLIC, lazy = true)
private final String name = expensive();

private String expensive() {
  return "wonwoo";
}

```

lazy가 true일때는 무조건 final 필드어야만 한다. lazy 속성이 false 일 경우에는 객체를 생성할 때 `expensive()` 메서드를 호출하지만 속성이 true일 경우에는 getName() 메서드를 호출할 때 `expensive()` 메서드를 호출 한다.

다음은 @Setter의 onParam 속성이다. 이 속성은 파라미터의 어노테이션을 작성할 수 있는 속성이다.

```java java
@Setter(onParam = @__(@NotNull))
private Long id;

```

만약 다음과 같은 코드가 있을 경우에는 아래와 같은 코드가 작성될 것이라고 판단된다.

```java java
class GetSetObjectOnParam {
  private Long id;

  public void setId(@NotNull Long id) {
    this.id = id;
  }
}

```

아주 간단한 코드이다. (물론 만든 사람은 아니겠지만..)

> IDEA에서는 `onParam`과 `onMethod`에 @Column 어노테이션의 속성을 넣으면 잘 동작하지 않는다. onParam 은 파라미터에 적용되니 @Column 자체가 들어 갈 수 없으니 그렇다 쳐도 onMethod는 왜안되는지.. 플러그인 문제인듯 싶다.

## @EqualsAndHashCode 와 @ToString

`@EqualsAndHashCode` 어노테이션은 이름 그대로 hashcode와 equals를 생성해주는 어노테이션이고, `@ToString`도 마찬가지로 `toString()` 메서드를 생성해주는 어노테이션이다.
공통 속성으로는 4가지 있는데 `exclude`, `of`, `callSuper`, `doNotUseGetters`가 존재 한다. exclude는 제외시킬 변수명을 작성하면 되고 of는 포함시킬 변수명을 작성하면 된다. callSuper 속성은 상위 클래스의 호출 여부를 묻는 속성이다. 마지막으로 doNotUseGetters의 속성은 getter 사용여부 인듯 하나 제대로 동작하지는 모르겠다.

```java java
@EqualsAndHashCode(of = "id")
@ToString(exclude = "name")
public class HashCodeAndEqualsObject {
  private Long id;
  private String name;
}

```

만일 위와 같이 작성하였다면 hasCode, equals, toString 모두 id만 존재하게 된다.
각각의 속성으로는 @EqualsAndHashCode 는 `onParam`, @ToString 는 `includeFieldNames` 속성이 존재한다. onParam 은 equals에 작성되며 위의 onParam 속성과 동일하므로 생략한다. includeFieldNames는 toString의 필드 명을 출력할지 하지 않을지의 여부이다. 만일 위의 코드로 `includeFieldNames`을 false로 한다면 다음과 같이 출력 된다.

```
HashCodeAndEqualsObject(null)

```

참고로 `canEqual` 메서드도 `@EqualsAndHashCode` 메서드에 포함되어 있다.

## @val 와 @var

스칼라, 코틀린 이외에 다른 언어들의 키워드와 동일하게 타입추론을 한다.

```java java
public class ValAndVarTests {
  @Test
  public void valVarTest() {
    val arrVal = Arrays.asList(1, 2, 3, 4, 5);
    arrVal = new ArrayList<>(); // compile error

    var arrVar = Arrays.asList(1, 2, 3, 4, 5);
    arrVar = new ArrayList<>();
  }
}

```

`val` 경우에는 `final` 키워드가 생성된다. 그래서 다시 어사인을 할 경우에 컴파일 에러가 발생한다. 마찬가지로 `var`는 final이 존재 하지 않으므로 다시 어사인이 가능하다. 위의 코드를 다시 만들어 보면 다음과 같을 것으로 예상된다.

```java java
final List<Integer> arrVal1 = Arrays.asList(1, 2, 3, 4, 5);
arrVal1 = new ArrayList<>();

List<Integer> arrVar1 = Arrays.asList(1, 2, 3, 4, 5);
arrVar1 = new ArrayList<>();

```

위와 동일한 바이트코드가 나올 것으로 예상해본다.

## @UtilityClass

유틸리티 클래스에 적용하면 되는 어노테이션이다. 만약 이 어노테이션을 작성하면 기본생성자가 `private` 생성되며 만약 리플렉션 혹은 내부에서 생성자를 호출할 경우에는 `UnsupportedOperationException`이 발생한다.

```java java
@UtilityClass
public class UtilityClassObject {
  public static String name() {
    return "wonwoo;";
  }
}


```

만약 위의 코드를 다시 작성해보면 다음과 같다.

```java 
class UtilityClassObjectNot {
  private UtilityClassObjectNot() {
    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
  }
  public static String name() {
    return "wonwoo;";
  }
}

```

