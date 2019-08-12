# AWS IAM



##  권한 정책

>현재 사용하고 있는 AWS 서비스는 ,   `RDS ` `S3` `EC2` `CloudFront` `Route53` `s3` `SES`  이다



#### 그룹

- 프로젝트 단위로 생성한다 
- 공통 권한을 기술한다
  - `CloudFront` `Route53`  `SES`   : FULL ACCESS 



#### 사용자

* 프로젝트_Admin

  > 배포시에 사용된다 
  >
  > 콘솔 접근을 할수없다
  >
  > 프로젝트 관리자만  `id` & `key`값을 알도록 한다 
  * 해당 프로젝트 그룹 의 공통 권한 상속받는다

  * `s3` : 해당 프로젝트 버킷 접근 (운영용 버킷 )

  * `RDS`  & `EC2` :  FULL ACCESS 

    * 추후 세부적인 관리 포인트가 생기면, RDS 와 EC2 또한 프로젝트 별 관리하도록 한다

    

* 프로젝트_release

  > 배포용 권한 (배포 담당자만 알고있는다 )

  * 해당 프로젝트 그룹 의 공통 권한 상속받는다
  * `s3` : 해당 프로젝트 버킷 접근 (배포 )

  

* 프로젝트_developer

  > 각 개발자 컴퓨터에  `id` & `key`값 저장된다
  >
  > 각 프로젝트 코드에  `id` & `key`값 노출된다 
  * 해당 프로젝트 그룹 의 공통 권한 상속받는다 

  * `s3` : 해당 프로젝트 버킷 접근 (운영, 테스트, 개발용 )

    * 만약, 개발자 단에서 `RDS`에 접근하려면,  관리자 문의 한다

      

