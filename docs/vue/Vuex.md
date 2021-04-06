---
title : vuex 사용(모듈화 포함)
tags : ["vuex ","저장소", "vue.js"]
---



# vuex

> `단일상태`관리 `패턴`  + 라이브러리
>
> 모든 컴포넌트에 대한 중앙 집중식 `저장소` 역활
>
> 단일 `state` , `Getters`로 읽고 ,  `actions`로 `mutations`변화하고 `mutations`로 값을 변경 한다 

**One-way data flow 를 가진다**

## 사용 이유?

> `eventBus` 도 있는데, 왜 vuex를 쓸까?
>
> * 컴포넌트 간의 `부-자` 
> * 컴포넌트 간의 `형제` 
>
> 두 타입간의 데이터 공유가 씽크문제가 발생하는 것을 막을 수 있다.

## vuex 부르기

* main.js 안에서 적용된 것은 `$`를 붙여서 불러올수 있다.

  ```javascript
  const app = new Vue({
    // "store" 옵션을 사용하여 저장소를 제공하십시오.
    // 그러면 모든 하위 컴포넌트에 저장소 인스턴스가 삽입됩니다.
    store
  })
  ```

  * `this.$store`

* vuex는 `store` 옵션(`Vue.use(Vuex)`에 의해 가능)으로 루트 컴포넌트의 모든 자식 컴포넌트에 저장소를 `주입`하는 메커니즘을 제공합니다.

  * 플러그인 형태 = 전역으로 

    `.use () ` 사용



## State

>Vuex는 **단일 상태 트리** 를 사용합니다. 
>
>즉, 이 단일 객체는 모든 애플리케이션 수준의 상태를 포함하며 "원본 소스" 역할을 합니다. 
>
> 단일 상태 트리를 사용하면 특정 상태를 쉽게 찾을 수 있으므로 디버깅을 위해 현재 앱 상태의 스냅 샷을 쉽게 가져올 수 있습니다.

### 선언

```javascript
const store = new Vuex.Store({
  state: {
    count: 0
  }
})
```



### vuex 상태를 vue 컴포넌트 에서 가져오기

