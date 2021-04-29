# Router

> 라우팅 처리
layout &  meta title & 권한  처리도 함께 한다

## 호출 순서

1. 네비게이션이 트리거됨
2. `beforeEach` : 전역 네비게이션 가드 (인증.인가)
3. `beforeRouteUpdate` : 재사용된 컴포넌트에서 호출됨
4. `beforeEnter` : 라우터 가드
5. `beforeRouteEnter` : 컴포넌트 가드 (컴포넌트 생 전)
6. `afterEach` : 전역 (title 설정한다)
7. `beforeCreate`: 컴포넌트 생성전 (this 사용x)
8. `created`
9. `mounted`
10. `beforeRouteEnter next()` : 컴포넌트 가드 next 훅
11. ~~
12. `beforeRouteLeave` : 컴포넌트 가드 (해당 컴포넌트에서 다른 라우터로 이동시 호출)

### 전역가드 `beforeEach`

- 애플리케이션 전역에서 동작
- beforeEach 를 호출하고 나면, **모든 라우팅이 대기상태**가 된다
- 화면이 전환 되지 않는다
- 해당 url로 라우팅 하기 위해서는 반드시 **next()를 호출**해야한다
- next()가 호출되기 전까지는, 화면이 전환되지 않는다
- 인증.인가 >> 킨더가든 처리 적용한다

### 라우터 가드 `beforeEnter`

- 특정 라우팅에 대해서 가드를 설정
- `/`에서 로그인 여부에 따른 분기 처리한다
    - 로그인된 상태만 `home-main` 가능
    - 킨더가든 처리에서 모두 `home-main`으로 보내고 , 여기서 로그인 여부에 따른 처리한다

```
const router = new VueRouter({
  routes: [
    {
      path: '/',
      name : 'home-main'
      components:  loadViewHeaderFooter('home-main'),
      beforeEnter: (to, from, next) => {
        if (store.getters['users/isUser']) {
	        next()
	    } else {
		    next({name:'user-login`})
	    }
      }
    }
  ]
})

```

### 컴포넌트 가드 `beforeRouteEnter`

- 라우터로 지정된 특정 컴포넌트에 가드를 설정한다
- 컴포넌트 단위로, 스크립트에 작성한다

```
const admin= {
  template: `...`,
  beforeRouteEnter (to, from, next) {
    console.log('컴포넌트 아직 생성되지 않은 시점 | 화면에 표시되기 전 수행될 로직')
    next(vm => {
      console.log('인스턴스, created,mounted 다음')
      vm.logout()
    })
  },
  beforeRouteUpdate (to, from, next) {
   // 화면에 표시된 컴포넌트가 변경될 때 수행될 로직
    // `this` 컴포넌트 인스턴스에 접근 할 수 있습니다.
  },
 beforeRouteLeave (to, from, next) {
 //이 컴포넌트를 렌더링하는 라우트가 이전으로 네비게이션 될 때 호출됩니다.
 // 컴포넌트를 화면에 표시한 url 값이 변경되기 직전의 로직
    // `this` 컴포넌트 인스턴스에 접근 할 수 있습니다.
  }
}

```

---

## 폴더 구조

### `index.js`

- 전역처리 관련 처리를 한다
    - history mode 설정

    ```jsx
    let router = new Router({
      mode: 'history',
      scrollBehavior: to => {
        if (to.hash) {
          return {
            selector: to.hash
          }
        } else {
          return {
            x: 0,
            y: 0
          }
        }
      },
      routes
    })
    ```

    - title 설정

    ```jsx
    // title 설정하기(전체)
    router.afterEach((to, from, next) => {
      document.title = i18n.t(to.meta.title)
    })

    ```

    - 인가 설정

        [kindergarten](https://www.notion.so/kindergarten-fd6ef64e02734777ab6e328d0b6ce9b0)

### `routes.js`

- 라우팅대상 정의 한다
- 없는 페이지 적용

    ```jsx
    없는 페이지를 접근할 때의 라우터 처리
    정의되어 있지 않은 모든 url에 대해서 반응하기 위한 정의

    routes.push({
      path: '*',
      redirect: {
        name: 'home'
      }
    })
    ```

[Layout ](https://www.notion.so/Layout-c0c4bebea9a04015b5263cf9a4145810)

[인가처리 방법(킨더가든)](Router%20808cd8f9fbfc4a2b898d57cb3742bd74/%E1%84%8B%E1%85%B5%E1%86%AB%E1%84%80%E1%85%A1%E1%84%8E%E1%85%A5%E1%84%85%E1%85%B5%20%E1%84%87%E1%85%A1%E1%86%BC%E1%84%87%E1%85%A5%E1%86%B8(%E1%84%8F%E1%85%B5%E1%86%AB%E1%84%83%E1%85%A5%E1%84%80%E1%85%A1%E1%84%83%E1%85%B3%E1%86%AB)%2048340d23fdfa482b955c5a340729f9a7.md)

## 사용

### 동적 세그먼트 사용

- 새로고침 시에 사용 될수 있다.
- pram 으로 전달은 하되, 세그먼트 사용하지 않으면(url 상에 노출x) , 새로고침 시에 처리를 해줘야한다 (cookie나 로컬스토리지

### 동일한 컴포넌트 params 변경 사항 반응

- 동일한 컴포넌트의 인스턴스가 재사용 된다
    - 컴포넌트의 라이프 사이클 훅이 포출되지 않는다

### 1. `$route` 객체 확인

```
 watch: {
    '$route' (to, from) {
      // 경로 변경에 반응하여...
    }
  }

```

### 2.`beforeRouteUpdate`

```
 beforeRouteUpdate (to, from, next) {
   // 재호출 필요한 부분 호출
    next()
  }

```