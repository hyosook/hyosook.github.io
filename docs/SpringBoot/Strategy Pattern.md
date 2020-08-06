# Strategy Pattern 

## Motivation

* `if ..else` 해결책의 문제점 
  * 내일의 문제를 대비할수 없음 
  * 지식의 재사용이나 모듈의 재사용이 불가능 
* 소프트웨어 구성을 
* 변화에 무관 부분과, 변화를 능동적으로 수용할수있는 부분으로 
* 나누어 설계 가능 

https://www.hungrydiver.co.kr/bbs/detail/develop?id=47

https://github.com/zbum/spring-strategy-runtime/tree/master/src              



## 적용

> 학교별 다른 조건의 중복체크 적용하기 
>
> 조건은 중복 or 없는거 가능



* supportable.java

```java
public interface Supportable {
    boolean supports(String schoolCode);
}

```

* DuplicateApplService

```java
public interface DuplicateApplService extends Supportable{

    void check(DuplicateDto appl);

}
```

* DuplicateApplLocator

````java
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DuplicateApplLocator {

    @NonNull
    private List<DuplicateApplService> duplicateApplServices;

    List<DuplicateApplService> resolve(String schoolCode) {
        //다중 체크 가능
        return duplicateApplServices.stream()
                .filter(it -> it.supports(schoolCode)).collect(Collectors.toList());
    }


}
````



* DuplicateService.java

```java
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DuplicateService {

    @Value("${constraint.allowDuplicate}")
    private String allowDuplicate;

    @NonNull
    private DuplicateApplLocator duplicateApplLocator;


    public void checkApplication(DuplicateDto duplicateDto) {
        if (!duplicateDto.isAllowRole()) {
            if ("FALSE".equalsIgnoreCase(allowDuplicate)) {
                duplicateApplLocator.resolve(duplicateDto.getSchoolCode()).forEach(duplicateApplService -> duplicateApplService.check(duplicateDto));

            }
        }
    }
}
```

* RegistrationCheckService

```java
/**
 * ' 학교 +입학연도 + 회차 + 국내 +지원완료+주민번호 '  중복체크
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RegistrationCheckService implements DuplicateApplService {

    @NonNull
    private CryptoService cryptoService;

    @NonNull
    private ApplRepository applRepository;

    /**
     * 현재 임시로 연대 세팅
     *
     * @param schoolCode
     * @return
     */
    @Override
    public boolean supports(String schoolCode) {
        return RecruitSchool.SchoolCodeType.YONSEI.getValue().equals(schoolCode);
    }

    /**
     * 국내전형 & 주민번호 기준 체크
     * 지원사항 저장 단계 제외 모든 단계 체크
     *
     * @param duplicateDto
     */
    @Override
    public void check(DuplicateDto duplicateDto) {
        if (!duplicateDto.isApplPartSaveStatus() && duplicateDto.isGeneral()) {
            String encr = duplicateDto.getRegistration().isDataEncr() ? cryptoService.decrypt(duplicateDto.getRegistration().getRegistrationEncr()) : duplicateDto.getRegistration().getRegistrationEncr();
            List<String> candidateList = applRepository.findRegistration(duplicateDto.getSchoolCode(), duplicateDto.getEnterYear(), duplicateDto.getRecruitPartSeq(),
                    duplicateDto.getAdmissionCode(), duplicateDto.getRegistration().getRegistrationBornDate(), Appl.Status.COMPLETE);
            for (String regst : candidateList) {
                if (cryptoService.decrypt(regst).equals(encr)) {
                    //같은값이어도 암호화다를수있음(복호화 비교필요)
                    throw new DuplicateException(MessageUtil.getMessage("DUPLICATE_REGISTRATION_FAIL"));
                }
            }
        }
    }

}

```

* UserIdCheckService

```java
/**
 * ' 학교+입학연도+회차+userId ' 중복체크
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserIdCheckService implements DuplicateApplService {

    @NonNull
    private ApplRepository applRepository;

    /**
     * 해당 서비스 사용학교시 추가
     * 현재 KDI 사용
     *
     * @param schoolCode
     * @return
     */
    @Override
    public boolean supports(String schoolCode) {
        return RecruitSchool.SchoolCodeType.KDI.getValue().equals(schoolCode);
    }

    /**
     * 지원사항저장 단계에서만 체크
     *
     * @param duplicateDto
     */
    @Override
    public void check(DuplicateDto duplicateDto) {
        if (duplicateDto.isApplPartSaveStatus()) {
            if (applRepository.countByPart_SchoolCodeAndPart_EnterYearAndPart_RecruitPartSeqAndPart_ApplPartNoAndApplicant_Id(
                    duplicateDto.getSchoolCode(), duplicateDto.getEnterYear(), duplicateDto.getRecruitPartSeq(), duplicateDto.getApplPartNo(), duplicateDto.getUserNo())
                    > 0) {
                throw new DuplicateException(MessageUtil.getMessage("DUPLICATE_USERID_FAIL"));
            }

        }

    }


}

```

* 사용

```java
 @NonNull
    private DuplicateService duplicateService; 
duplicateService.checkApplication(dto);
```

