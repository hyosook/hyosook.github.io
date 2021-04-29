엔터티의 데이터를 변환해서 데이터베이스에 저장 할 수 있다

- 편의를 위해서  데이터 변환 저장시
- 밸류타입을 한개 칼럼에 저장할때

## 1. **자바의 boolean 사용 하고, 데이터베이스에 `y` `n` 으로 저장**

```java
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

- `@Converter` 어노테이션 사용

- ```
  AttributeConverter
  ```

   인터페이스 구현

  - `convertToDatabaseColumn` : 엔티티의 데이터를 데이터베이스 칼럼에 저장할 데이터로 변환한다
  - `convertToEntityAttribute` : 데이터베이스에서 조회한 칼럼 데이터를 엔티티의 데이터로 변환

- 제네릭에 현재타입과 변환할 타입을 지정해야한다

### **칼럼 레벨에 적용**

```
 @Column(name = "MULTI_YN")
 @Convert(converter = BooleanToYNConverter.class)
 private boolean multi;
```

### **클래스 레벨에 적용**

```
@Entity
@Convert(converter=BooleanToYNConverter.class , attributeName="multi")
```

- `attributeName` 속성을 사용해서 어떤 필드에 컨버터를 적용할지 명시해야 한다

### **글로벌 설정**

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

- @Converter(autoApply=true)
  - 모든 Boolean 타입에 컨버터를 적용한다
  - 따로 `@Convert`를 지정하지 않아도 된다

## 2. 밸류타입을 한개 컬럼에 저장해야 할 때

### Names

```java
public class Names {
 private List<Object> values = new ArrayList<>();

 public Object getValues() {
        if (this.values.size() == 1) {
            return this.values.get(0);
        }
        return this.values;
    }

   public void setValues(Object values ) {
        if (Object.isArray()) {
            this.values.addAll(values)
        } else {
            this.values=values
        }
    }

}
```

### 엔터티 내부

```java
@Column(name = "ACAD_NAME")
@Convert(converter = NameListConverter .class)
private Names values;
```

### NameListConverter

```java
@Converter
public class NameListConverter implements AttributeConverter<Names, String> {
	@Override
	public String convertToDatabaseColumn(Names attribute) {
		if (attribute == null) return null;
		return attribute.getValues().stream()
				.map(Names::toString)
				.collect(Collectors.joining(","));
	}

	@Override
	public Names convertToEntityAtrribute(String dbData) {
		if (dbData == null) return null;
		return  Arrays.stream(info.split(",")).map(value -> new Names(value)).collect(Colletors.toList());
	}
}
```

- 한 컬럼에 콤마로 구분된 데이터들이 들어간다. ([test@d.com](mailto:test@d.com), [test@t.com](mailto:test@t.com))
- 콤마로 구분된 데이터들을 객체에서 컬렉션으로 변환된다.

## 3. 기타

- Enum 사용시 value로 테이블에 넣는경우
- 테이블에  대문자로 변환해야하는 경우...