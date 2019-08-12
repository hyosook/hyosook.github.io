---
title : Entity와 Value타입
---



# Entity와 Value타입 

도메인 모델의 엔터티는 DB 테이블의 엔터티와 달리 **도메인 기능**을 포함한다. 

### 불변타입으로 구현 
도메인 모델 (Entity와 Value) 에 Set 메소드를 넣지 않는다. (불변 타입으로 구현)

단순 데이터를 변경하는 Set 메소드가 아닌 의미가 있는 기능 메소드로 만들어서 사용한다. 

> Set 메소드가 남발되면, 영속성 컨텍스트에서 데이터 변경이 감지되어 의도하지 않은 update SQL이 발생될 수 있다.

set 메소드가 없는 도메인에 값을 세팅하는 방법은 **생성자**이다.

생성할 때 최초로 값을 세팅해주고, 변경이 필요한 시점에 새로 생성해준다. 

##### Example
```java
public class Money {
	private int value;

	public Money add(Money money) {
		return new Money(this.value + money.value);
	}
}
```

### 예외 -  Set 메소드를 가지는 경우
`private`으로 내부에서 데이터를 변경할 목적으로 사용할 수 있다. 