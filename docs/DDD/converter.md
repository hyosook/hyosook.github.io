---
title : Converter
tag : ["JPA", "DDD"]
---



## @Converter

> 엔티티의 데이터를 변환해서 데이터베이스에 저장 할 수 있다



### 자바의 boolean 사용 하고, 데이터베이스에 `y` `n` 으로 저장하기 

```java
@Converter
public class BooleanToYNConverter implements AttributeConverter<Boolean, Character> {
    @Override
    public Character convertToDatabaseColumn(Boolean attribute) {
        return (attribute != null && attribute) ? 'Y' : 'N';
    }

    @Override
    public Boolean convertToEntityAttribute(Character dbData) {
        return dbData == 'Y';
    }
}
```

* `@Converter ` 어노테이션 사용 
* `AttributeConverter` 인터페이스 구현 
  * `convertToDatabaseColumn` : 엔티티의 데이터를 데이터베이스 칼럼에 저장할 데이터로 변환한다
  * `convertToEntityAttribute` :  데이터베이스에서 조회한 칼럼 데이터를 엔티티의 데이터로 변환 
* 제네릭에 현재타입과 변환할 타입을 지정해야한다 



### 칼럼 레벨에 적용

```java
 @Column(name = "MULTI_YN")
 @Convert(converter = BooleanToYNConverter.class)
 private boolean multi;
```



### 클래스 레벨에 적용 

```java
@Entity
@Convert(converter=BooleanToYNConverter.class , attributeName="multi")
```

* `attributeName` 속성을 사용해서 어떤 필드에 컨버터를 적용할지 명시해야 한다



## 글로벌 설정

```java
@Converter(autoApply=true)
public class BooleanToYNConverter implements AttributeConverter<Boolean, Character> {
    @Override
    public Character convertToDatabaseColumn(Boolean attribute) {
        return (attribute != null && attribute) ? 'Y' : 'N';
    }

    @Override
    public Boolean convertToEntityAttribute(Character dbData) {
        return dbData == 'Y';
    }
}
```

* 모든 Boolean 타입에 컨버터를 적용한다 
* 따로  `@Convert`를 지정하지 않아도 된다 