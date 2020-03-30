# aws api 젠킨스 세팅

## jenkin 빌드 프로세스 

1. Build & Package
   - Gradle build tool 이용
   - Jenkins 서버에 jar 파일 생성
2. Deploy
   - S3 bucket에 jar 업로드
   - EB에서 S3의 jar파일을 소스로 하여 배포



## Task 작성

### 1. item 생성 

* 새로운 Item을 선택한다.
  * 기존 item과 중복되지 않는 이름을 입력하고 **Freestyle project** 선택 후 **OK**를 누른다.
  * (기존의 item으로부터 설정을 복사할 경우 **Copy from**에서 item 이름을 검색하여 입력한다.)

### 2. General

![](https://imgur.com/oPbyLdV.png)

### 3. 소스코드 관리

![](https://imgur.com/LvoHUtD.png)

### 4. 빌드 유발 / 빌드환경

![](https://imgur.com/5q8FpOc.png)

### 5. Build

![](https://imgur.com/qyp9bIO.png)

>  `Add build step > Invoke Gradle script`를 눌러 추가한다.
>
> 해당 프로젝트는 `build.gradle`에 `userCombinJar`로 이름을 설정하였다

### 7. 빌드 후 조치

![](https://imgur.com/oPbyLdV.png)



![](https://imgur.com/GwKQy6T.png)

> `Add build step > AWS Elastic Beanstalk`를 눌러 추가한다.
>
> * Packaging
>   - Root Object: 기본적으로 jar가 생성되는 경로 이다 
>   - 해당 프로젝트는 /user-api/build/libs/user-api-0.0.1-SNAPSHOT.jar
> * * Uploading
>     - S3 Bucket Name 
>       - jar 파일을 올리는 공간 
>       - 미리 생성 되어있어야한다 
>   * Version and Deployment
>     - Zero downtime: **check 체크해제** (체크하면 기존 Environment가 존재한다고 에러가 발생)