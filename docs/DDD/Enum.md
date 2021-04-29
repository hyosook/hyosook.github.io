#  Enum

자주 변경되지 않는 코드성 데이터는 시스템코드 테이블을 이용하는 것보다 `ENUM` 으로 구현할 수 있다.

## 사용하기

### 정의

```java
@Column(name = "APPL_STS_CODE")
@Enumerated(EnumType.STRING)
private Status status;

@Getter
    public enum Status implements EnumModel {
        ACTIVE("활성","active"),
        UNACTIVE("비활성","uncative")
        ;

        String value;
				String engValue

        Status(String value,engValue) {
            this.value = value;
						this.engValue= engValue
        }

        @Override
        public String getValue() {
            return value;
        }

        @Override
        public String getEngValue() {
            return engValue;
        }
        @Override
        public String getCode() {
            return name();
        }
    }
```

- EnumType.STRING : enum `이름`을 db에 저장
- EnumType.ORDINAL: enum `순서'`를 db에 저장

## enum 리스트 조회

### 1.

```bash
@Configuration
public class AppConfig {

    @Bean
    public EnumMapper enumMapper() {
        EnumMapper enumMapper = new EnumMapper();

        enumMapper.put("KeyName1", Enumlass.KeyName1.class); # 등록하기
        enumMapper.put("KeyName2", Enumlass.KeyName2.class); # 등록하기

        return enumMapper;
    }
}
```

### 2.  CommCodeController

```bash
method: 'get',
url: commcode/enums/{names}
ajax('GET', '/commcode/enums/KeyName1,KeyName2').then(_res => {
      this.KeyName1= _res.KeyName1
      this.KeyName2= _res.KeyName2
    })
```

### ex) ModelMapper 매핑

> name을 카멜케이스로 작성 매핑가능

```bash
@Setter
@Getter
public class EvaluateResponse {
    
    private Status status;

    private String statusValue;
    private String statusEngValue;
  
}
```

------

## Enum value을 사용하기 위한 세팅

> 개별 개발자는 해당 내용을 생략하도록 한다

```java
public class EnumMapper {
    private Map<String, List<EnumValue>> factory = new HashMap<>();

    private List<EnumValue> toEnumValues(Class<? extends EnumModel> e) {
        return Arrays.stream(e.getEnumConstants())
                .map(EnumValue::new)
                .collect(Collectors.toList());
    }

    public void put(String key, Class<? extends EnumModel> e) {
        factory.put(key, toEnumValues(e));
    }

    public Map<String, List<EnumValue>> getAll() {
        return factory;
    }

    public Map<String, List<EnumValue>> get(String keys) {
        return Arrays.stream(keys.split(","))
                .collect(Collectors.toMap(Function.identity(), key -> factory.get(key)));
    }
}
public interface EnumModel {
    String getValue();
    String getEngValue();
    String getCode();
}
@Getter
public class EnumValue {
    private String value;
		private String engValue;
    private String code;

    public EnumValue(EnumModel enumModel) {
        this.value = enumModel.getValue();
				this.engValue = enumModel.getEngValue();
        this.code = enumModel.getCode();
    }
}
@Configuration
public class AppConfig {

    @Bean
    public EnumMapper enumMapper() {
        EnumMapper enumMapper = new EnumMapper();
        enumMapper.put("KeyName1", Enumlass.KeyName1.class); # 등록하기
        return enumMapper;
    }
}
```