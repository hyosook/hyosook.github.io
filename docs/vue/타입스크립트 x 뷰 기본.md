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

  

#### `@Component`

```html
<script lang="ts">
import {Component, Vue} from 'vue-property-decorator'; 
import apexButton from '@/components/apexButton.vue'; 
import modal from '@/components/modal.vue'; 
    
@Component({
    components:{
        apexButton,modal
    },
    mixins:[pageMxiin] 
})  
    
export default class myComponent extends Vue { 
  
}
</script>
```



### `@watch`

> `immediate:true` 컴포넌트 초기화 시에도 실행할지 여부

```html
<script lang="ts">
import {Component, Vue, Watch } from 'vue-property-decorator'; 
    
@Component
export default class myComponent extends Vue { 
     name ='hyosook'
    
    @Watch('name',{immediate:true})
    onChangeName(val: string, oldVal: string) {
        //바뀌었을때 처리
    }
    
}
</script>
```

#### `PropSync`

> 자식 컴포넌트에서 부모 컴포넌트의 **.sync** 속성을 전달할때 사용하는 데코레이터로 아래와 같이 사용해서 값을 할당하는 것만으로 처리가 가능하다.

```html
<myComponent childValue.sync="value" />
```

* `@PropSync `사용

```html

 <script lang="ts">
 import { Component, PropSync, Vue } from 'vue-property-decorator';
 
 @Component
 export default class myComponent extends Vue {
 @PropSync({ type: String }) childValue: string;
     
  // 값 변경 적용
  updateValue(newVal: string) {
    this.childValue = newVal;   # 이 시점에서 부모 컴포넌트로 전달된다.
  }
}

```

* `@Prop` & `$emit`  사용

```html
<script lang="ts">
 import { Component, Prop, Vue } from 'vue-property-decorator';
 
 @Component
 export default class myComponent extends Vue {
 @Prop({ type: String }) childValue: string;
     
  // 값 변경 적용
  updateValue(newVal: string) {
    this.$emit('update:childValue', newVal)   
  }
}
```



#### `@emit`

> * 이벤트 이름  
>   * `@Emit('이름')`
>   * 생략시 메서드 이름 케밥케이스로 사용

```html
<script lang="ts">
import { Vue, Component, Emit } from 'vue-property-decorator'

@Component
export default class YourComponent extends Vue {
  count = 0

  @Emit()
  addToCount(n: number) {
    this.count += n
  }

  @Emit('reset')
  resetCount() {
    this.count = 0
  }

  @Emit()
  returnValue() {
    return 10
  }

  @Emit()
  onInputChange(e) {
    return e.target.value
  }

}
```

```html
export default {
  data() {
    return {
      count: 0
    }
  },
  methods: {
    addToCount(n) {
      this.count += n
      this.$emit('add-to-count', n)
    },
    resetCount() {
      this.count = 0
      this.$emit('reset')
    },
    returnValue() {
      this.$emit('return-value', 10)
    },
    onInputChange(e) {
      this.$emit('on-input-change', e.target.value, e)
    }
  }
}
```

#### `@Ref`

>  $refs에서 참조할 수 있는 요소 또는 컴포넌트를 정의하는 것으로 사전에 정의함으로서 오타나 수정에 대응하기 쉽도록 하는 역할을 담당한다.

```html
<myComponent ref="childComponent" />
<buttom ref="submitButton"></buttom>

<script lang="ts">
 import { Component, Ref, Vue } from 'vue-property-decorator';
 
 @Component({
     component:{
         myComponent
     }
 })
 export default class myComponent extends Vue {
 @Ref() childComponent :myComponent
 @Ref() submitButton :HTMLButtomElement
     
  mounted(){
     // 자식 컴포넌트 메서드 실행
    this.childComponent.updateValue(); 
       // 버튼에 포커스 설정
    this.submitButton.focus()
  }
}
```





#### `@ Prop`



```html
<script lang="ts">
import {Component, Vue} from 'vue-property-decorator'; 
    
@Component
export default class myComponent extends Vue { 
   @Prop({type:String,  default:'hysook'})
    name:string
    
     @Prop({type:Boolean,  required:true})
    isShow:Boolean
    
}
</script>


 // '!'는 초기화 속성에 붙이는 prefix (타입스크립트에게 미리 알려주는 역할)
  @Prop() private msg!: string;
얘는 null이나 undifind 되뤖더
```



### 



### vuex

