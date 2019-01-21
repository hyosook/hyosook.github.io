---
title : vueRouter
tags: ["vue.js"]
---



#vue-router

- 뷰에서 라우팅 기능을 구현할 수 있도록 지원하는 공식 라이브러리
- SPA(Single Page Application) 사용
  - 페이지를 이동할 때 마다 서버에 웹 페이지를 요청하여 새로 갱신하는것이 아니라
  - 미리 해당 페이지들을 받아놓고 페이지 이동 시에 클라이언트의 라우팅을 이용하여 화면을 갱신하는 패턴을 적용한 어플리케이션
- 라우팅이 경로에 따라 컴포넌트를 바꿔치기해서 렌더링



## 태그 사용

### 라우터 링크

* `<router-link to="url 값">`
* 페이지 이동 태그
* to에 지정한 url로 이동
* `<a>`태그 사용 안하는 이유 
  * 히스토리모드와 해쉬뱅 모드에서는 주소 체계가 달라서 `<a>` 태그를 사용할 경우 모드 변경시 주소값을 일일이 변경해 줘야한다. 하지만 `<router-link>`는 변경할 필요가 없다.
  * `<a>` 태그를 클릭하면 화면을 갱신하는데 `<router-link>`는 이를 차단해준다. 갱신 없이 화면을 이동할 수 있다. 

### 라우터 뷰

* `<router-view>`
* 페이지 표시 태그
* 변경되는 url에 따라 해당 컴포넌트 뿌려줌
* 랜더링 해주는 부분



## 정의

```javascript
const router = new VueRouter({
  mode: 'history',
  routes: [
    { path: '/', component: Home },
    { path: '*', component: NotFound }
  ]
})
```

###mode

