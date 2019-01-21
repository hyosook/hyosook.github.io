# GIT Issues 

> 새로운 추가될 기능, 개선 해야할 기능, 버그 등등 모든것이 이슈라고 볼 수 있습니다. 
>
> 모든 활동 내역에 대해서 이슈를 등록하고 그 이슈기반으로 작업을 진행하게 됩니다.



## Issues  등록

> 각각의 항목을 등록하면 git < Issues 메뉴의 `Issues `,`Board`,`labels`,`Milestones` 에서 확인 가능

* 해당 프로젝트 git < Issues<  `new Issue`

* Description 

  * `md` 문법으로 작성 

    * ex)  한개의 Issue에 리스트가 여러건인 경우

      ```markdown
      - [ ] 회원 가입
      - [ ] 회원 탈퇴
      - [ ] 회원 정보 수정
      - [ ] 분실 정보 찾기
      ```

      * 체크 하게되면 아래처럼 확인 가능

        ```markdown
        김효숙 @khs 6 minutes ago
        Marked the task 회원 가입 as complete
        ```

* Assignee 

  * 해당 작업의 담당자

* Due date

* Milestone

  * 이번 출시 버전이 1.0.0 일 경우 해당 버전이든 이슈(작업) 기능 강화, 새 기능추가, 버그 기타 등등 모든 이슈를 Version 1.0.0 Milestone이라는 항목에 추가하면 , Version 1.0.0에 대한 전체적인 상황을 한눈에 볼 수가 있는 장점이 있습니다.

* Labels

  * 해당 작업의 성격
  * ex) 회원 , 공통모듈 ... 



## 

## Issues와  코드 연동

### mentioned in commit

* `2`번 이슈에 해당코드 commit 

  ```bash
  git ciam "refs #2 회원가입"
  ```

  * gitlab 2번이슈에서 아래처럼 확인 가능

    ```bash
    김효숙 @khs 5 minutes ago
    mentioned in commit b022701f
    ```


## Issues  close

* `1`번이슈 해결시 커밋과 함께 close 하기 

  ```bash
  git  ciam “close #1 메일모듈완료” 
  ```