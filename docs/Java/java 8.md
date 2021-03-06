# java8

## Optional

>  null 처리를 보다 간편하게 하기 위해서 만들어졌다.



### 객체 생성하기

- `Optional.empty()`
  - null을 담고 있는, 한 마디로 비어있는 Optional 객체를 얻어옵니다.
    이 비어있는 객체는 Optional 내부적으로 미리 생성해놓은 싱글턴 인스턴스입니다.

```
Optional<Member> maybeMember = Optional.empty();
```

- `Optional.of(value)`
  - null이 아닌 객체를 담고 있는 Optional 객체를 생성합니다.
  - null이 넘어올 경우, NPE를 던지기 때문에 **주의해서 사용해야 합니다**.

```
Optional<Member> maybeMember = Optional.of(aMember);
```

- `Optional.ofNullable(value)`
  - null인지 아닌지 확신할 수 없는 객체를  담고 있는 Optional 객체를 생성합니다.
    `Optional.empty()`와 `Optional.ofNullable(value)`를 합쳐놓은 메소드라고 생각하시면 됩니다.
    null이 넘어올 경우, NPE를 던지지 않고 `Optional.empty()`와 동일하게 비어 있는 Optional 객체를 얻어옵니다.
    해당 객체가 null인지 아닌지 자신이 없는 상황에서는 이 메소드를 사용하셔야 합니다.



http://multifrontgarden.tistory.com/131



#### Optional 객체의 값 가져오기

* get() : 만약 데이터가 없을 경우에는 null 리턴
* ofElse() : 값이 없을 경우에는 기본값을 지정할 수 있다.
* Supplier<T> 인터페이스를 활용하여 orElseGet()메소드 사용
* Supplier<T> 인터페이스를 활용하여 orElseThrow()메소드 사용 (데이터가 없는 경우 지정한 예외 발생)

```
Optional<String> optVal = Optional.of("abc");

String str1 = optVal.get(); 
// optVal에 저장된 값을 반환. null이면 예외발생 

String str2 = optVal.orElse(""); 
// optVal에 저장된 값이 null일 때는, ""를 반환 

String str3 = optVal.orElseGet(String::new); 
// 람다식 사용가능 () -> new String()

String str4 = optVal.orElseThrow(NullPointerException::new); 
// 널이면 예외발생

```

#### isPresent()

```
if(Optional.ofNullable(str).isPresent()) { 
  System.out.println(str);
}

```

- Optional 객체의 값이 null이면 false, 아니면 true를 반환한다.

#### ifPresent()

```
Optional.ofNullable(str).ifPresent(System.out::println);

```

#### OptionalInt, OptionalLong, OptionalDouble

```
public final class OptionalInt {
  ...
  private final boolean isPresent; // 값이 저장되어 있으면 true
  private final int value; // int타입의 변수
}
```



## Stream

> 연속된 정보를 처리하는데 사용
>
> 

### 스트림의 구조

- 스트림 생성 : 컬렉션 목록을 스트림 객체로 변환
- 중개 연산 : 생성된 스트림 객체를 사용하여 중개 연산 부분에서 처리 (아무런 결과를 리턴하지 못한다), 0개 이상 존재
- 종단 연산 : 중개 연산에서 작업된 내용을 바탕으로 결과를 리턴

Stream() 은 순차적으로 데이터를 처리한다.(index순)



### FIND

```java
List <DataMap> resultList = this.tutorAbsenceManageService.selectAbsentList (tutorAbsenceManageVo);

List<DataMap> result = resultList.stream().filter( obj -> {
     String date = dateFormat.format((Date) obj.get("absenceDt"));
     return Objects.equals(date, dayMap.get("date")) && Objects.equals(dayMap.get ("periodNo"), obj.get("periodNo"));
}).collect(Collectors.toList());

```

### admin을 제외한 전체 다시 가져오기

```java
 List<CommonCode> typeList = commonCodeRepository.findByCodeGroup("PAY_TYPE");

        List<CommonCode> typeResult = typeList.stream().filter(obj -> {
            String type = PayType.ADMIN.codeVal();
            return !Objects.equals(type, obj.getCode());
        }).collect(Collectors.toList());
```

### anyMatch / contains

```java
 if (authentication.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().contains(AuthorityType.ROLE_MASTER.getValue()))) {
            redirectURL = "/master/prod/list";
        }
```

### filter / count

```java
count = count + (int) protoAns.getValues().stream().filter(v -> v.equals(protoItem.getItemNo())).count();
```

```java
  Optional<EvalAns> ansOptional = evalAnsList.stream().filter(eval -> eval.getQst().getQstNo() == (qst.getQstNo())).findFirst();
```

### peek

* // 스트림을 순회하며

```java
 List<HeaderDto> askList = list.stream().filter(dto ->
                    dto.getName().equals("ask")
            ).peek(dto -> dto.setDday(getDday(dto.getReqEndDate()))).collect(Collectors.toList());
```

### mapToInt

```java
datas.stream().mapToInt(Integer::intValue).toArray()
```