* 기본값은 , `해쉬뱅 모드`
* `history`모드 사용 시 ,  브라우져 history 객체의 [pushState() API](https://developer.mozilla.org/ko/docs/Web/API/History_API)를 사용 

### routes

* 경로와 컴포넌트로 이뤄진 컬렉션
* 실제 이 맵을 보고 , 라우터가 경로에 따라 그에 맞는 컴포넌트를 렌더링한다
* 정의한 순서대로 경로를 매칭해서 컴포넌트를 출력
* 정의되지 않은 경로로 들어올결우
  -  `{ path: '*', component: NotFound }` 설정에 의해 NotFound 컴포넌트가 그려질 것이다.

### children

* 중첩된 라우팅이 중첩된 화면을 구성한다(http://blog.jeonghwan.net/2018/04/07/vue-router.html)

* 특정 라우팅의 하위 경로가 변경됨에 따라 하위 컴포넌트로 변경할수 있는 기능

  * 부모 컴포넌트 부분에는 `<router-view></router-view>` 반드시 들어가야 한다 
    * 이부분에 자식이 보여짐

  ```javascript
  export default new VueRouter({
    routes: [
      // 생략 
      
      { path: '/posts',
        component: { template: '<div>Posts <br/><router-view></router-view></div>' },
  
        // 중첩된 라우트는 children 속성으로 하위 라우트를 정의할 수 있다.
        children: [
          { path: 'new', component: { template: '<div>New Post</div>' } },
          { path: 'detail', component: { template: '<div>Post Detail</div>' } }
        ]
      }
  
   
    ]
  })
  ```



### 동적 라우트 매칭

```javascript
const User = {
  template: '<div>User</div>'
}

const router = new VueRouter({
  routes: [
    // 동적 세그먼트는 콜론으로 시작합니다.
    { path: '/user/:id', component: User }
  ]
})
```

* 동적 세그먼트는 콜론 `:`으로 표시됩니다. 

* 라우트가 일치하면 동적 세그먼트의 값은 모든 컴포넌트에서 `this.$route.params`로 표시됩니다. 

  ```javascript
  const User = {
    template: '<div>User {{ $route.params.id }}</div>'
  }
  ```

* 라우트 정의 우선순위로 처리 
  * /post/new
  * /post/:id



```javascript

<html>
  <head>
  </head>
  <body>

    <div id="app">
      <header>
        <h1>Router</h1>
      </header>
      <p>
        <router-link to="/main">Main 컴포넌트로 이동</router-link>
        <router-link to="/login">Login 컴포넌트로 이동</router-link>
      </p>
      <router-view><router-view>  <!-- url 값에 따라 갱신되는 화면 영역 -->
    </div>
    <hr>

    <script src="https://cdn.jsdelivr.net/npm/vue@2.5.2/dist/vue.js"></script>
    <script src="https://unpkg.com/vue-router@3.0.1/dist/vue-router.js"></script>
    <script>
    var Main = {
      template: '<div>main</div>'
    }
    var Login = {
      template: '<div>Login</div>'
    }
    var routes = [
      {path:'/main', component:Main},
      {path:'/login', component:Login}
    ]
    
    var router = new VueRouter({
      routes
    });
    
    new Vue({
        el :'#app',
        router :router
    })
    </script>
  </body>
</html>



```

*  정의되지 않은 경로로 들어올결우
  *  `{ path: '*', component: NotFound }` 설정에 의해 NotFound 컴포넌트가 그려질 것이다.





## 라우터를 통해 값 전달

###1. path에 key-value 

- 전달
  - router.pash()의 인자로 path를 넘길때 , key-value 작성

    ```javascript
    router.push("/login?key=value &key2=value")
    ```
- 받기
  - vue 객체의 query 이용

  - ```javascript 
    this.$route.query.key
    ```

    전달 값은 주소창에 노출되므로, 보안 내용은 금지한다


### 2.params를 이용한 값 전달

*  params를 이용할때는 `name`  와 `props:true` 사용

  * routes에 추가한다 

    ```javascript
    routes:[
        {
            path: '/user',
            name:'login',
            componet:Login,
            props:true
        }
    ]
    ```

* 전달

  ```javascript
  router.push({ name:'login',params:{'key':'value'} })
  ```

  * path대신 name을 이용해 어떤 경로로 갈지 알려주고 
  * params를 이용해 전달한  값을 객체로 알려준다 

* 받기

  ```javascript
  this.$route.params.key
  ```

  * rul에 노출이 없엇, path 방법보다는 안전하다

## 

## 데이터 가져오기

> 매개 변수와 함께 라우트를 사용할 때 주의 해야할 점은 사용자가 `/user/foo`에서 `/user/bar`로 이동할 때 **동일한 컴포넌트 인스턴스가 재사용된다는 것입니다.** 두 라우트 모두 동일한 컴포넌트를 렌더링하므로 이전 인스턴스를 삭제 한 다음 새 인스턴스를 만드는 것보다 효율적입니다. **그러나 이는 또한 컴포넌트의 라이프 사이클 훅이 호출되지 않음을 의미합니다.**
>
> 동일한 컴포넌트의 params 변경 사항에 반응하려면 `$route` 객체를 보면됩니다.

* 데이터 페치 : 서버로 부터 데이터를 가져오는 기능

* 각 화면별로 라우팅이 일어나는 시점에 데이터를 불러와야한다

* 화면이 리프레시 될 경우만 `created`훅이 동작한다

  * :id 값이 변경될때 같이 변경되는 것이 필요하다 =>`watch`사용 

* `created()` 훅에서 가져온다

  ```javascript
  created() {
    // 컴포넌트 생성시 데이터를 패치한다 
    this.fetchData()
  },
  watch: {
    // 라우터 객체를 감시하고 있다가 fetchData() 함수를 호출한다 
    '$route': 'fetchData'
  },
  methods: {
    fetchData () {
      this.data = null
      this.loading = true
  
      Post.get(this.$route.params.key).then(data => {
        this.data = data
        this.loading = false
      })
    }
  }
  ```

   

## 

# 라우트 컴포넌트에 속성 전달

컴포넌트에서 `$route`를 사용하면 특정 URL에서만 사용할 수 있는 컴포넌트의 유연성을 제한하는 라우트와 강한 결합을 만듭니다.

컴포넌트와 라우터 속성을 분리하려면 다음과 같이 하십시오.

**$route에 의존성 추가**

```js
const User = {
  template: '<div>User {{ $route.params.id }}</div>'
}
const router = new VueRouter({
  routes: [
    { path: '/user/:id', component: User }
  ]
})
```

**속성에 의존성 해제**

```js
var MasterReqCncpDetail = {
    template: `<div>  의뢰서 상세 {{ param.reqType }}  {{ param.key }} 값 :{{  param.keyNo }} 
       
</div>`
    ,
    data() {
        return {};
    },
    props: {
        param: {
            type: Object,
            default() { return null; },
        }
    },
    created() {

    }
}

var router = new VueRouter({
    mode: 'history',
    routes: [
        {
            path: '/master/request', component: {template: '<router-view></router-view>'},
            children: [
                {path: 'reqList/:key/:keyNo', name: 'masterReqList', component: MasterReqList, props: true},

                {path: ':reqType/detail', name: 'masterReqCncpDetail', component: MasterReqCncpDetail, props: true},

        }
    ]
})
        
          <router-link :to="{ name:'masterReqCncpDetail',params: {param: {keyNo:'5', reqType:'proto',key:'reqNo'}}}">masterReqCncpDetail</router-link><br>
```

이를 통해 어디서나 컴포넌트를 사용할 수 있으므로 컴포넌트 재사용 및 테스트하기가 더 쉽습니다.

## 

## 

## 

## 인증 플루우 

* 권한이 있는 path로 만 들어가게 하기 
* SPA 개발에서는 로그인 후 서버에서 발급받은 액세스 토큰(access token)의 유무로 확인할 수 있다.
*  뷰 라우터에는 **네비게이션 가드**라는 기능이 있는데 이것이 인증 플로우를 구현하는데 적합하다.
* vue 각각에서 처리하면 번거롭다 - > 라우터에서 생성되기 전에 체크를 한다
*  라우터가 타고 들어가는건, 해당 컴포넌트를 그리는 것이므로 그리기전에 체크한다

### 네비게이션 가드

* 라우터 설정 객체에는 `beforeEnter()` 함수를 추가할 수 있다. 
* 이 함수는 경로에 따라 라우트 매칭을 결정하고 해당 컴포넌트를 생성하기 직전에 호출되는 함수다. 
* 여기서 인증 여부를 판단한 뒤 컴포넌틀 랜더링을 진행하거나 혹은 로그인 페이지로 이동하는 등의 로직을 구현하면 된다.



    const requireAuth = (to, from, next) => {
      if (Auth.loggedIn()) return next()
      next({
        path: '/login',
        query: { redirect: to.fullPath }
      })
    }
    
    { 
      path: '/posts', component: Posts,
      children: [{ 
        path: 'new', 
        component: NewPost, 
    
        // 인증 여부를 체크하는 requreAuth를 beforeEnter 속성에 추가했다
        beforeEnter: requireAuth 
      },



## 라우터 url에 해시 값(#)을 없애는 방법

    var router = new VueRouter({
      mode:'history',
      routes
    });