>  상태를 "검색"하는 가장 간단한 방법은 [계산된 속성](http://kr.vuejs.org/guide/computed.html)내에서 일부 저장소 상태를 가져오는 것

### 1. computed

```javascript
  computed: {
    count () {
      return this.$store.state.count
    }
  }
```

### 2. `mapState `

* 배열 형태 사용 ( 매핑 된 계산된 속성의 이름이 상태 하위 트리 이름과 같을 때 문자열 배열을 `mapState`에 전달할 수 있습니다.) 

```javascript
import {mapState} from 'vuex'    ///vuex에서 mapState 가져오기
computed: mapState([
  // this.count를 store.state.count에 매핑 합니다.
  'count'
])
```

### 3. `mapState` ,**proposal-object-rest-spread**

* 다른 계산된 속성과 사용시 

```javascript
import {mapState} from 'vuex'    ///vuex에서 mapState 가져오기
computed:{
localComputed () { /* ... */ },
...mapState(['count'])
}
```

> 만약 , vue컴포넌트 내에서 단 한개의 상태만을 가져온다면, `1` 번의 방법을 사용하겠지만 
>
> 확장 성을 위해서 `3`번 사용을 표준으로 한다  



## Getters

> 때로는 저장소 상태를 기반하는 상태를 계산해야 할 수도 있습니다.(예: 아이템 리스트를 필터링하고 계산)
>
> 장소의 계산된 속성으로 생각할 수 있습니다. 계산된 속성처럼 getter의 결과는 종속성에 따라 `캐쉬`되고, 일부 종속성이 변경된 경우에만 다시 재계산 됩니다.
>
> 모든 컴포넌트에서 부르는 상태가 있다면, `getters`에 등록해서 사용하는것이 중복코드를 막는 좋은방법



### 선언 

```javascript
const store = new Vuex.Store({
  state: {
    todos: [
      { id: 1, text: '...', done: true },
      { id: 2, text: '...', done: false }
    ]
  },
  getters: {
    doneTodos: state => {
      return state.todos.filter(todo => todo.done)
    }
  }
})

```

- 속성 인자로 다른 getter 로 가능

```javascript
getters: {
  // ...
  doneTodosCount: (state, getters) => {
    return getters.doneTodos.length
  }
}
```

* getter에 전달 인자로 전달

```javascript
getters: {
  // ...
  getTodoById: (state) => (id) => {
    return state.todos.find(todo => todo.id === id)
  }
}


///컴포넌트에서 사용 
store.getters.getTodoById(2) // -> { id: 2, text: '...', done: false }
```



###1.computed

```javascript
computed: {
  doneTodosCount () {
    return this.$store.state.todos.filter(todo => todo.done).length
  }
}
```



### 2.`mapGetters` ,proposal-object-rest-spread

* Enhancde Object Literals 
  * object의 property가 key와 value가 동일하다면, 하나로 축약 가능

```javascript
import { mapGetters } from 'vuex'

export default {
  // ...
  computed: {
    // getter를 객체 전개 연산자(Object Spread Operator)로 계산하여 추가합니다.
    ...mapGetters([
      'doneTodosCount',
      'anotherGetter',
      // ...
    ])
  }
}
```

* getter를 다른 이름으로 매핑

```javascript
computed: {
...mapGetters({
  // this.doneCount를 store.getters.doneTodosCount에 매핑하십시오.
  doneCount: 'doneTodosCount'
})
 }
```





## mutations

> state 값을 변화 시키는 mutations
>
> * state는 직접 변경도 가능하다  >> but 절대 권장 하지 않는다 
>   * 디버깅을 보장 되지 않음 (dev-tools)
>   * 상황에 따라서, 값이 바뀌지 않을 때도 있다 
>
> 같은 기능을 하는 함수 사용을 막기 위해서 사용
>
> 무조건 동기적 이여야 한다  >> 대부분 상태 변경
>
> 비동기 (axios) 같은 경우 , action에서 
>
> mutations 을 호출 = commit 
>
> mutations을 commit 해서 state를 변화 한다 
>
> `vue.set()`을 사용해서 state를 변경 가능 > vue reactive rule에 따라서 가능. 이후에 mutation을 시킴.
>
>  Mutations 의 역할 자체가 State 관리에 주안점을 두고 있다. 상태관리 자체가 한 데이터에 대해 여러 개의 컴포넌트가 관여하는 것을 효율적으로 관리하기 위함인데 
>
> Mutations 에 비동기 처리 로직들이 포함되면 같은 값에 대해 여러 개의 컴포넌트에서 변경을 요청했을 때, 그 변경 순서 파악이 어렵기 때문이다.
>
> 처리 시점을 예측할 수 있는 동기 처리 로직만 넣어야 한다.
>
> 자바스크립트의 비동기 처리란 특정 코드의 연산이 끝날 때까지 코드의 실행을 멈추지 않고 다음 코드를 먼저 실행하는 자바스크립트의 특성을 의미합니다.

- 처리 시점을 예측할 수 있는 동기 처리 로직만 넣어야 한다.

- 여러개의 컴포넌트에서 아래와 같이 state 값을 변경하는 경우 어느 컴포넌트에서 해당 state를 변경했는지 추적하기 어렵다.

  ```
  methods: {
     increaseCounter() { this.$store.state.counter++; }
  }
  ```

- 특정 시점에 어떤 컴포넌트가 state를 접근하여 변경한 건지 확인하기 어렵기 때문이다.

  - 뷰의 반응성(화면의 변화를 스크립트에서 인지하는것 등)을 거스르지 않게 명시적으로 상태변화를 수행한다. 반응성, 디버깅, 테스팅 혜택



### 선언

```js
const store = new Vuex.Store({
  state: {
    count: 1
  },
  mutations: {
    increment (state) {
      // 상태 변이
      state.count++
    }
  }
})
```

```js
this.$store.commit('increment', 10)
```

* 전달 인자(payload) 가능

```javascript
const store = new Vuex.Store({
  state: {
    count: 1
  },
  mutations: {
     increment: (state, payload)=> {
      // 상태 변이
      state.count+=payload.amount
    }
  }
})
```


* vue의 반응성 규칙

  >Vuex 저장소의 상태는 Vue에 의해 반응하므로, 상태를 변경하면 상태를 관찰하는 Vue 컴포넌트가 자동으로 업데이트됩니다. 이것은 또한 Vuex 변이가 일반 Vue로 작업 할 때 동일한 반응성에 대한 경고를 받을 수 있음을 의미합니다.
  >
  >원하는 모든 필드에 앞서 저장소를 초기화하는 것이 좋습니다.

```javascript
state.obj = { ...state.obj, newProp: 123 }
```




* 객체 스타일로 commit 가능  (명사로 표현이 가능하므로, 더 직관적이다)

```javascript
this.$store.commit({
  type: 'increment', // mutations 이름 
  amount: 10
})
```

### mapMutations 

> 컴포넌트 안에서 변이 커밋하기
>
> `this.$store.commit('xxx')`를 사용하여 컴포넌트에서 변이를 수행하거나
>
>  컴포넌트 메소드를 `store.commit` 호출에 매핑하는 `mapMutations` 헬퍼를 사용할 수 있습니다 (루트 `store` 주입 필요)

```js
import { mapMutations } from 'vuex'

export default {
  // ...
  methods: {
    ...mapMutations([
      'increment' // this.increment()를 this.$store.commit('increment')에 매핑합니다.
    ]),
    ...mapMutations({
      add: 'increment' // this.add()를 this.$store.commit('increment')에 매핑합니다.
    })
  }
}
```

> 이렇게 하면, 실제 선언된 함수처럼 사용이 가능하다 



## actions

> 비 순차적 또는 비동기 처리 로직들을 선언한다
>
> 하나의 state에 여러가지 데이터가 접근하려 할떄 순서를 알기 힘들다
>
> dispatch(보내다) 사용해서 actions을 부른다
>
> 컴포넌트에서 dispatch로 actions을 부르고 , commit으로 mudtaions이 호추로디어 state가 변함 
>
> 비동기 처리 로직을 선언하는 메서드 `async methods`. 즉, 비동기 로직을 담당하는 mutations이다.
>
> 데이터 요청, Promise, ES6 async과 같은 비동기(결과를 받아올 타이밍이 예측되지 않은 로직) 처리는 모두 actions에 선언한다.
>
> 

```javascript
actions: {
  incrementAsync: ({ commit },payload)=> {
    setTimeout(() => {
      commit('increment')
    }, 1000)
  }
}
```

### mapActions

```javascript
import { mapActions } from 'vuex'

export default {
  // ...
  methods: {
    ...mapActions([
      'increment' // this.increment()을 this.$store.dispatch('increment')에 매핑
    ]),
    ...mapActions({
      add: 'increment' // this.add()을 this.$store.dispatch('increment')에 매핑
    })
  }
}

```

## why ??

### sate , getters 는 `computed` 인데 , mutations,actions는 `methods` 에 매핑할까?

1. 수정하는 것이지 , 가져오는게(state, getter) 아니다
2. computed는 `캐싱`

## 모듈화

> 저장소를`모듈`로 나눌수 있다.
>
> 관리를 위해 모듈로 나누어서 사용한다. 

#### store/modules/modal.js 

modal이라는 모듈 한개

```javascript
const state = {
 test: 'test1'
}
const actions = {

}
const getters = {

}

const mutations = {

}

export default {
  state,
  getters,
  actions,
  mutations
}
```

#### store/modules/index.js

모든 모듈을 `store/index.js` 상단에 `import XX from 'store/modules/XX'` 라고 써줘야하는 번거로움이 있다. 

js 로 modules 파일 아래 모든 파일들을 import 해주도록 짜두면 편리하다.

```javascript js
import camelCase from 'lodash/camelCase'
// Storing in variable a context with all files in this folder
// ending with `.js`.
const requireModule = require.context('.', false, /\.js$/)
const modules = {}

requireModule.keys().forEach(fileName => {
    if (fileName === './index.js') return
    // filter fullstops and extension 
  // and return a camel-case name for the file
    const moduleName = camelCase(
        fileName.replace(/(\.\/|\.js)/g, '')
    )
  // create a dynamic object with all modules
    modules[moduleName] = {
    // add namespace here
        namespaced: true,
        ...requireModule(fileName).default
    // if you have exported the object with name in the module `js` file
    // e.g., export const name = {};
    // uncomment this line and comment the above
        // ...requireModule(fileName)[moduleName]
    }
})
export default modules
```

#### store/index.js

modules 만 import 해준다.

```javascript js
import Vue from 'vue'
import Vuex from 'vuex'
import modules from './modules'

Vue.use(Vuex)

const store = new Vuex.Store({
  modules
})

export default store
```



### 사용

```javascript
  store.commit('SET_ERROR', err, { root: true }) //  부모 실행
```









### getters 를 watch로 감시하기

```javascript
  mounted () {
    this.$store.watch((state, getters) => getters['appl/applStatus'](this.$route.params.applNo), (newval, oldval) => {
      console.log('applStatus change')
      this.active = false
    })
  }
```

