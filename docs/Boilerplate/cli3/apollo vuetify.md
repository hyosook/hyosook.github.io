

## apollo 패키지 추가 



* 개발 환경 컨테이너로 들어 갑니다.

~~~
$ ./run-bash.sh
~~~
* apollo 를 추가 

~~~
$ cd home-main
$ vue add apollo
~~~

패키지 인스톨이 끝나면 apolo 관련된 선택 사항에 대한 질문이 나옵니다. 

~~~
  ? Add example code (y/N) Y
~~~

에제 코드를 추가 할 것인가를 묻습니다. 
앞으로의 학습을 위해서 Y 를 선택합니다. 

~~~
  ? Add a GraphQL API Server? (y/N) Y
~~~

테스트용 GraphQL API 서버를 추가할 것인가를 묻습니다. 
개발을 편하게 하기 위해서 Y 를 선택합니다. 

~~~
  ? Enable automatic mocking? (y/N) Y 
~~~

자동 목킹을 활성화 할 것인가를 묻습니다. Y 를 선택합니다. 

~~~
  ? Add Apollo Engine? (y/N) N
~~~

아폴로 엔진을 사용할 것인가를 묻습니다. 
이것은 apollo 개발 회사가 제공하는 플랫폼을 사용할 것인가를 묻는데 아니오인 N 를 선택합니다. 

* 설치 과정이 진행이 끝나면 exit 명령을 수행하여 컨테이너를 종료 합니다. 

~~~
$ exit
~~~



* run-dev-apollo.sh 스크립트를 다음과 같이 작성

~~~
#!/bin/bash
echo -e "\\033]2;start home main develop apollo sever\\007"
docker-compose run --name start-home-main-dev-apollo-server \
  --rm \
  -u $(id -u ${USER}):$(id -g ${USER}) \
  --workdir /apps/home-main/ \
  start-home-main-ds \
  yarn run apollo
~~~

* 개발용 apollo 서버 동작

~~~ bash
$ ./run-dev-apollo.sh

yarn run v1.9.4
$ vue-cli-service apollo:watch
$ vue-cli-service apollo:run --delay
Using default PubSub implementation for subscriptions.
You should provide a different implementation in production (for example with Redis) by exporting it in 'apollo-server/pubsub.js'.
✔️  Automatic mocking is enabled
✔️  GraphQL Server is running on http://localhost:4000/graphql
✔️  Type rs to restart the server
~~~



## vuetify 패키지 추가 

vuetify 는 구글 매터리얼 디자인을 구현해 놓은 스타일 프레임워크 패키지 입니다. 
유명한 부트스트랩 같은 겁니다. 

이 패키지는 vue 명령으로 설치하지 않고 yarn 으로 설치 합니다. 
그래서 만들어진 소스를 살짝 고쳐야 합니다. 

* 개발 환경 실행

~~~
$ ./run-bash.sh
~~~

*  vuetify 모듈을 추가 합니다. 

~~~
$ cd home-main
$ yarn add -D vuetify
   :
[4/4] Building fresh packages...
success Saved lockfile.
success Saved 1 new dependency.
info Direct dependencies
└─ vuetify@1.4.1
info All dependencies
└─ vuetify@1.4.1
Done in 20.60s.
~~~

* 매터리얼 디자인 아이콘 추가

~~~shell
$ yarn add -D material-design-icons-iconfont
   :
[4/4] Building fresh packages...
success Saved lockfile.
warning Your current version of Yarn is out of date. The latest version is "1.13.0", while you're on "1.9.4".
info To upgrade, run the following command:
$ curl --compressed -o- -L https://yarnpkg.com/install.sh | bash
success Saved 1 new dependency.
info Direct dependencies
└─ material-design-icons-iconfont@4.0.3
info All depend
~~~

```shell
$ exit 
```



* 소스 수정 

* main.js 의 import 선언된 끝 부분에 다음을 추가 합니다. 

> apps/home-main/src/main.js

~~~
import Vuetify from 'vuetify'
import 'vuetify/dist/vuetify.min.css'
import 'material-design-icons-iconfont/dist/material-design-icons.css'

Vue.use(Vuetify)
~~~



* vuetify 는 폰트로 구글의 Roboto 폰트를 사용합니다.
  Roboto 는 구글 사이트에서 제공하고 이 폰트 링크를 포함 시켜야 합니다.
  index.html 파일의 <head> 태그 사이에 다음과 같이 추가 해야 합니다.

~~~ html
<link href='https://fonts.googleapis.com/css?family=Roboto:300,400,500,700|Material+Icons' rel="stylesheet">
~~~

> apps/home-main/public/index.html

~~~ HTML
    :
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width,initial-scale=1.0">
    <link rel="icon" href="<%= BASE_URL %>favicon.ico">
    <link href='https://fonts.googleapis.com/css?family=Roboto:300,400,500,700|Material+Icons' rel="stylesheet">
    <title>home-main</title>
  </head>
   :
~~~

* 스크립트를 실행하여 이상이 없는가를 검증

~~~
$ ./run-lint.sh
$ ./run-unit.sh
$ ./run-e2e.sh 
~~~



