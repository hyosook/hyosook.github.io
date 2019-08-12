> 인증,인가 처리에  [vue-kindergarten](https://github.com/JiriChara/vue-kindergarten) 사용한다 <br>
> ex) [Role Based Authorization 설명 ](https://codeburst.io/role-based-authorization-for-your-vue-js-and-nuxt-js-applications-using-vue-kindergarten-fd483e013ec5) <br>
> Vue + Vuex + VueRouter + vue-kindergarten



# kindergarten(유치원)  사용 용어 

## Child (아기)

- 킨더가든이 관리해야할 대상 
  - 어플리케이션의 현재 사용자 

## Perimeter(범위,둘레)

* 어플리케이션 내부 권한별 기능, 메소드 범위를 정의하는 모듈이다. 

## Governess(원장선생님)
* Sandbox의 보초이다. Child가 어떤 권한없는 활동을 하지 못하도록 한다. 

## Sandbox

- Perimeter는 Sandbox안에 연결되어있으며, 메소드들은 Sandbox 통해서 접근가능하다. 
- Sandbox는 Governess에 의해서 통제된다. 규칙을 따르지 않았을 때 발생할 문제들을 예방한다.





# 예제 설명

## child

> vuex의 `user` 를 child 로 선언한다

```js
export default store => store && store.getters['users/loggedInUser']
```



## Governess

> 라우팅을 관리하는 Governess 를 정의한다.
>
> * 사용자가 정의된 `action` 
>   * 허락됨  :  전달받은 `next() ` 함수가 수행된다.  => 정상 라우팅이 일어남 
>   * 허가되지 않음  :  전달받은 `next() ` 함수에 , `next({ name: 'home-main' })`으로 선언해서 수행된다 =>  `home-main`  으로 라우팅 됨 



* /governess/RouteGoverness.js

  ```js
  import { HeadGoverness } from 'vue-kindergarten'
  
  export default class RouteGoverness extends HeadGoverness {
    guard (action, { next }) {
      return this.isAllowed(action) ? next() : next({ name: 'home-main' })
    }
  }
  ```

  

* `/router/index.js`

  > 전역 네비게이션 가드 에서 호출리턴 한다

  ```js
  ... 생략 
  return sandbox.guard(action, {
          from,
          to,
          next
        })
  ```

  

## Perimeter

> `Perimeter`를 상속한 `BasePerimeter` 를 만들고 
>
> `BasePerimeter` 를 상속한  `Perimeter`들은 만든다 

* BasePerimeter

  > 공통으로 사용하는 기능을 정의 



## router  beforeEach

> 전역 네비게이션 가드 
>
> 킨더가든을 통한, 인증&인가 처리를 한다 



```js
router.beforeEach((to, from, next) => {

  let perimeter = null

  to.matched.some((routeRecord) => {
    if (!typeUtil.isNullOrUndefined(routeRecord.meta.perimeter)) {
      perimeter = perimeters[routeRecord.meta.perimeter]
    }
    const Governess = routeRecord.meta.governess || RouteGoverness 
    const action = routeRecord.meta.perimeterAction || 'read' 
    
    if (perimeter) {
      const sandbox = createSandbox(child(store), {
        governess: new Governess(),
        perimeters: [
          perimeter
        ]
      })
      return sandbox.guard(action, {
        from,
        to,
        next
      })
    }
    return next()
  })
})
```



# 사용

* noticePerimeter 생성

  ```js
  import BasePerimeter from './base-perimeter'
  
  export default new BasePerimeter({ #상속받아서 사용 
    purpose: 'notice', 
    can: {
      read () {
        return this.isUserRole()
      },
      update (creId) {
        return this.isAdminRole() || this.isCreator(creId)
      }
    }
  })
  ```



### 라우팅

* routes.js 에서 선언한다

  ```js
    {
      path: '/post',
      name: 'post',
      components: parent('post'),
      meta: {
        perimeter: 'noticePerimeter' #
      },
      children: [
        {
          path: 'list/notice',
          name: 'post-list-NOTICE',
          component: loadView('post-list'),
          meta: {
            title: 'post.notice.title',
            type: 'NOTICE'
          }
        },
        {
          path: 'update/notice/:postNo/:creId',
          name: 'post-update-NOTICE',
          component: loadView('post-register'),
          meta: {
            title: 'post.notice.title',
            type: 'NOTICE',
            perimeterAction: 'update', #지정하지 않으면 기본 read
            governess: RoutePramGoverness  #지정하지 않으면 기본 RouteGoverness
          }
        }
      ]
    }
  ```

  



### 메소드

* 사용할곳에서 선언한다 

  ```js
  import noticePerimeter from '@/kindergarten/perimeters/notice-perimeter'
  
  export default {
  
    perimeters: [
      noticePerimeter
    ],
      
  }
  ```

  

* `$perimeter의 purpose.isAllowed('can 메소드명',parameter)`

  ```js
   <button v-show="$notice.isAllowed('update', post.author.userId)">수정</button>
  ```
