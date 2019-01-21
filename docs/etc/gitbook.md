## 초기 설정



### - Github Repository 생성

- <https://github.com/>
- New Repository, Repository 이름은 **USERNAME.github.io**
- USERNAME 은 Github의 가입시에 사용자의 **username**을 입력한다
- Public / Private 중 Public 선택
- Create Repository 버튼을 통해 Repository 생성



### - gitbook 설정

* gitbook-cli 모듈을 전역 모듈로 설치

  ```bash
  # 콘솔 / cmd / terminal에서 아래줄을 입력후 엔터!
   $ npm install gitbook-cli -g
  ```

* gitboot init

  ```bash
   $ gitbook init
  ```

  * `README.md`와 `SUMMARY.md`가 생긴다.
    * 깃북은 `SUMMARY.md`파일을 통해 화면 좌측의 내비게이션/목록 부분을 만든다.

* 마크다운 작성용 브랜치와 깃북으로 빌드한 파일이 올라갈 브랜치를 나눈다

  ```bash
  # gitbook init 으로 만들어진 파일들을 커밋
  (master) $ git ciam "init gitbook"
  
  # docs 브런치 생성
  (master) $ git branch docs
  ```

  * docs : 마크다운 파일 작성 branch
  * master: gitbook 빌드 & 빌드된 html 파일 호스팅



### - 파일 만들기 (docs 브런치에서)

- 디렉토리 생성

  - 최상위 `docs` 폴더 생성

  ```bash
  docs / 
     vue / 
        index.md
     DDD /
        index.md
  ```

- `SUMMARY.md`

  - 폴더구조에 맞추어서 작성한다

  ```makefile
  
  
  - [Introduction](README.md)
  
  - [vue](docs/asdf/index.md)
    - [a](docs/asdf/a.md)
  
  - [DDD](docs/qwer/index.md)
    - [a](docs/qwer/a.md)
  
  
  ```



### - 쉘 스크립트 만들기

- 최상위 부분에  `publish_gitbook.sh` 파일을 만든다
  - `read.me`와 같은 위치

```sh

gitbook build

cp -R _book/* .
rm -r _book/

git add .
git commit -m "Update"
git push origin master


```

- _book 디렉토리 내의 파일들이 호스팅 하는데 필요한 파일들이기 때문에, 밖으로 꺼내야한다



### - 쉘 스크립트 실행

`publish_gitbook.sh` 더블 클릭 



----



## 사용하기

### - md 파일 작성

### - SUMMARY.md 수정

### - 쉘 스크립트 실행

