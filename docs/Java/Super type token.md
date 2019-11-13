Super type token

## Generic

- `List<E>`

```java
List<String> nameList = new ArrayList<>();
nameList.add("hs")
```

- `<E>` : 타입 파라미터 

- 컴파일 타임에 해당 타입이 일치하는지 확인 가능 

  - 안전한다  .  과거에는 아무 객체나 넣을수 있었다 

    ```java
    List nameList = new ArrayList();
    nameList.add(1)
    ```

### 장점

* 컴파일시 타입 체크 

  * 잘못된 타입이 사용될 문제점 컴파일 과정에서 제거한다 

*  타입 변환(castring)을 제거

  - 타입 변환이 많이 생길수록 전체 애플리케이션 성능이 떨어지게 된다.

  

* 클래스, 인터페이스.메소드 정의할때 type을 파라미터로 사용할수 있도록 한다 



### 제너릭 타입

* type을 파라미터로 가지는 `클래스`와 `인터페이스` 

* `클래스` `인터페이스`  이름 뒤에 `<>` 부호가 붙고, `<이곳>`에  **타입파라미터**가 위치한다 

  ```java
  class 클래스이름<T> {...}
  
  interface 인터페이스이름<T> {...} 
  ```

### 제너릭 메소드  <T,R> R method(T t)

```java
public <타입파라미터, ...> 리턴타입 메소드명(매개변수, ...) { ... }
```

#### 타입 파라미터 

- 임의의 대문자 알파벳 한 글자 표현

  ```bash
  E - Element
  K - Key
  N - Number
  T - Type
  V - Value
  ```

- 두개 이상의 멀티 파라미터 가능  `<K,V...>`

  

#### 제한된 타입 파라미터  `<T extends 최상위 타입>`



### 와일드카드 타입(<?>, <? extends ..>, <? super ...>)

- 제네릭타입<?> : **제한 없음**
  - 모든 클래스나 인터페이스 타입이 올 수 있다.
- 제네릭타입<? extends 상위타입> : **상위 클래스 제한**
- 제네릭타입<? super 하위타입> : **하위 클래스 제한**



#### 사용

```java
public class Box<T> {
  private T t;

  public T get() { return t; }

  public void set(T t) { this.t = t }
}

```

* `String ` 경우 

  ```java
  Box<String> box = new Box<String>();
  
  public class Box<String> {
    private String t;
  
    public String get() { return t; }
  
    public void set(String t) { this.t = t }
  }
  ```

  

* `Integer`경우 

  ```java
  Box<Integer> box = new Box<Integer>();
  
  public class Box<Integer> {
    private Integer t;
  
    public Integer get() { return t; }
  
    public void set(Integer t) { this.t = t }
  }
  
  ```

  

## type token

* 타입을 나타내는 토큰
* 클래스 리터럴이 타입토큰으로 사용된다 
* 

- 클래스 리터럴(Class Literal)은 `String.class`, `Integer.class` 등을 말하며, `String.class`의 타입은 `Class<String>`, `Integer.class`의 타입은 `Class<Integer>`다.
- 타입 토큰(Type Token)은 쉽게 말해 타입을 나타내는 토큰이며, **클래스 리터럴이 타입 토큰으로서 사용된다**.
- `myMethod(Class<?> clazz)` 와 같은 메서드는 타입 토큰을 인자로 받는 메서드이며, **method(String.class)로 호출하면, String.class라는 클래스 리터럴을 타입 토큰 파라미터로 myMethod에 전달한다**.

* 타입을 나타내는 token

* Class<String> 
* `String.class`  : 클래스 리터럴 
* `Class<String>` :  타입 토큰 
* 





그리고 클래스에 메타정보를 가지고 있는 java.lang.Class 타입이 있다. 자바에 모든 클래스는 이 Class 타입이 있다. String의 Class를 가져오기 위해서는 아래와 같다.

```java
Class<String> stringClazz = String.class;
```





그럼 java.util.List에 Class 타입을 가져오기 위해서는 어떻게 해야할까? 마찬가지로 아래와 같다.

```java
Class<List> stringClazz = List.class;
```



그런데 java.util.List는 제네릭을 사용할 수 있다. 즉 List<String> 이렇게 선언된 List를 흔히 String List라고 부른다. 그렇다면 String List Type에 java.lang.Class는 어떻게 가져와야 할까?

| 1    | Class<List<String>> listClazz = List<String>.class; | [cs](http://colorscripter.com/info#e) |
| ---- | --------------------------------------------------- | ------------------------------------- |
|      |                                                     |                                       |

이렇게 가져와야 할까? 코드를 직접 ide에 타이핑해보면 알 수 있다. 이건 문법적으로 오류다. 즉, 컴파일 에러인 것이다. 이렇게 사용할 수 없다.



그런데 java.util.List<java.lang.String>.class와 같이 사용하고 싶은 경우가 발생한다.

예를 들면

1.  A 객체를 B 객체로 매핑 (ModelMapper 사용)
2. Spring에 RestTemplate 사용 시



https://blog.woniper.net/319?category=506090





```java
Type type
```

```java
new TypeToken<List<CommentResponse>>(){}.getType()
```



