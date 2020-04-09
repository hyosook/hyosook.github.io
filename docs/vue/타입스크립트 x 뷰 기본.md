# 타입스크립트 x  쀼

> TypeScript를 vue에 적용하는 방법은 두가지가 있다 

> * `Vue.extend`[^1] 를 이용해 객체로 만드는 방법
>* `Class` [^2]로 만드는 방법



## [^1]Vue.extend

> 기존에 사용하던 방식과 거으 흡사하게 사용할수 있다 .
>
> 하지만, 좋은 조합이 아니라는 의견이 있는듯 

```HTML
<script lang="ts"
import Vue from 'vue';

export default Vue.extend({
  data() {
    return {
      
    };
  },
  methods: {
    
  }
});
</script>
```





##  [^2] Decorator(@)  사용

> 기존의 객체 스타일 작성 방식이 아닌 클래스 기반 작성 방식을 사용
>
> `Vue` 라이브러리를 클래스에 상속하여 
>
> 클래스 멤버 변수와 클래스 함수 형태로 정의

```html
<template>
   
</template>

<script lang="ts"> 
import Vue from 'vue'
import Component from 'vue-class-component'

@Component 
export default class myComponent extends Vue { 
   
}
</script>

```

###  class에 정의 

#### Data

>  클래스 `맴버`로 정의해서 사용한다 

```html
    
@Component  
export default class myComponent extends Vue { 
    name ='hyosook'
}

```

#### computed

> 클래스 `get /set` 로 정의하여 사용

```html
  
@Component  
export default class myComponent extends Vue { 
    name ='hyosook'
    
    get myname(){
        return this.name+'입니다'
    }
    
    set name(value) {
      this.name =value
  }
}

```

#### methods

```html
    
@Component  
export default class myComponent extends Vue { 
   
   onSaveBtn(){
        alert('버튼클릭')
    }
}

```

#### 라이프 사이클 

> 메소드와, 라이프 사이클 같은 공간에서 정의 되므로, 이름 주의 필요

```html
@Component  
export default class myComponent extends Vue { 
   beforeDestory(){  
    }
    mounted () {
    }
     render() {
    }
}
```

### vue-property-decorator 

> 위의 `vue-class-component`  와 더불어서, 유용한 라이브러리
>
>  `vue-class-component`  기반으로 작성됨 

```html
<script lang="ts">
import { Vue, Component, Prop } from 'vue-property-decorator';

@Component
export default class myComponent extends Vue {
  
}
</script>  
```

