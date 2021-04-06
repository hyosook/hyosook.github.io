# JPA Enum 사용

* EnumMapper.java

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

```

* EnumModel.java

```java

public interface EnumModel {
    String getValue();
    String getKorName();
    String getEngName();
}

```

* `EnumValue.java`

```java

@Getter
public class EnumValue {
    private String value;
    private String korName;
    private String engName;

    public EnumValue(EnumModel enumModel) {
        this.value = enumModel.getValue();
        this.korName = enumModel.getKorName();
        this.engName = enumModel.getEngName();
    }
}

```

* AppConfig.java

```java
@Configuration
public class AppConfig {

    @Bean
    public EnumMapper enumMapper() {
        EnumMapper enumMapper = new EnumMapper();
        enumMapper.put("PayType", PayEnums.PayType.class);
        return enumMapper;
    }
}
```

* 사용

```java
 @Getter
    public enum PayType implements EnumModel {
        CARD("신용카드", "Credit Card", "card"),
        VBANK("가상계좌", "Virtual Bank", "vbank"),
        REALTIME_TRANSFER("실시간계좌이체", "Real-time Transfer", "trans"),
        WIRE_TRANSFER("무통장입금", "Wire Transfer", "wire"),
        PAYPAL("페이팔", "Paypal", "paypal")
        ;

        String korName;
        String engName;
        String input;

        PayType(String korName, String engName, String input) {
            this.korName = korName;
            this.engName = engName;
            this.input = input;
        }

        @Override
        public String getValue() {
            return name();
        }

        public static PayType setValue(String pgInput, String input) {
            if(pgInput.equals(PG.PAYPAL.input)) return PayType.PAYPAL;
            return Arrays.stream(PayType.values())
                    .filter(payType -> payType.hasPayTypeCode(input))
                    .findAny()
                    .orElse(null);
        }

        public boolean hasPayTypeCode(String input) {
            return this.input.equals(input);
        }
    }
```



```java
    @NonNull
    private EnumMapper enumMapper;

 /**
     * enum 리스트 조회
     * @param names
     * @return
     */
    @GetMapping("/enums/{names}")
    public ResponseEntity<Map<String, List<EnumValue>>> findByEnums(@PathVariable String names) {
        return ResponseEntity.ok(enumMapper.get(names));
    }
```





- 연대 월 화 

  