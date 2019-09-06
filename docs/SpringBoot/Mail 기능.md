# Mail

## 구성

* `/_support/aws/mail/`

| 명                         |                                                              | 기능                                                         |
| -------------------------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| SESmailServiceImpl         | - aws ses를 사용하여 구현<br> - 단체메일의 경우 자동으로 50개씩 끊어서 전송<br>- 받는 메일주소는  `TO`가 아니라 , `BCC`로 처리됨 | -  sendAsync : 비동기 에러시 서버에 로그만 찍힘 <br>- send :`SESMailException` 발생 처리 |
| exception/SesAsyncHandler  | `AsyncHandler` 구현                                          | - 에러 발생시 서버로그 출력 `onError` 프로젝트에 따른 구현 변경 확인 필요 |
| exception/SESMailException | `CustomException` 상속 구현                                  |                                                              |

* `/_support/mail/`

|                                 |                                                      |                                                              |
| ------------------------------- | ---------------------------------------------------- | ------------------------------------------------------------ |
| MailDto                         | 메일전송에 필요한 dto                                | - to : 받는 주소 <br>- subject : 제목 <br>- templateId : classpath:/templates/email-templates/메일 템플릿 파일명 <br> - replacements : html 템플릿의 {{key}} 와 vlaue <br>-  resourceLoader |
| MailService                     | `interface`                                          |                                                              |
| MailTemplateUtil                | `templates/email-templates/` 의 템플릿에 데이터 매핑 |                                                              |
| exception/MailTemplateException | `CustomException` 상속 구현                          |                                                              |
| exception/MailValidException    |                                                      |                                                              |





## 사용방법

* aws ses  사용 위해서  `C:\User\.aws\credentials` 파일 필요 

* 메일 템플릿 만들기 

 *  `templates/email-templates/`에 `create.html` 파일 생성
 파일을 

  *  `{{key}} 형태로 만들기 `

* 주입

    ```java
        @NonNull
        private MailService mailService;
    
        @Autowired
        ResourceLoader resourceLoader;
    ```

    

* 메일dto 만들기

    ```java
    //템플릿에 매핑되는 replacements 생성 
     Map<String, String> replacements = new HashMap<String, String>();
    
            replacements.put("projectName", "프로젝트 이름");
            replacements.put("content", "내용");
    
            MailDto dto = MailDto.builder()
                    .to(mailList)
                    .subject("메일 테스트 제목 ")
                    .templateId("sample.html")
                    .replacements(replacements)
                    .resourceLoader(resourceLoader)
                    .build();
    ```

* mail 형식 검증 (옵션)

  ```java
  ApexAssert.notMailPattern(mailAddr, MessageUtils.getMessage("MAIL_ADDRESS_CHECK_FAIL"), MailValidException.class);
  ```

  * mail 형식 오류가 있으면, 전체 전송이 되지 않음 

    

* 동기 메일 전송

  ```java
     mailService.send(dto);
  ```

  

* 비동기 메일 전송 

  ```java
   mailService.sendAsync(dto);
  ```



* mail 형식 검증  (옵션)

  ```java
  ApexAssert.notMailPattern(mailAddr, MessageUtils.getMessage("MAIL_ADDRESS_CHECK_FAIL"), MailValidException.class);
  ```

