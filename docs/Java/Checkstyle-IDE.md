Intellij 코드 품질 개선 플러그인

https://www.notion.so/Intellij-2677422fac1e4d05b90af9825e3aab75



## Checkstyle-IDE

코딩 포매터 규칙

- Plugins에서 설치
- https://github.com/google/styleguide 에서 `intellij-java-google-style.xml` 다운
- Settings > Code Style 에서 Scheme에 GoogleStyle 적용

![https://s3-us-west-2.amazonaws.com/secure.notion-static.com/ee489aa3-5ac6-4162-a942-cd0d97d3881f/Untitled.png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/ee489aa3-5ac6-4162-a942-cd0d97d3881f/Untitled.png)

- Code Style > Java 에서 Tab size, Indent 4로 수정
- 자동 포매터 실행(단축키) : 맞춰서 포맷됨

> 자동 수정 기능 못 찾음

## PMD

미사용 변수, 비어있는 코드 블락, 불필요한 오브젝트 생성 등을 검사

- Plugins 에서 설치
- PMD 룰 정의
  - https://pmd.sourceforge.io/pmd-4.2.6/rules/basic.html
  - 참고하여 Settings > Other Settings 에서 커스텀 RuleSet 등록 가능
- 패키지 마우스 우측 클릭 > Run PMD
  - Rule 전체를 돌릴수도 일부 Rule을 돌릴수도 있다.

![https://s3-us-west-2.amazonaws.com/secure.notion-static.com/3eb95b7f-4412-49e1-b7fa-6142b05ad876/Untitled.png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/3eb95b7f-4412-49e1-b7fa-6142b05ad876/Untitled.png)

- 하단 PMD 작업창에서 자세한 내용 확인 가능

> 자동 수정 기능 못 찾음

## FindBug

잠재적인 에러 타입을 찾아줌

- Plugins 설치
- 지금 쫌 잘 모르겠음 ...