```java
 Optional<EvalAns> ansOptional = evalAnsList.stream().filter(eval -> eval.getQst().getQstNo() == (qst.getQstNo())).findFirst();
```



### group by

* 그룹 기준 key  map형태 

````java
List<ApplPartLanguage> list = applPartLanguageRepository.findApplPartLanguageByApplPart(part);


Map<Language.LangType, Map<Language.LangInfoType, List<ApplPartLanguageDto >>> results =list.stream().collect(
                        Collectors.groupingBy(ApplPartLanguage::getType
                                , Collectors.groupingBy(ApplPartLanguage::getInfoType,
                                        Collectors.mapping(ApplPartLanguageDto::from, Collectors.toList())
                                )
                        )
                );
````

* getType >  getInfoType >

```java
## LangCodeNameOut 으로 map keya만들기
Map<LangCodeNameOut,Map<LangCodeNameOut, List<ApplPartLanguageResponse>>> results = list.stream()
                .collect(
                        groupingBy(lang -> new LangCodeNameOut(lang.getType().name(), lang.getType().getKorName(), lang.getType().getEngName()),
                        groupingBy(lang -> new LangCodeNameOut(lang.getInfoType().name(), lang.getInfoType().getKorName(), lang.getInfoType().getEngName()),
                        Collectors.mapping(ApplPartLanguageResponse::from, Collectors.toList()))));




##

package kr.co.apexsoft.gradnet2.user_api.applpart.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public class LangCodeNameOut {
    private String code;
    private String codeKrValue; //프론트  컴포넌트필요 이름 통일
    private String codeEnValue;

    public LangCodeNameOut(String code) {
        this.code = code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof LangCodeNameOut))
            return false;
        LangCodeNameOut langCodeNameOut = (LangCodeNameOut) o;
        return Objects.equals(code, langCodeNameOut.code);

    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

    @Override
    public String toString() {
        return code + ":{" +
                "code='" + code + '\'' +
                ", codeKrValue='" + codeKrValue + '\'' +
                ", codeEnValue='" + codeEnValue + '\'' +
                '}';
    }
}

```



## map

* 추출
* 스트림에 있는 값들을 **특정방식**으로 **변환**하여 **새로운 스트림** 반환 
* 특정 element만 모으기

```java
      List<EvalAns> answers = protoAnsDtos.stream().map(protoAnsDto -> {
            Qst protoQst = qstRepository.findOne(protoAnsDto.getQstNo());
            if (protoQst == null) {
                throw new RuntimeException(String.format("'qstNo: %s is not exist.", protoAnsDto.getQstNo()));
            }

            EvalAns protoAns = evalAnsRepository.findOne(protoAnsDto.getAnsNo());
            if (protoAns == null) {
                protoAns = new EvalAns();
                protoAns.setQst(protoQst);
                protoAns.setEval(persistEval);
            }

            if (!protoAns.getValues().equals(protoAnsDto.getItems())) {
                protoAns.getValues().addAll(protoAnsDto.getItems());
            }

            return protoAns;
        }).collect(Collectors.toList());
```



```java
List<ProtoAnsDto> protoAnswers = eval.getAnsList().stream().map(
                    evalAns -> new ProtoAnsDto(evalAns.getAnsNo(), evalAns.getQst().getQstNo(),
                            evalAns.getValues().size() != 0 && evalAns.getQst().getType().codeVal().equals(QstType.FILEUPLOAD.codeVal())?fileService.getDownLoadLink((String) evalAns.getItems()):""
                            ,evalAns.getValues())
            ).collect(Collectors.toList());
```



```java
 anslist.add(valueList.stream().collect(Collectors.joining(" , ")));
```



* filert 성공하면 ->map 진행 순으로 반복됨

```java
 List<PayResponse> test2 = iamportList.stream()
                .filter(iamport -> !completeList.contains(iamport.getImp_uid()))
                .map(iamport -> modelMapper.convertToCamelCase(iamport, PayResponse.class))
                .collect(Collectors.toList());
```



* filter 결과 있으면 map 없으면 ,null

  ```java
    school.values().filter(code -> code.hasCode(schoolCode)).findFirst()
    .map(school ->         school.getList().map(EnumValue::new).collect(Collectors.toList()))
        .orElse(null);
  ```

  

### 메소드 참조 ::(더블 콜론)

- static 메소드 참조 : `ContaningClass::staticMethodName`

  ```java
  .stream().map(LanguageResponse::from).collect(Collectors.toList())
  ```

- 특정 객체의 인스턴스 메소드 참조 : `contaningObject::instanceMethodName`

  ```java
  .stream().map(LanguageResponse::from).collect(Collectors.toList())
  ```

  

- 특정 유형의 임의의 객체에 대한 인스턴스 메소드 참조 : `ContaningType::methodName`

  ```java
  .stream().map(RecruitSchoolOut::getSchoolCode).toArray(String[]::new);
  ```

  

- 생성자 참조 : ClassName::new

- 메소드 참조

  ```java
  list.stream().map(this::makeLanguageResponse).collect(Collectors.toList());
  ```

  