> 컴포넌트에서 mapAction 핼퍼를 사용하기 위해 Component 데코레이터  사용 가능 
>
> 하지만 권장하지 않는다
>

```html
<script lang="ts">
import {Component, Vue, Prop} from 'vue-property-decorator';
import {mapActions} from 'vuex';

@Component({
  methods: {
    ...mapActions(['addTodo'])
  }
})
export default class myComponent extends Vue {

}
</script>
```



### 클래스를 이용한 설계

### ROOT 인터페이스 이용설계

```javascript
import Vue from 'vue';
import Vuex, { ActionContext, StoreOptions } from 'vuex';

Vue.use(Vuex);

import overview from '@/store/modules/overview';
import sidebar from '@/store/modules/sidebar';


# 1. 인터페이스에서 state의 타입을 지정
export interface RootState {
  schoolCodes:string[];
  recruitParts: RecruitPart[]|null;
}


export interface RecruitPart  {
  schoolCode: string;
  enterYear:string;
  partSeq: string;
}

# 2. 스토어타입(Vuex에 StoreOptions라는 내장 객체타입을 이용)지정
#    인터페이스로 선언한 부분은 구현을 반드시 해야한다 
const store: StoreOptions<RootState> = {
  state: {
    schoolCodes :null,
    recruitParts: null
  },
  mutations: {
    initSchoolCodes(state: State, schoolCodes: string[]) {
      state.schoolCodes = schoolCodes;
    }
  },
  actions: {
     fetchAllRecruitParts({state, getters, dispatch, commit}: ActionContext<RootState, RootState>) {
      commit('initSchoolCodes', []);
    }
  },
  getters: {
    schoolCodes:(state: State)=>state.schoolCodes
  },
  modules: {
    overview,
    sidebar
  },
};

export default new Vuex.Store(store);

```



### **모듈 vuex-module-decorators**   

```js

import { VuexModule, Module, Mutation, Action } from 'vuex-module-decorators'

@Module({ namespaced: true, name: 'user' })
export default class User extends VuexModule {
    
  public name: string = 'hyosook'


# getter (state,getter 만)
   get myname() {
    return this.name
  }

   get name() {
    this.name; // hyosook
    this.myname() // hyosook
  }

# Mutation (state,Mutation,Action 만)
  @Mutation
  public setName(newName: string): void {
    this.name = newName
    this.updateName(newName)
  }


# Action (all 가능)
  @Action
  public updateName(newName: string): void {
      this.setName(newName)
    //this.context.commit('setName', newName) 같음
     
  }
}
```

#### 다른 모듈 호출 

```javascript

this.context.rootGetters['users/isUser']   # users모듈의 isUser getter
this.context.rootGetters.schoolCodes       # root의  schoolCodes  getter

```

```javascript
this.context.dispatch('module/action', payload, {root: true});
```



### vuex-class

> 우리가사용하는 helper처럼 사용하는기능

```javascript
# users 모듈 
const mutations = {
  LOGOUT: (_state) => {
  },
  LOGIN: (_state) => {
  }
}

```

```html
<template>
  <div class="details">
    <a @click="LOGOUT()">로그아웃</a>
      
    <a @click="_login()">로그인</a>
      
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator'
import {  State,Getter,namespace } from 'vuex-class'
const users = namespace('users')



@Component
export default class User extends Vue {
 

  @users.Getter
  private isUser!: boolean

  @Mutation
  private changeActiveRecruitPart: (part:BasicCodeName) => void 

  
  @users.Mutation
  private LOGOUT: () => void 
  
  
  
  @users.Mutation('LOGIN')
  private _login: () => void
  
   @user.Action
  public updateName!: (newName: string) => void

}
</script>
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

> `null / Undefined` 설정 할수 없다 
>
> 남용하면 좋지않으므로 , 

`required: true`   , `기본값 설정` 하는 경우에 사용한다 

```js
export default class myComponent extends Vue { 
  @Prop({ type: String, required: true }) readonly name!: string;
  @Prop({ type: Array, default: () => [] }) readonly items!: string[];
}
```



### ? 

> 선택적 매개변수. 
>
> 즉, 호출할 때 끼워줘도 되고 안 끼워줘도 되는 매개변수이다. 

```
function extend(target: any, deep?: boolean) { 
if (deep) {    // ...  } 
else {    // ...  }}

extend({ foo: 'bar' }); // OK
extend({ foo: 'bar' }, true); // OK
extend({ foo: 'bar' }, true, 1); // Error
```

