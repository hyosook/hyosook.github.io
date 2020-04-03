# 젠킨스 front 배포

## 프로세스

1. Build & Package

   - nodejs 툴 이용
   - Jenkins 서버에 build 파일 생성

2. Deploy

   - S3 bucket에 업로드

     



## Plugin 설치

**`Jenkins 관리 > 플러그인 관리 > 설치 가능`**

#### NodeJs

* 2020.03` V 1.3.4` 설치 



#### S3 publisher

* 2020.03 `V0.11.2` 설치 



## 환경설정

**`Jenkins 관리 > 환경설정 > Amazon S3 profiles`**



## s3 IAM 

![1585197573898](C:\Users\apexsoft\AppData\Roaming\Typora\typora-user-images\1585197573898.png)





**`Jenkins 관리 > Global Tool Configuration`.**

![1585205329321](C:\Users\apexsoft\AppData\Roaming\Typora\typora-user-images\1585205329321.png)





## job 만들기

```bash
pwd
npm -version
npm install -g yarn
yarn -v
cp -rf .env.awstest .env.production
yarn install
yarn build
```



![1585313035128](C:\Users\apexsoft\AppData\Roaming\Typora\typora-user-images\1585313035128.png)

![1585205886697](C:\Users\apexsoft\AppData\Roaming\Typora\typora-user-images\1585205886697.png)