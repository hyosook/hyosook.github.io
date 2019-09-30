# EC2 보안



## 프로젝트 단위로 `키 페어` 적용 

### 키 페어 생성

> 재발급이 불가하다 

1.  [AWS > EC2> 키 페어](https://ap-northeast-2.console.aws.amazon.com/ec2/v2/home?region=ap-northeast-2#KeyPairs:sort=keyName)

2. `키 페어 생성 ` 

### ELB 적용

1. AWS > ELB > 프로젝트 어플리케이션 > 보안 

   ![](https://imgur.com/Ga5vlGJ.png)



## putty ec2 접속

### ppk 생성

1. [링크 접속](https://www.chiark.greenend.org.uk/~sgtatham/putty/latest.html)

2. `putty.exe`  `puttygen.exe` 설치

3. `puttygen.exe` 실행

4.  RSA 선택, Load

   ![](https://imgur.com/fapFShE.png)

5. 다운로드한 키페어 pem 파일 선택

   ![](https://imgur.com/EcQo12g.png)

6. 확인 버튼 클릭 키 생성

7. Save private key 클릭 > 예 클릭 

   ![](https://imgur.com/UJSsb19.png)

8. `프로젝트이름.ppk`  파일 저장 

### putty 접속

1. putty 실행
2. Session 탭 정보 입력 
3. auth로 가서 만들었던 ppk파일 등록 

![](https://imgur.com/StmZEQf.png)

4. Session 탭으로 다시 돌아와 저장 후 open 
5. 알림창 `예` 클릭 
6. `ec2-user`로그인 접속 