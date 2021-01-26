---
title : elb
---



# elb 배포

## EB 생성하기 

1. 새 환경 생성
   * 웹 서버 환경 

2. 

![](https://imgur.com/WiWhjyv.png)

3. 추가옵션 구성 

> 해당 과정은, 환경 생성 후에도 변경이 가능하다

4.

![](https://imgur.com/23jKdZB.png)

* cloudWatch Logs  > log를 관리 할 수있다. 필요시 수정 선택

* AWS_ACCESS_KEY_ID /  AWS_SECRET_KEY > s3,mail 등에 필요 

* LANG > 한글 깨짐이 해결된다.

* SERVER_PORT 

  *   Elastic Beanstalk은 응용 프로그램이 포트 5000에서 수신 대기한다고 가정합니다
    기본적으로 Spring Boot 응용 프로그램은 포트 8080에서 수신 대기합니다
  * Spring Boot 응용 프로그램이 수신하는 포트를 변경합니다.
  * 이에 대한 수정 방법으로
    Elastic Beanstalk 환경에서 SERVER_PORT 환경 변수를 지정하고 값을 5000으로 설정

* SPRING_PROFILES_ACTIVE >  콤마(,)로 구분하여 복수개 선택 가능

  * RDS를 적용하는 경우: rds 프로파일 추가




## gradle 설정하기

* [다운로드](https://drive.google.com/open?id=1sEkPpUa7DFpAiNzhd2ZoV3QbUqkzp26A) 에서 , /resources/`.ebextensions` 넣기
* [문서 확인후 넣기](https://drive.google.com/open?id=1iUwjzJwp4Fjl0ITdNAkg4JgVxlR9q7Nf)

* 로컬에서 jar 파일을 만들어 본다 
  * `gradlew combineJar`

## s3

> jar 파일을 올릴수 있는 버킷을 만든다
>
> apexsoft-social-innovation-jar-elasticbeanstalk



## Jenkins

### Jenkins 빌드 프로세스

> 1. Build & Package
>    - Gradle build tool 이용
>    - Jenkins 서버에 jar 파일 생성
> 2. Deploy
>    - S3 bucket에 jar 업로드
>    - EB에서 S3의 jar파일을 소스로 하여 배포

### Task 작성

1. Item 생성

* **새로운 Item**을 선택한다.

  기존 item과 중복되지 않는 이름을 입력하고 **Freestyle project** 선택 후 **OK**를 누른다.

  (기존의 item으로부터 설정을 복사할 경우 **Copy from**에서 item 이름을 검색하여 입력한다.)

  ![](http://i.imgur.com/XbPLD1x.png)


2. 소스코드 관리 

![](https://imgur.com/UTWxzba.png)



3. 빌드 유발

![](http://i.imgur.com/yc1NIJY.png)



4. Build

* `Add build step > Invoke Gradle script`를 눌러 추가한다.

  `Use Gradle Wrapper`를 선택한다.

  - Make gradlew executable: check
  - Tasks: clean combinJar
  - Force GRADLE_USER_HOME to use workspace: check

* 



* `Add build step > AWS Elastic Beanstalk`를 눌러 추가한다.

  Credentials는 `~/.aws/credentials`를 이용해 미리 생성해야 한다. (우리는 이미 생성된 것이 있다.)

  - AWS Credentials and Region
    - Credentials: **AKIAIGKMKOHTIHMWAPEA (AWS credential)**
    - AWS Region: **ap-northeast-2** (서울 리전)
    - Number Of Attempts: **30**
  - Application and Environment (EB 정보 입력)
    - Application Name: **social-innovation**
    - Environment Name: **SocialInnovation-env**

* ![](https://imgur.com/SHJ8xrE.png)

* Packaging

  - Root Object: **build/libs/social-innovation-town-api-0.0.1-SNAPSHOT.jar**(기본적으로 jar가 생성되는 경로)

* Uploading

  - S3 Bucket Name: **apexsoft-social-innovation-jar-elasticbeanstalk** (S3 bucket이 생성되어 있어야 함)
  - S3 Key Prefix: **social-innovation-town-api-0.0.1-SNAPSHOT.jar**

* Version and Deployment

  - Version Label Format: \${GIT_COMMIT}-\${BUILD_TAG}

  - Zero downtime: check 체크해제 (체크하면 기존 Environment가 존재한다고 에러가 발생)


### Task 실행

대시보드에서 Task 뒤에 있는 ![Imgur](http://i.imgur.com/eqtjjuu.png) 버튼이나 Task 화면에서 `Build Now`를 실행한다.

빌드 상태는 아래와 같다.

- 빨강: 실패
- 파랑: 성공
- 회색: 취소
- 노랑: 불안정

![Imgur](http://i.imgur.com/eQTCOEG.png)



## Elastic Beanstalk  https 설정하기 

### Route53

1. 가비아에서 구매한 도메인을 입력하고 생성한다 

![](https://imgur.com/3TdMWdn.png)



2.  Type Ns 쓰여있는 value (주소)를 복사한다

![](https://imgur.com/lqZfjH6.png)



3.  도메인을 구입한 사이트 (가비아)에 접속하여 , 해당 도메인의 네임서버에 하나씩 등록한다

![](https://imgur.com/CNaKy71.png)

![https://imgur.com/ZPXwNCd.png](https://imgur.com/ZPXwNCd.png)

### ACM(Amazon Certificate Manager)

> 증명서를 발급 받는다 



1.  반드시 !!  `버지니아 북부` 로 선택한다  >> Cloud Front 때문에

![](https://imgur.com/dq7zhMr.png)

2. 공인 인증서 요청 

![](https://imgur.com/ryUYTFa.png)

3.

![](https://imgur.com/MOrVl2D.png)

4. 메일 소유 인증 요청 

   > 가비아에서 구매하고 계정을 만들었을때의 도메인으로 간다 

![](https://imgur.com/a0lmnjC.png)

5. 

![](https://imgur.com/cklDgNC.png)

6. 발급 완료 확인

![](https://imgur.com/1wT4hnj.png)





### 로드밸런서 

> eb> 로드 밸런서 수정 
>
> ![](https://imgur.com/Cr729kO.png)



1. auto scaling 그룹

   * 로드 밸런싱 수행으로 한다 

     

2. 로드 밸런서 

   ![](https://imgur.com/N5vUHEX.png)



> 인증한 ssl 인증서 선택




### Route53

1. 

![](https://imgur.com/t1WjEuo.png)



2. alias target : 로드밸런서 선택

   [클릭 - 로드밸런서 선택 > 태그 > 이름 확인 가능 ](https://ap-northeast-2.console.aws.amazon.com/ec2/v2/home?region=ap-northeast-2#LoadBalancers:sort=loadBalancerName)

   

![](https://imgur.com/zSPitHG.png)



### 로드밸런서

> ec2 -> 로드 밸런서

1. 삭제 방지하기

   * 설명 -> 속성

   ![](C:\Users\apexsoft\AppData\Roaming\Typora\typora-user-images\1566270506691.png)

2. http -> https 

   * 리스너

![](https://imgur.com/x6UCKHI.png)

![](https://imgur.com/7411nU3.png)

![](https://imgur.com/Q8mMtUE.png)

![](https://imgur.com/uwoiyjk.png)