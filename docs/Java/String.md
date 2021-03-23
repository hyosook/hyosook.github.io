#  분해



## toCharArray

> `char` 배열로 반환하는 String 클래스의 메소드 



## charAt()

> 인자의 위치에 있는 char 값을 반환하는 클래스의 메소드 



## substring()

> index 기준으로 문자열 잘라서, String return 
>
> 인덱스를 이용하여 문자열부분만 자를때

```java
public String substring(int beginIndex)
public String substring(int beginIndex, int endIndex)
```



## split()

> 정규표현식을 기준으로 문자열을 잘라서 배열에 넣어줌
>
> 반복되는 구획문자를 기준으로 문자열을 추출

```java
public String[] split(String regex)
public String[] split(String regex, int limit)
```





# 붙이기

### String  / StringBuffer /  StringBuilder

|              | String               | StringBuffer  | StringBuilder |
| ------------ | -------------------- | ------------- | ------------- |
| Storage Area | Constant String Pool | Heap          | Heap          |
| Modifiable   | No (immutable)       | Yes (mutable) | Yes (mutable) |
| Thread Safe  | Yes                  | Yes           | No            |

### String

* java의 String은 클래스 
* String은 참조형
* 불변

```java
String str = "abc";
```

```java
char data[] = {'a','b','c'}; 
String str = new String(data);

```

### StringBuffer

```java
StringBuffer sb =new StringBuffer();

sb.append('a'); 
//큰따옴표("")는 String을 의미하며 character와 같은 경우
    // 다음과 같이 작은 따옴표('')를 이용하여 character형태로
    // 추가시키는 것이 더욱 효율적이다.

sb.append("adb");
```



## +

## concat()

> new String()으로 새로 만든다



## append()