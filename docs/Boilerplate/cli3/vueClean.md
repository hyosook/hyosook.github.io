

## 간단한 소스로 만들기



### 개발 서버 실행

~~~ bash
$ cd ~/start-study/docker
$ ./run-dev.sh 
~~~

브라우저 접속은 다음 URL 을 입력 합니다. 

> localhost:80080/



### VUE 프레임 워크 흐름과 소스

여러분이 처음 소스를 살펴 볼 때 다음 순서대로 소스를 살펴 보면 좋습니다. 
이 순서는 components 를 제외 하면 VUE 의 동작 순서이기도 합니다. 

1. public/index.html
2. src/main.js
3. src/App.vue
4. src/router.js
5. src/view/
6. src/components/

7. tests/unit/
8. tests/e2e/

청소도 이 순서대로 합니다. 

### public/index.html 청소

> [public/index.html](https://github.com/kcert2018/start-vue-build-up-guide/blob/master/apps/z020-home-main-clean/public/index.html)
~~~ javascript
<!DOCTYPE html>
<html lang="en">
  <head>
      :
    <title>hyosook</title>
~~~

 index.html 을 보면 다음과 같은 문구가 있습니다 .

~~~ javascript
<!-- built files will be auto injected -->
~~~

이 주석은 VUE 컴파일러가 이 부분에 자동으로 소스들을 추가 한다는 의미 입니다.
즉 이 부분 이하는 건들지 않고 위 문구도 제거하시지 마세요.

VUE 개발시에 index.html 은 특별한 경우가 아니면 건들지 않게 됩니다. 

### src/main.js 청소

main.js 는 VUE 가 시작되는 곳입니다. 

청소 대상은 graphql apollo 관련 부분입니다. 
따라하기 단계상 아직은 서버와 연결될 필요가 없습니다.
GraphQL 서버 연결 처리 부분을 주석 처리합니다.

다음과 같이 import { createProvider } from './vue-apollo' 와 apolloProvider: createProvider(), 을 주석 처리 합니다. 

> [src/main.js](https://github.com/kcert2018/start-vue-build-up-guide/blob/master/apps/z020-home-main-clean/src/main.js)
~~~ javascript
import store from './store'
// import { createProvider } from './vue-apollo'
    :
new Vue({
    :
  // apolloProvider: createProvider(),
    :
}).$mount('#app')
~~~



### src/App.vue 청소

App.vue은 웹 페이지가 가장 처음 표시되는 내용을 다룹니다. 

보통은 전체 화면 레이아웃을 여기서 정의합니다. 
초보분들은 레이아웃을 어떻게 정의 하는지는 아직 크게 신경 쓸 필요도 없습니다.
청소 단계에서는 초보분들이 자주 참조할 형태의 참고 소스를 만들 겁니다. 

묻지도 따지지도 말고 다음과 같이 App.vue 를 만드세요

> [src/App.vue](https://github.com/kcert2018/start-vue-build-up-guide/blob/master/apps/z020-home-main-clean/src/App.vue)
~~~ javascript
<template>
  <div id="app">
    <h1>First!</h1>
  </div>
</template>
import { mapGetters, mapMutations, mapActions } from 'vuex'

export default {
  name: 'App',

  data () {
    return {
    }
  },

  computed: {
    ...mapGetters({
    }),

    sample: {
      get () { return '' },
      set (newValue) {}
    }
  },

  components: {
  },

  methods: {
    ...mapMutations({
    }),
    ...mapActions({
    })
  },

  created () { console.log('CALL created()') },
  mounted () { console.log('CALL mounted()') },
  activated () { console.log('CALL activated()') },
  deactivated () { console.log('CALL deactivated()') },
  destroyed () { console.log('CALL destroyed()') }
}
<style>
</style>
<style scoped>
</style>
~~~



### src/router.js 청소

router.js 는 각 웹 뷰 페이지들을 분리하고 관리하는데 중심이 되는 소스


> [src/router.js](https://github.com/kcert2018/start-vue-build-up-guide/blob/master/apps/z020-home-main-clean/src/router.js)
~~~ javascript
import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

export default new Router({
  // mode: 'history',
  base: process.env.BASE_URL,
  routes: [
  ]
})
~~~

주석 처리하고 있는 "mode: 'history'," 는 여러분의 취향에 달렸습니다.

마이크로 서비스 개발 방식으로 각 기능별로 별도의 앱을 만들 경우,
위와 같이 주석 처리 하시면 각 앱의 특정 뷰에 접근하실 때 URL에 해쉬태그를 이용할 수 있습니다.

쿠버네티스나 웹 페이지 제공 서버로 아파치나 NGINX 를 사용하실 계획이면,
위와 같이 주석 처리 하여 히스트로 모드를 사용하지 않는 것이 좋습니다. 

앞으로 진행하면서 배포 단계에서 설명이 될 것이므로 지금은 뭥미? 하셔도 괜찮습니다. 무시하세요  ^^;

### src/view/ 청소

src/view/ 디렉토리는 실제 UI 를 만들고 관리하는 뷰 소스를 모아 놓는 곳입니다. 

현재는 청소 중이므로 이 안에 있는 파일들을 모두 제거 합니다. 

### src/components/ 청소

src/components/ 디렉토리는 UI가 공통적으로 쓰고 어플리케이션 의존적이지 않은 공통 컴포넌트를 모아 놓는 곳입니다. 

현재는 청소 중이므로 이 안에 있는 파일들을 모두 제거 합니다. 

### tests/unit/ 청소

소스를 변경했으므로 관련된 단위 테스트도 청소해 줄 필요가 있습니다. 
심플하게 다음 파일을 제거하세요

> tests/unit/example.spec.js

### tests/e2e/ 청소

e2e 는 최소한 하나의 테스트 파일이 있어야 합니다. 기존에 있던 tests/e2e/specs/test.js 파일을 다음과 같이 수정해 주세요

> [tests/e2e/specs/test.js](https://github.com/kcert2018/start-vue-build-up-guide/blob/master/apps/z020-home-main-clean/tests/e2e/specs/test.js)
~~~ javascript
// https://docs.cypress.io/api/introduction/api.html

describe('Sample Test', () => {
  it('Nothing...', () => {
  })
})
~~~



### 항상 습관처럼 실행 하자!



~~~
$ ./run-lint.sh
$ ./run-unit.sh
$ ./run-e2e.sh 
~~~

