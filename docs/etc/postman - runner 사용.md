
같은 `api`를 여러번 반복해야하는 경우에 Postman의 Runner를 사용할 수 있다. 

ex) 회원가입 유저를 100명 생성

### 1. Collection, folder 생성
반복해야하는 api 작업을 collection과 folder를 생성해서 저장한다. 

### 2. Environments 생성 
Environments를 생성하고, 데이터가 변경되는 부분을 variable로 만든다.

![Image 3](https://user-images.githubusercontent.com/45536409/54904203-35677b00-4f21-11e9-9af3-4c0aa4fd5770.png)

### 3. variable 부분 정의
`{{userId}}` 부분은 반복해서 api를 실행할때 데이터를 바꿔줄 부분이다.  `form-data`, `json`등 넣고싶은 부분에 `{{userId}}`를 넣는다. 
> 여러 부분에 변수를 두고싶으면 variable을 추가해서 사용하면 된다.

### 4. Runner 설정 및 Run
상단 메뉴중 `Runner` 클릭한다. 
![image](https://user-images.githubusercontent.com/45536409/55044008-09144180-507c-11e9-9183-fd2c5ae48632.png)
* Environment : Environments 중에서 선택
* Iterations : 반복 횟수
* Delay : 테스트사이 지연 시간
* Log Response : (추후 확인 필요)
* Data : 외부 데이터를 변수로 설정

