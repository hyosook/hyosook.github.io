# 타입스크립트 x 뷰

https://avengersrhydon1121.tistory.com/251?category=834822

https://codingcoding.tistory.com/1254

http://ccambo.github.io/Dev/Vue/6.How-to-use-vue-property-decorator/

 ##  클래스 기반 컴포넌트 (Class base Component)

### 개념

#### `@Component`

> 정의된 클래스를 Vue가 인식할수 있는 형태로 변환

```html
<script>
export default{
    name: 'myComponent'
}
</script>
```

```html
<script lang="ts">
import {Component, Vue} from 'vue-property-decorator'; 
    
@Component  
export default class myComponent extends Vue { 
}
</script>
```

#### Data

> 클래스 맴버로 정의해서 사용한다 

```html
<script>
export default{
    name: 'myComponent',
    data(){
        return {
            name:'hyosook'
        }
    }
}
</script>
```

```html
<script lang="ts">
import {Component, Vue} from 'vue-property-decorator'; 
    
@Component  
export default class myComponent extends Vue { 
    name ='hyosook'
}
</script>
```

#### computed

> 클래스 getter로 정의하여 사용

```html
<script>
export default{
    name: 'myComponent',
    data(){
        return {
            name:'hyosook'
        }
    },
    computed:{
        myname(){
            return this.name+'입니다'
        }
    }
}
</script>
```

```html
<script lang="ts">
import {Component, Vue} from 'vue-property-decorator'; 
    
@Component  
export default class myComponent extends Vue { 
    name ='hyosook'
    
    get myname(){
        return this.name+'입니다'
    }
}
</script>
```



#### 메소드

```html
<script>
export default{
    name: 'myComponent',
  
    methods:{
        onSaveBtn(){
            alert('버튼클릭')
        }
    }
}
</script>
```

```html
<script lang="ts">
import {Component, Vue} from 'vue-property-decorator'; 
    
@Component  
export default class myComponent extends Vue { 
   
   onSaveBtn(){
        alert('버튼클릭')
    }
}
</script>
```

#### 라이프 사이클 

> 메소드와, 라이프 사이클 같은 공간에서 정의 된다 !!

```html
<script>
export default{
    name: 'myComponent',
  
    beforeDestory(){
        
    }
}
</script>
```

```html
<script lang="ts">
import {Component, Vue} from 'vue-property-decorator'; 
    
@Component  
export default class myComponent extends Vue { 
   
   beforeDestory(){
       
    }
}
</script>
```

### `@Component`인수

#### Component , mixins

```html
<script>
export default{
    name: 'myComponent',
  
   components:{apexButton,modal},
    mixins:[pageMxiin]
}
</script>
```

```html
<script lang="ts">
import {Component, Vue} from 'vue-property-decorator'; 
    
@Component({
    components:{apexButton,modal},
    mixins:[pageMxiin] 
})  
    
export default class myComponent extends Vue { 
  
}
</script>
```



### `@Prop`

```html
<script>
export default{
   props:{
       name:{
           type:String,
           default:'hysook'
       
       },
       isShow:{
          type:Boolean,
           required:true
       }
   }
}
</script>
```

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
```



### `@watch`

> `immediate:true` 컴포넌트 초기화 시에도 실행할지 여부

```html
<script lang="ts">
import {Component, Vue} from 'vue-property-decorator'; 
    
@Component
export default class myComponent extends Vue { 
     name ='hyosook'
    
    @Watch('name',{immediate:true})
    onChangeName(){
        //바뀌었을때 처리
    }
    
}
</script>
```







### 만들기 

1. template, script, style 부분에서 script에 반드시 lang="ts"를 명시

2. `import {Component, Vue} from 'vue-property-decorator';`
3. `@Component`  데코레이터 명시
4.  클래스 이름명시 와 뷰 상속받기

```html
<template>
    <div>
        <input type="text" v-model="message">
        <div>{{message}}</div>
    </div>
</template>

<script lang="ts"> # 1. ts명시
import {Component, Vue} from 'vue-property-decorator';  #  2. import

@Component  #  3. Component 데코레이터 명시
export default class Message extends Vue { # 4. 클래스 이름과 뷰 상속받기

   message: string = '메세지를 입력해주세요';

}
</script>

<style>

</style>
```

 ### prop

* 자식 

```html
<template> 
    <div> {{parentMessage}} </div> 
</template> 

<script lang="ts"> 
import {Component, Prop, Vue} from 'vue-property-decorator'; 
    
@Component 
export default class Children extends Vue {
    
    @Prop() parentMessage?: string; 
} 
</script> 
```

* 부모

```html
<template> 
    <div class="home"> 
        <img alt="Vue logo" src="../assets/logo.png"> 
        <message></message> 
        <children :parentMessage="message"></children> 
    </div> 
</template> 

<script lang="ts"> // @ is an alias to /src 
import {Component, Vue} from 'vue-property-decorator'; 
import message from '@/components/message.vue'; 
import children from '@/components/children.vue'; 
    
 @Component({ 
     components: { 
         message, children, 
     }, 
 }) 

 export default class Home extends Vue { 
     message: string = 'hello world'; 
    }
</script>

```













### vuex

https://avengersrhydon1121.tistory.com/251