* 개인 개발자 _developer

  > [콘솔접근](<https://797540350750.signin.aws.amazon.com/console>)이 가능하다  
  >
  > 개발팀 개발자 별로 생성된다 

  * s3 버킷 별 생성된 정책을 할당한다
    - 다중 할당 가능 

  



## 정책 생성 _S3 

> 현재는 `s3 버킷`에  접근할수 있는 정책만 만들지만, 추후 여러가지로 늘어날수 있다
>
> 프로젝트 S3별 정책을 만들어서, 여러 버킷 (개발,테스트,운영) 을 하나의 정책으로 묶을 수도 있다.



* IAM > 정책 > 정책 생성 

* 1단계 

  * `s3:ListAllMyBuckets` : 모든 버킷 나열
  * `s3:ListBucket`, `s3:GetBucketLocation` : `apexsoft.web.upload` 항목, 폴더 객체 및 나열
  * `apexsoft.web.upload` 이하 , 업로드 ,다운로드, 삭제, 권한 변경 가능 

  https://docs.aws.amazon.com/ko_kr/AmazonS3/latest/dev/example-policies-s3.html

  ```bash
  {
     "Version":"2012-10-17",
     "Statement":[
        {
           "Effect":"Allow",
           "Action":[
              "s3:ListAllMyBuckets"
           ],
           "Resource":"arn:aws:s3:::*"
        },
        {
           "Effect":"Allow",
           "Action":[
              "s3:ListBucket",
              "s3:GetBucketLocation"
           ],
           "Resource":"arn:aws:s3:::apexsoft.web.upload"
        },
        {
           "Effect":"Allow",
           "Action":[
              "s3:PutObject",
              "s3:PutObjectAcl",
              "s3:GetObject",
              "s3:GetObjectAcl",
              "s3:DeleteObject"
           ],
           "Resource":"arn:aws:s3:::apexsoft.web.upload/*"
        }
     ]
  }
  ```

  

* 생성한 정책 이름 

  ![1561439528780](C:\Users\apexsoft\AppData\Roaming\Typora\typora-user-images\1561439528780.png)





## 그룹

> - 프로젝트 단위로 생성한다 
> - 공통 권한을 기술한다 (3개)
>   -    `CloudFront` `Route53`  `SES`   : FULL ACCESS  ( 필요에 따라서 분리 )



![1561438802609](C:\Users\apexsoft\AppData\Roaming\Typora\typora-user-images\1561438802609.png)

![1561950045684](C:\Users\apexsoft\AppData\Roaming\Typora\typora-user-images\1561950045684.png)









## 사용자  - 프로젝트 Admin

> 해당 계정은, 배포에 사용된다. 개발자들에게 노출하지않는다
>
> 현재 , `RDS` & `EC2`  FULL 접근으로 설정하지만, 
>
> 차후 인원과 관리의 필요성이 늘어나면  `RDS , EC2 , SES`   또한  `S3`처럼 관리한다.



### 생성 

* IAM > 사용자 > 사용자 추가 

  ![1561444878150](C:\Users\apexsoft\AppData\Roaming\Typora\typora-user-images\1561444878150.png)

* 2단계  >기존 정책 직접 연결

  * RDS FULL ACCESS , EC2 FULL ACCESS .S3_사용자정책
  * 그룹에 사용자 추가는 해당 프로세스가 끝난뒤 한다

  ![1561445014666](C:\Users\apexsoft\AppData\Roaming\Typora\typora-user-images\1561445014666.png)

  

  

* 5단계

  > 해당 계정은, 콘솔로 로그인 하지 못하게 추가 설정 하지 않는다 
  >
  > 엑세스 KEY 와 ID 는 노출되지 않게 관리한다 

  ![1561445312164](C:\Users\apexsoft\AppData\Roaming\Typora\typora-user-images\1561445312164.png)

* IAM > 사용자 > 그룹에 해당 사용자를 추가한다

  > ![1561445411854](C:\Users\apexsoft\AppData\Roaming\Typora\typora-user-images\1561445411854.png)



###확인

 !![1561950112053](C:\Users\apexsoft\AppData\Roaming\Typora\typora-user-images\1561950112053.png)



##사용자  - 프로젝트 developer

> 개발자들에게 공유되고 , 소스코드에 명시가 되므로 최소화 한다



### 생성

* 2단계  > 그룹에 사용자 추가



### 개발자 사용

* C:users/apexsoft/.aws

  * `credentials`파일  수정

    ```bash
    [default]
    aws_access_key_id = 관리자문의
    aws_secret_access_key = 관리자문의
    
    [유저이름]
    aws_access_key_id = 관리자문의
    aws_secret_access_key = 관리자문의
    
    ```

* `api`  >`application.yml`

  ```bash
  aws:
      credential.profile: 유저이름
      ses:
          region: us-west-2
          sendMail: 해당프로젝트 메일
      s3:
          signingRegion: ap-northeast-2
          endPoint: s3.ap-northeast-2.amazonaws.com
          url: https://s3-ap-northeast-2.amazonaws.com
          bucketName: 버킷
  ```

* `front`  > `.env`

  ```bash
  VUE_APP_AWS_ACCESS_KEY_ID=관리자문의
  VUE_APP_AWS_SECRET_ACCESS_KEY=관리자문의
  ```

  

## 사용자- 프로젝트 배포담당자

> 프론트 배포를 위한 권한 
>
> 그룹에 포함시키고, 배포 폴더만 접근할수있도록 한다 



## 사용자- 개인 개발자 _developer

> 개발 인원 별 콘솔 접근 계정을 만들어서 ,  각 정책을 할당한다



* IAM >  사용자 >  사용자 추가 

  * 개발자닉네임_developer
  * 공통비밀번호 할당 후 각각이 수정하게 한다

  ![1561442209275](C:\Users\apexsoft\AppData\Roaming\Typora\typora-user-images\1561442209275.png)

* 정책 연결

  * `apexsoft.web.upload` 와 `apexsoft.ev.upload` 버킷 접근 가능

  ![1561441352867](C:\Users\apexsoft\AppData\Roaming\Typora\typora-user-images\1561441352867.png)

* 태그

  * 해당 사용자의 mail을 등록한다

  ![1561441401465](C:\Users\apexsoft\AppData\Roaming\Typora\typora-user-images\1561441401465.png)

* 

  ![1561441463837](C:\Users\apexsoft\AppData\Roaming\Typora\typora-user-images\1561441463837.png)

* 로그인 

  ![1561442325235](C:\Users\apexsoft\AppData\Roaming\Typora\typora-user-images\1561442325235.png)

* 비번 변경

  ![1561442355057](C:\Users\apexsoft\AppData\Roaming\Typora\typora-user-images\1561442355057.png)









## IAM 확인

```
aws iam list-users
```
