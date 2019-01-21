---
title : mixin
tags : ["vue.js"]
---



# MixIn

> * vue 구성요소에 재사용 가능한 기능을 넣는 방법
>
> * 재사용 가능한 기능은 기존 기능과 병합 
>
> * 캡슐화 및 공통된 기능을 분리시켜 코드 재사용성을 높혀준다.
>
> * 오버라이딩 기능도 사용할 수 있어 커스텀 및 확장에 용이하다.
>
>   ​
>
> * 얼핏 보면 extends와 유사하게 보일 수가 있다.
>
>   * extends: 유사한 기능의 컴포넌트들을 추상화 하여 상위 컴포넌트를 만들고 차이가 있는 기능들을 하위 컴포넌트에 구현한다.
>   * mixins: 서로 다른 기능의 컴포넌트에 동일한 기능을 배포하는 방법으로 예를 들면 로깅 기능을 aspect 단위로 추가하는 것을 들 수 있다.



## 선언 및 사용

### 1. 

```javascript vue.js
var mixin1 = {
...
}

var mixin2 ={
 ...
}

new Vue({
  el: '#app',
  mixins: [mixin1,mixin2]
})

```

### 2. 전역 MixIn

> 생성되는 모든 vue 에 영향을 주기때문에 `사용자 지정 옵션`을 줘서 사용한다

- 사용자 지정 옵션

```javascript vue.js
Vue.mixin({
  created: function () {
    var myOption = this.$options.mixInOption1   
    if (myOption) {
      console.log("전역mixIn실행:"+myOption)
    }
  }
})
new Vue({
  el: '#app',
  mixInOption1: 'hello!'
})

//Console Logs
> 전역mixIn실행 hello!
```

- vue 인스턴스 , key :`mixInOption1` value: `'hello!'` 
- Global  mixin , `this.$options.  ` `key` 조건 으로 



## 특징

* mixin객체 안에 `created()`, `methods`, `data`,`mounted`... 구성 옵션 포함 가능

```javascript vue.js
var mixin = {
  data: {
    name: '이름'
  },
  created: function () {
    console.log('mixin hook called')
  },
  methods: {
    mixinFnc() {
      console.log('html 직접접근 '+this.name);
    }
  }
}
```



* html에서 직접 접근 가능

```html vue.js
<div id="app">
  <button @click.prevent="mixinFnc">click</button>
</div>

var mixin = {
 methods: {
    mixinFnc() {
      console.log('mixinFnc 함수');
    }
  }
}
```



* vue 컴포넌트 안에서 mixin안 `data` 도 접근 가능 ex) `this.name`  

```javascript veu.js
var mixin = {
  data: {
    name: '이름'
  }
}
new Vue({
  el: '#app',
  mixins: [mixin,mixin2],
  created: function () {
    console.log('component hook called:' +this.name)
  }
})

//Console Logs
> component hook called: 이름
```



* `중첩 옵션`이 포함되어 있으면 `mixin 먼저` 호출된 이후, vue 컴포넌트 호출

```javascript vue.js
const myMixin = {
  created(){
    console.log("mixin hook called)
  }
}

new Vue({
  el: '#root',
  mixins:[myMixin],
  created(){
    console.log("component hook called")
  }
})

//Console Logs
> mixin hook called
> component hook called
```



* 하지만, `methods`, `components`, `directives`와 같은 객체값 요구하는 옵션에 `충돌하는 키`가 있을 경우 (같은 메소드명) `컴포넌트 옵션만` 호출됨.

```javascript vue.js
const myMixin = {
methods:{
    name(){
       console.log("myMixin")
    }
  }
}

new Vue({
  el:"#root",
  mixins:[myMixin],
  methods:{
    name(){
      console.log("Christian John")
    }
  }
})

//Console Logs
> Christian John
```



## Global function 사용하고 싶은경우

### 1.

* `.js`파일을 만든다

  ```javascript mixin
  var mixin = {
    data: {
      name: '이름'
    },
    created: function () {
      console.log('mixin hook called')
    },
    methods: {
      mixinFnc() {
        console.log('html 직접접근 '+this.name);
      }
    }
  }
  ```

* 사용을 원하는 곳에서 `.js`파일을 넣고 ,  `mixIn 선언` 한다

  ```javascript vue.js
  new Vue({
    el:"#root",
    mixins:[myMixin]
  })
  ```



### 2. 전역 MixIn 