# 프론트 배포

> 프론트 배포는 

기존처럼 s3 버킷을 사용한다 



## 정책 생성

### 프로젝트 CodeCommit 접근 

> 개인 개발자 ComdeCommit 접근 

* `CODE_프로젝트명_ALL` 

  ```BASH
  {
      "Version": "2012-10-17",
      "Statement": [
          {
              "Effect": "Allow",
              "Action": [
                  "codecommit:*"
              ],
              "Resource": [
                  "arn:aws:codecommit:ap-northeast-2:*:porject1_front",
                  "arn:aws:codecommit:ap-northeast-2:*:porject1_api"
              ]
          },
          {
              "Effect": "Allow",
              "Action" : "codecommit:ListRepositories",
              "Resource" :"*"
          }
      ]
  }
  ```



### s3 버킷 (파일업로드 )

> 개인 개발자 파일관련 버킷 접근 

* `S3_프로젝트_ALL`

  ```JSON
  {
      "Version": "2012-10-17",
      "Statement": [
          {
              "Sid": "VisualEditor0",
              "Effect": "Allow",
              "Action": [
                  "s3:ListBucket",
                  "s3:GetBucketLocation"
              ],
              "Resource": [
                  "arn:aws:s3:::apexsoft.프로젝트명.upload",
                  "arn:aws:s3:::apexsoft.프로젝트명.upload.dev",
                  "arn:aws:s3:::apexsoft.프로젝트명.upload.test"
              ]
          },
          {
              "Sid": "VisualEditor1",
              "Effect": "Allow",
              "Action": [
                  "s3:PutObject",
                  "s3:GetObjectAcl",
                  "s3:GetObject",
                  "s3:DeleteObject",
                  "s3:PutObjectAcl"
              ],
              "Resource": [
                  "arn:aws:s3:::apexsoft.프로젝트명.upload/*",
                  "arn:aws:s3:::apexsoft.프로젝트명.upload.dev/*",
                  "arn:aws:s3:::apexsoft.프로젝트명.upload.test/*"
              ]
          },
          {
              "Sid": "VisualEditor2",
              "Effect": "Allow",
              "Action": "s3:ListAllMyBuckets",
              "Resource": "arn:aws:s3:::*"
          }
      ]
  }
  ```

  

### 프론트 배포 버킷 접근 

> 정적 웹 호스틍 사용 s3접근 정책 

* `Front_S3_master_release`

  ```json
  {
      "Version": "2012-10-17",
      "Statement": [
          {
              "Effect": "Allow",
              "Action": [
                  "s3:PutObject",
                  "s3:PutObjectAcl",
                  "s3:GetObject",
                  "s3:GetObjectAcl",
                  "s3:DeleteObject"
              ],
              "Resource": [
                  "arn:aws:s3:::프로젝트1/*",
                  "arn:aws:s3:::프로젝트2/*"
              ]
          }
      ]
  }
  ```

  

### CodeBuild 

> CodeBuild 사용시 

* `Front_master_build`

  ```json
  {
      "Version": "2012-10-17",
      "Statement": [
          {
              "Effect": "Allow",
              "Action": [
                  "codecommit:CancelUploadArchive",
                  "codecommit:GetBranch",
                  "codecommit:GetCommit",
                  "codecommit:GetRepository",
                  "codecommit:GetUploadArchiveStatus",
                  "codecommit:UploadArchive",
                  "codecommit:GitPull"
              ],
              "Resource": "*"
          },
          {
              "Effect": "Allow",
              "Resource": [
                  "arn:aws:codebuild:ap-northeast-2:*:report-group/*",
                  "arn:aws:codebuild:ap-northeast-2:*:build/*:*"
              ],
              "Action": [
                  "codebuild:CreateReportGroup",
                  "codebuild:CreateReport",
                  "codebuild:UpdateReport",
                  "codebuild:BatchPutTestCases",
                  "codebuild:BatchPutCodeCoverages",
                  "codebuild:BatchGetBuilds",
                  "codebuild:StartBuild",
                  "codebuild:BatchGetBuildBatches",
                  "codebuild:StartBuildBatch"
              ]
          },
          {
              "Effect": "Allow",
              "Resource": [
                  "arn:aws:logs:ap-northeast-2:*:log-group:/aws/codebuild/*:*",
                  "arn:aws:logs:ap-northeast-2:*:log-group:*:log-stream:*",
                  "arn:aws:logs:ap-northeast-2:*:log-group:*:log-stream:*/*"
              ],
              "Action": [
                  "logs:CreateLogGroup",
                  "logs:CreateLogStream",
                  "logs:PutLogEvents"
              ]
          },
          {
              "Effect": "Allow",
              "Resource": [
                  "arn:aws:s3:::codepipeline-ap-northeast-2-*"
              ],
              "Action": [
                  "s3:PutObject",
                  "s3:GetObject",
                  "s3:GetObjectVersion",
                  "s3:GetBucketAcl",
                  "s3:GetBucketLocation"
              ]
          }
      ]
  }
  ```

  