- [`@Prop`](https://github.com/kaorun343/vue-property-decorator#Prop)
- [`@PropSync`](https://github.com/kaorun343/vue-property-decorator#PropSync)
- [`@Model`](https://github.com/kaorun343/vue-property-decorator#Model)
- [`@Watch`](https://github.com/kaorun343/vue-property-decorator#Watch)
- [`@Provide`](https://github.com/kaorun343/vue-property-decorator#Provide)
- [`@Inject`](https://github.com/kaorun343/vue-property-decorator#Provide)
- [`@ProvideReactive`](https://github.com/kaorun343/vue-property-decorator#ProvideReactive)
- [`@InjectReactive`](https://github.com/kaorun343/vue-property-decorator#ProvideReactive)
- [`@Emit`](https://github.com/kaorun343/vue-property-decorator#Emit)
- [`@Ref`](https://github.com/kaorun343/vue-property-decorator#Ref)
- `@Component` (**provided by** [vue-class-component](https://github.com/vuejs/vue-class-component))



### vuex 사용 

> 클래스를 이용한 설계 가장 추천됨 

#### vuex-module-decorators

```js

import { VuexModule, Module, Mutation, Action } from 'vuex-module-decorators'

@Module({ namespaced: true, name: 'test' })
class User extends VuexModule {
    
  public name: string = ''

   get myname() {
    return this.name / 2
  }

  @Mutation
  public setName(newName: string): void {
    this.name = newName
  }

  @Action
  public updateName(newName: string): void {
      this.setName(newName)
    //this.context.commit('setName', newName)
  }
}
export default User
```

```html
@Module
class MyModule extends VuexModule {
  foo = "bar";

  get someGetter() {
    return 123;
  }
  get myGetter() {
    this.foo; // -> "bar"
    this.someGetter; // -> 123
    this.someMutation(); // undefined, getters cannot call mutations
    this.someAction(); // -> undefined, getters cannot call actions
  }

  @Mutation
  someMutation() {
    /* ... */
  }
  @Mutation
  myMutation() {
    this.foo; // -> "bar"
    this.someGetter; // -> undefined, mutations dont have access to getters
    this.someMutation(); // -> undefined, mutations cannot call other mutations
    this.someAction(); // -> undefined, mutations cannot call actions
  }

  @Action
  async someAction() {
    /* ... */
  }
  @Action
  async myAction() {
    this.foo; // -> "bar"
    this.someGetter; // -> 123
    this.myMutation(); // Ok
    await this.someAction(); // Ok
  }
}
```



```html
import Vue from 'vue'
import Vuex from 'vuex'
import User from '@/store/modules/user'
Vue.use(Vuex)
const store = new Vuex.Store({
  modules: {
    User
  }
})
export default store
```



#### vuex-class

> 우리가사용하는 helper처럼 사용하는기능

```html
<template>
  <div class="details">
    <div class="username">User: {{ nameUpperCase }}</div>
    <input :value="name" @keydown="updateName($event.target.value)" />
  </div>
</template>
<script lang="ts">
import { Component, Vue } from 'vue-property-decorator'
import {  State,
  Getter,
  namespace } from 'vuex-class'
const users = namespace('users')
const user = namespace('user')
@Component
export default class User extends Vue {
  @user.State
  public name!: string

  @user.Getter
  public nameUpperCase!: string

  @user.Action
  public updateName!: (newName: string) => void
}
</script>
```

```js
export default class MainNavBar extends Vue {


  @Getter 
  private recruitParts:  BasicCodeName[] | null

  @Mutation
  private changeActiveRecruitPart: (part:BasicCodeName) => void 


  @users.Getter
  private isUser!: boolean

  @users.Mutation('LOGOUT')
  private _logout: () => void 

  private get recruitPeriod(){ 
   return  this.activeRecruitPart!==null?  this.activeRecruitPart.startDate + " ~ " + this.activeRecruitPart.endDate :
    ' 선택한 모집회차가 없습니다'
    }
  

  /**
   * 상단 navbar 에서 회차 변경 시 main 화면으로 이동
   * @param val
   */
   private _changeActiveRecruitPart(part:BasicCodeName) {
     this.changeActiveRecruitPart(part)
     if (this.$router.currentRoute.path !== "/") {
       this.$router.push("/");
     }
    
}
}
```



* 사용하기

```HTML
const mutations = {
  LOGOUT: (_state) => {
  }
}
```



```HTML
 <a @click="LOGOUT()">로그아웃</a>


@users.Mutation
private LOGOUT: () => void 

```

```HTML
 <a @click="_logout()">로그아웃</a>


@users.Mutation('LOGOUT')
private _logout: () => void
```

```html
 @users.Getter
  private isUser!: boolean
```







# 타입스크립트 

## readonly

> 읽기 전용으로 한정하겠다	

```js
  export default class myComponent extends Vue { 
  @Prop(String) readonly name:string #할당불가
@Model('update',{type:Object}) readonly profile:strin #할당불가
@PropSync(String) value:string # 할당 가능 
}
```

## !

대상 맴버에 대한 `NonNullAssersion` 

`null / Undefined` 설정 할수 없다 

남용하면 좋지않으므로 , 

`required: true`   , `기본값 설정` 하는 경우에 사용한다 

```js
export default class myComponent extends Vue { 
  @Prop({ type: String, required: true }) readonly name!: string;
  @Prop({ type: Array, default: () => [] }) readonly items!: string[];
}
```

### ?

Undefined간응?