## 역활 Role 생성 

> 빌드&  배포 담당자가 해당기능 수행시 적용함 

* `Front_master_build_deploy`

  ![](https://imgur.com/mNFY2ZW.png)

  ![](https://imgur.com/SyYO4qN.png)

 

## 개발자 IAM 설정 

> 개발자 리포지토리 접근 IAM 설정하기 

* 리파지토리 접근 권한 주기 

* s3 브라우저 접근 권한 주기 

* 보안 자격 증명 생성

  ![](https://imgur.com/4wEYFTr.png)

* 서울 리전으로 comdecommit 접근확인 



## 프로젝트 빌드 설정 

* package.json

```json
"build": "vue-cli-service build",
"testbuild" : "vue-cli-service build --mode awstest",
```

### 빌드 모드 설정 

> 현재 환경변수로 설정함 
>
> ![](https://imgur.com/v1owtqb.png)

#### 1. 환경 변수로 설정

* 환경변수 

  * `AWS_BUILD_MODE ` : 빌드 모드 선택
  * `AWS_BUCKET_NAME ` : dist 업로드 버킷

* `buildspec.yml`

  ```yml
  version: 0.1
  phases:
    pre_build:
      commands:
        - echo Installing source yarn dependencies...
        - yarn install
    build:
      commands:
        - echo Build started on `date`
        - $AWS_BUILD_MODE
    post_build:
      commands:
        # copy the contents of /build to S3
        - aws s3 cp ./dist/ s3://$AWS_BUCKET_NAME --recursive --grant read=uri=http://acs.amazonaws.com/groups/global/AllUsers
  ```

  

#### 2. yml 파일로 설정 

* `buildspec.yml`

```json
version: 0.1
phases:
  pre_build:
    commands:
      - echo Installing source yarn dependencies...
      - yarn install
  build:
    commands:
      - echo Build started on `date`
      - yarn build
  post_build:
    commands:
      - aws s3 cp ./dist/ s3://product --recursive --grant read=uri=http://acs.amazonaws.com/groups/global/AllUsers

```

* `testbuild.yml`

```json
version: 0.1
phases:
  pre_build:
    commands:
      - echo Installing source yarn dependencies...
      - yarn install
  build:
    commands:
      - echo Build started on `date`
      - yarn testbuild
  post_build:
    commands:
      - aws s3 cp ./dist/ s3://test --recursive --grant read=uri=http://acs.amazonaws.com/groups/global/AllUsers

```



## codebuild

### 빌드 프로젝트생성

https://docs.aws.amazon.com/ko_kr/codebuild/latest/userguide/build-env-ref-available.html

> 프론트 프로젝트의 경우 nodejs 버전 확인  필수

![](https://imgur.com/Ao8Hsmb.png)



![](https://imgur.com/Jq0SfYg.png)



![](https://imgur.com/KaAE46S.png)



![](https://imgur.com/LcwJhKe.png)



![](https://imgur.com/5oIcqz9.png)

![](https://imgur.com/xpleWCm.png)



![](https://imgur.com/Vl8STWS.png)



## aws Chatbot -  Slack 연동 

### slack

* 연결하려는 `채널` -> 앱추가 -> `aws chatboot`

### chatboot  생성

* 신규 생성의 경우 `권한` > 템플릿을 사용하여 IAM생성 선택
  * 모든 탬플릿 선택한다

### 알림설정 (CodeCommit,CodeBuild,CodeDeploy,CodePipeline)

* `codeCommit` 알림 규칙생성

  ![](https://imgur.com/YmKExpG.png)

  

* `codeBuild` 알림 규칙 생성

  ![](https://imgur.com/BckgsvY.png)

