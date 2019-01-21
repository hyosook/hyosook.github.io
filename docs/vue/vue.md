---
title : vue.js 기본
tags : ["vue.js"]

---



## JSBin

- 브라우저에서 JavaScript, CSS, HTML 코드를 작성 하여 실시간으로 결과를 확인 할 수 있는 서비스
- https://jsbin.com/fivomus/edit?html,output



## MVVM  ( Model-View-ViewModel )



![mvvm-of-vue](http://blog.jeonghwan.net/assets/imgs/2017/03/27/mvvm-of-vue.png)

- 웹페이지는 돔(사람들이 눈으로 보는 페이지 ) 과 자바스크립트의 연합으로 만들어지게 되는데 

- 돔이 View 역할을 하고, 자바스크립트가 Model 역할을 한다. 

  ​

- 뷰모델이 없는 아키텍처에서는 getElementById 같은 돔 API를 직접 이용해서 모델과 뷰를 연결해야 한다. 

- 자바스크립트에서 컨트롤러 혹은 프리젠터 역할까지 하면서 코드양이 증가하는 단점이 생기기게 되는데 제이쿼리를 이용해 돔에 데이터 뿌려주는 코드들이 대부분 그랬다.



- view와 model 그 사이에서
- View와는 바인딩이나 커멘드로 연결하고 
- Model과는 Data를 주고 받는 역할을 담당

- 뷰모델이 대신 수행해 주는 것이 MVVM 모델이다. 
- 뷰모델에 자바스크립트 객체와 돔을 연결해 주면 뷰모델은 이 둘간의 동기화를 자동으로 처리한다. 이것이 뷰js가 하는 역할이다.
- MVC 패턴에서 컨트롤러 역할처럼 데이터를 관리하고 액션을 처리한다.





## 뷰모델 View Model

- MVVM 모델의 VM을 뷰js가 담당한다.
- MVC 패턴에서 컨트롤러 역할처럼 데이터를 관리하고 액션을 처리한다.



 `Vue` 생성자 함수의 인스턴스를 생성하면서 뷰 모델을 다루게 된다. 

아래 예제와 같이 인스턴스를 생성할 때 뷰와 데이터를 연결하기 위한 옵션을 설정할 수 있다.

- 뷰 관련 옵션 : `el`, `template`
- 데이터 관련 옵션 : `data`, `methods`, `computed`
- 컴포넌트 관련 옵션 : `components`
- 생명 주기 훅 : `created`, `mounted`, `updated`, `destroyed`

### 데이터 관련 옵션

### computed( 선언적 의존 관계)

- computed 라는 의미는 진짜 말그대로 "계산된" 으로 해석이 됩니다. 

- 선언적으로 의존 관계를 만들었다는 것입니다. 

- 기본적으로 getter만 가지고 있지만, 필요한 경우 setter를 제공할 수 있습니다.

  #### 기본 (getter 사용)

  ```javascript vue.js
  var vm = new Vue({
    el: '#demo',
    data: {
      firstName: 'Foo',
      lastName: 'Bar'
    },
    computed: {
      fullName: function () {
        return this.firstName + ' ' + this.lastName
      }
    }
  })
  ```

  #### computed (계산된) setter

  - computed는 기본적으로 getter만 가지고 있지만 필요한 경우 setter를 제공함

  ```javascript vue.js
  computed: {
    fullName: {
      // getter
      get: function () {
        return this.firstName + ' ' + this.lastName
      },
      // setter
      set: function (newValue) {
        var names = newValue.split(' ')
        this.firstName = names[0]
        this.lastName = names[names.length - 1]
      }
    }
  }
  ```

  처음 fullname이 시작될 떄는 get부분의 코드가 작동된다는 뜻이며, 

  fullname이란 값이 변한다면 set에 있는 함수가 작동된다는 것이다.

- 사용

  ```html html
  <div id="product-list-bottom">
                  <div v-if="noMoreItems">No more items</div>
              </div>
  
  computed: {
          noMoreItems: function() {
              return this.items.length == this.results.length && this.results.length > 0
          }
      }
  ```

  ​

### 감시자(watch)



- 대부분은 computed가 더 적합하지만 일부는  watch 가 필요한 경우가 있음 
- 데이터 변경에 대한 응답으로 **비동기식** 또는 **시간이 많이 소요되는 조작을 수행할 때** 사용



- 우리나라말로 해석하면 감시자와 같습니다. 값의 변화를 주시하는 역할

  > 우리가 알고있는 input text 에 이벤트로 onkeyup 을 넣으면 값이 들어오는걸 주시하고 있죠. 
  >
  > onfocus 도 포커싱이 들어오면 바로 반응을 합니다. 이렇든 개발자들에게 감시자라는 단어는 매우 친숙하게 다가옵니다.

  ​

- 우선 이 기능은 미리 data 안에 선언해주어야 합니다. 우리가 전역변수를 만들듯이 data , state , props 개념으로 사용되죠.
- n또한 v-model 과 함께 사용됩니다. 우리가 배운 **v-model** 은 양방향 바인딩이라고해서 클라이언트 측에서 데이터를 보내면 Script 단에 바로 받아 데이터를 수정해버립니다
- v-model로 양방향 바인딩을 합니다. 즉 always 에 변화가 있을때 watch가 그걸 알아 차리고 그에 맞게 선언된 always 에 function 을 작동시킵니다.



```javascript vue.js
<div id="app">
    <input type="text" v-model='always'/>
    <div>{{ing}}</div>
</div>


var demo = new Vue({
    el : "#app",
    data : {
        always : '',
        ing : "입력을 기다리는 중입니다."
    },
    watch : {
        always : function(hook){
        console.log(hook)
            this.ing = "입력중......"
            this.fail(this.always);
        }
    },
    methods : {
        fail : function(text){
            if(text.length < 5){
                this.ing = '5글자 이상이어야 합니다.';
            }   
        }
    }
})
```



#### 계산된 캐싱(Computed Caching) vs 메소드(Methods)

- 둘다 vue.js 안에서 함수를 정의하는 부분

  ​

- 둘의 차이점은 데이터가 변동되지 않는다고 가정했을때에 드러난다.

> - computed 
>
> 데이터 변동이 없는 상태에서 computed는 이전의 계산된 값을 캐시해 두었다가 함수 호출시 다시 쓰게 된다.
>
> 종속성 중 일부가 변경된 경우에만 다시 계산 
>
>
>
> - methods
>
> 사용될 때마다 함수의 계산을 다시 하게 된다.

- 종속성에 따라 캐시된다



- 만약 데이터가 자주 변동되지 않는다면, 비용적인 면에서는 computed가 좋다고 할 수 있고,
- 데이터가 수시로 업데이트 된다면 계속해서 캐시를 저장하는 computed보다는 methods가 더 좋을 수 있다.



- 그래서인지 html 상에서 computed는 마치 변수처럼 쓰이고, methods는 함수처럼 쓰인다.

> 예를들어 똑같은 parse라는 함수가 있다면 computed는 parse를 그대로 쓰고 
>
> methods는 parse()라고 쓰게 된다.



### 계산된 속성(Computed) vs 감시된 속성(Watched)

```html html
<div id="demo"> {{ fullName }} </div>
```

```javascript vue.js
var vm = new Vue({
  el: '#demo',
  data: {
    firstName: 'Foo',
    lastName: 'Bar',
    fullName: 'Foo Bar'
  },
  watch: {
    firstName: function (val) {
      this.fullName = val + ' ' + this.lastName
    },
    lastName: function (val) {
      this.fullName = this.firstName + ' ' + val
    }
  }
})
```

위의 코드를 보면  firstName 과  lastName 의 코드가 중복되므로 효율적이지 못함 
이때는 computed를 이용한다면 아래와 같이 한줄로 작성 가능해짐

```javascript vue.js
var vm = new Vue({
  el: '#demo',
  data: {
    firstName: 'Foo',
    lastName: 'Bar'
  },
  computed: {
    fullName: function () {
      return this.firstName + ' ' + this.lastName
    }
  }
})
```





### Template

**사용하기 편하고 반복적으로 사용할 수 있는 플랫폼**

### Component

템플릿은 뭔가 추상적인 개념이라고 한다면 컴포넌트는 이를 구현할 기술이라고 생각해보죠.

독립적인 JS 하나당 component 하나를 만들어서 기능을 

줘서 반복사용을 통해 활용성을 높이고 유지보수도 간편하게 할 수 있습니다.

text 나 값은 props 나 data로 바꾸면 되니까요. 

컴포넌트는 Vue의 가장 강력한 기능 중 하나입니다. 기본 HTML 엘리먼트를 확장하여 재사용 가능한 코드를 캡슐화하는 데 도움이 됩니다. 상위 수준에서 컴포넌트는 Vue의 컴파일러에 의해 동작이 추가된 사용자 지정 엘리먼트입니다. 경우에 따라 특별한 `is` 속성으로 확장 된 원시 HTML 엘리먼트로 나타날 수도 있습니다.



- 새 vue 인스턴스 만들기 (root 인스턴스 생성)

  ```javascript vue.js
  // 루트 인스턴스 생성
  new Vue({
    el: '#example'
  })
  ```

  ​

- 전역 컴포넌트 등록

  ```javascript vue.js
  // 등록
  Vue.component('my-component', {
    template: '<div>사용자 정의 컴포넌트 입니다!</div>'
  })
  ```

- 컴포넌트는 인스턴스의 템플릿에서 커스텀 엘리먼트,`<my-component></my-component>`로 사용할 수 있습니다. 루트 Vue 인스턴스를 인스턴스화하기 **전에** 컴포넌트가 등록되어 있는지 확인하십시오. 

  ```html html
  <div id="example">
    <my-component></my-component>
  </div>
  ```

모든 컴포넌트를 전역으로 등록 할 필요는 없습니다. 컴포넌트를 `components` 인스턴스 옵션으로 등록함으로써 다른 인스턴스/컴포넌트의 범위에서만 사용할 수있는 컴포넌트를 만들 수 있습니다:

```javascript vue.js
var Child = {
  template: '<div>사용자 정의 컴포넌트 입니다!</div>'
}
new Vue({
  // ...
  components: {
    // <my-component> 는 상위 템플릿에서만 사용할 수 있습니다.
    'my-component': Child
  }
})
```



`data` 는 반드시 함수여야합니다.

Vue 생성자에 사용할 수 있는 대부분의 옵션은 컴포넌트에서 사용할 수 있습니다. 

한가지 특별한 경우가 있습니다. `data` 는 함수여야 합니다.

[컴포넌트 작성](https://kr.vuejs.org/v2/guide/components.html#컴포넌트-작성)컴포넌트는 부모-자식 관계에서 가장 일반적으로 함께 사용하기 위한 것입니다. 

컴포넌트 A는 자체 템플릿에서 컴포넌트 B를 사용할 수 있습니다. 

그들은 필연적으로 서로 의사 소통이 필요합니다.

 부모는 자식에게 데이터를 전달해야 할 수도 있으며, 자식은 자신에게 일어난 일을 부모에게 알릴 필요가 있습니다. 그러나 부모와 자식이 명확하게 정의된 인터페이스를 통해 가능한한 분리된 상태로 유지하는 것도 매우 중요합니다. 이렇게하면 각 컴포넌트의 코드를 상대적으로 격리 할 수 있도록 작성하고 추론할 수 있으므로 유지 관리가 쉽고 잠재적으로 쉽게 재사용 할 수 있습니다.

Vue.js에서 부모-자식 컴포넌트 관계는 **props는 아래로, events 위로** 라고 요약 할 수 있습니다. 

부모는 **props**를 통해 자식에게 데이터를 전달하고 

자식은 **events**를 통해 부모에게 메시지를 보냅니다



`v-bind`를 사용하여 부모의 데이터에 props를 동적으로 바인딩 할 수 있음
데이터가 상위에서 업데이트 될 때마다 하위 데이터로도 전달된다.

```html html
<div>
    <input v-model="parentMsg">
    <br>
    <child v-bind:my-message="parentMsg"></child>
</div>
```

위와같이 코드를 작성하면 입력창에 입력하면 아랫줄에 같은 내용이 실시간으로 바인딩된다.

## 디렉티브 (Directive)

- 엘리먼트에게 **이러이러하게 작동해라!** 하고 지시를 해주는 지시문

- vue 엘리먼트에서 사용디는 특별한 속성

- 디렉티브는 Vue 의 기능들을 사용하기 위해서 사용하는, HTML 태그 안에 들어가는 하나의 속성

- `v-text` 이런 식으로 `v-` prefix 를 지니고 있습니다.

  ​

- HTML 태그 안의 내용을 Vue 인스턴스 안의 데이터값으로 설정 할수 있음

- 머스태쉬 태그나, 디렉티브를 사용 할 때, 그 내부의 값을 꼭 데이터 명으로 해야 하는건 아니다.

  > **자바스크립트 표현식**을 사용 할 수도 있습니다.
  >
  > ```html html
  > <h1>{{ Date() }}</h1>
  > ```

  ​

  ### 머스태쉬 태그    

  - 내부의 값으로  **데이터 명** ,  **자바스크립트 표현식** 을 사용 할수 있다.

    ```html html
    /* Date 함수 사용 */
    <h1> {{ Date() }} </h1>
    ```

    ```html 
    /* 자바스크립트 표현식 3항 연산자 사용 */ 
    <div id="app">
        
        <img :src="smile ? feelsgood : feelsbad"/>
        
        <img v-bind:src="smile ? feelsgood : feelsbad"></img>
        
      </div>
    
    var app = new Vue({
    
      data: {
        smile: true,
        feelsgood: 'https://imgh.us/feelsgood_1.jpg',
        feelsbad: 'http://imgh.us/feelsbad.jpg'
        
      }   
    });
    ```

    ```javascript vue.js
     /* 함수 사용 */
     <div id="app">
        <h1> {{ myfunction() }} </h1>
        
      </div>
      
      
      var app = new Vue({
      el: '#app', 
     data : {
       funvalue : '함수 값'
     },
      methods: {
        myfunction: function() {
        
          // 인스턴스 내부의 데이터모델에 접근 할 땐,
          // this 를 사용한다.
          return this.funvalue
        }
      }
    });
      
      
    ```

    ​

  ​

  ### 1. v-text 디렉티브(결과 머스태쉬 태그 와 같다)

   `v-text` 라는 디렉티브를 사용

  ```html vue.js
   <h1> {{number}}</h1>
   		||
   <h1  v-text="number"> </h1>
  ```

  - vue 엘리먼트의 name 변수로 설정 되게 한다

  ​

  ### 2. v-html 디렉티브

  **“여기서 렌더링 할 건 html 형식이야”** 라는걸 지정하기 위해서 `v-html` 이라는 디렉티브를 사용합니다.

  ​

  ```html html/vue.js
    <div id="app">
      
      <h1 v-html="nameItaly"></h1>
    </div>
    
    var app = new Vue({
    el: '#app', 
    data: {
      nameItaly:'<i>기울어짐</i>'
    }
  });
  ```

  ​

  ### 3. v-show 디렉티브

  해당 엘리먼트가 보여질지, 보여지지 않을지 `true` / `false` 값으로 지정 할 수 있습니다. 

  ```html html/vue.js
   <h1 v-show="show" v-html="nameItaly"></h1>
      
  var app = new Vue({
    el: '#app', 
    data: {
      nameItaly:'<i>기울어짐</i>',
      show:false
    }
  });
      
     
  ```

  ​

  ### 4. v-if 디렉티브

   변수명 대신에 조건문을 씁니다 (변수명을 입력해도 작동하긴 합니다)

  ### 5. v-else 디렉티브

  `v-if` 디렉티브를 사용 했을때 그 아래에 `v-else` 디렉티브를 사용하는 엘리먼트를 넣어주면, 윗부분의 조건문이 만족하지 않을때 보여진답니다.

  `v-if` 바로 아래에 `v-else` 디렉티브를 사용했죠? 이 디렉티브의 값은 따로 설정해주지 않아도 됩니다.

  ### 6. v-else-if 디렉티브

  `v-else-if` 는 첫번째 조건문의 값이 참이 아닐 때, 다른 조건문을 체크하여 다른 결과물을 보여줄 수 있게 해줍니다.

  `v-else-if` 는, 언제나 `v-if` 디렉티브를 사용하는 엘리먼트의 **다음위치**에 있어야 합니다. 만약에 `v-else` 디렉티브가 사용되는 경우엔 그 **사이에** 위치해있어야 하구요, 이 디렉티브를 **여러번 사용**해도 됩니다.

  ```html html
   <div id="app">
      <h1 v-if="value > 5">value 가 5보다 크군요</h1>
      <h1 v-else-if="value === 5">값이 5네요</h1>
      <h1 v-else>value 가  5보다 작아요</h1>
    </div>
  ```

  ​

  ### 7. v-pre 디렉티브

  특정 엘리먼트를 **무시**하는데에 사용 됩니다. 

  이걸 사용하므로서, Vue 시스템에서 해당 엘리먼트는 지시문이 없다는걸 인식하게 되어 그 엘리먼트 내부의 자식엘리먼트들을 신경쓰지 않고 그냥 **건너뜁니다**

  ```html html
   <h1 v-pre> {{ 이건 그대로 렌더링해줘요 }} </h1>
  ```

### 9. v-once 디렉티브

  컴포넌트를 초기에 **딱 한번만** 렌더링합니다. 

  초기 값은 그대로 **고정**이 되어있고 현재 값은 그때 그떄 업데이트 되죠?

```html html
     <h2 v-once> value초기 값 보여준다 : {{ value }}</h2>
     <h2>value 현재 값 보여준다: {{ value }}</h2>
```

  ​

### v-for 디렉티브   

- HTML 에서 **for-loop** 을 구현하기 위하여 사용됩니다. 즉, **비슷한 내용을 반복적으로 보여줄 때 **사용

- ```html html
  v-for "(arr , index ) in arrs"  {{ arr.value}}
  ```

- `item in items` 의 형식으로 작성합니다.

  > `items` 는 Vue 엘리먼트의 데이터 안에 들어있는 배열 이름 
  >
  > `item` 은 렌더링 하게 될 때, 각 원소를 가르키는 별침(alias)

```html html/vue.js
<div id="app">
    <ul>
      <li v-for="item  in todos ">
      {{ item .text }}
      </li>
    </ul>
  </div>
  
  var app = new Vue({
  el: '#app', 
  data: {
    todos: [
      { text: 'Vue.js 튜토리얼 작성하기' },
      { text: 'Webpack2 알아보기' },
      { text: '사이드 프로젝트 진행하기' }
    ]
  }   
});
```



- #### index 값 받아오기

  렌더링을 할 때, 각 원소들을 순서번호(index) 를 가져오려면, 디렉티브 값에 `(todo, index) in todos` 형식으로 작성을 하면 됩니다.

```html html/vue.js
    <div class="product" v-for="(item, index) in items">
  
      <img v-bind:src="item.productImage">
        
     <span v-on:click="onDetail(index)">  {{ item.productName }} </span>
        
        </div>
```

```html html/vue.js
  <div id='test'>
    
    <div v-for="(item,index) in arr">
      index :{{index}}/  
      value : 
      <button @click="fncClick1(index)">{{item}}</button>
    </div>
    
  new Vue({
  el : '#test',
  data :{
    arr:['a','b']
  },
  methods:{
    fncClick1 : function(index){
     
      return alert (this.arr[index])
    }
  }
})  

```

## v-bind 디렉티브 

- **HTML 태그의 속성 값**을 데이터값을 사용해야 한다면 어떻게 해야할까요?

- HTML 엘리먼트에서 `src` 값을 Vue 엘리먼트의 데이터 중 image 로 설정하고 싶다

- `<img v-bind:src="image"/>` 와 같은 형식으로 하면 됩니다.

- `v-bind:` 뒤에 속성의 이름 / 편의를 위해서, `v-bind` 를 생략 할 수 있습니다. 그냥 **콜론 뒤에 속성의 이름**만 넣어주면 돼요. 

  ​

```html html/vue.js
    <div id="app">
      
        <img v-bind:src="addr"/>
        <img :src="addr"/>
      
      </div>
      
      var app = new Vue({
      el: '#app', 
      
      data: {
        addr: 'https://imgh.us/feelsgood_1.jpg'
      }   
    });
      
```

  ​

## v-model , 양 방향 데이터 바인딩(폼 관련 태크만 사용가능 )

- 뷰 ⇄  데이터 **형태로 바인딩하여 데이터가 **양 방향 으로 흐르게 해주는 것 입니다. 

- 데이터에 있는 값이 뷰에 나타나고, 이 뷰의 값이 바뀌면 데이터의 값도 바뀌는것이죠.

- v-model 을 설정 함으로서 , 이 input 엘리먼트의 값이 업데이트 되면  자동으로 바뀐다

  > 이렇게 폼에 관련된 태그에만 사용 될 수 있습니다: `<input>` `<select>` `<textarea>`

  ​



```html html/vue.js
   <div id="app">
    <h1>{{ feels }}</h1>
    <h3><input type="text" v-model="feels"/>개구리</h3>
    <img v-bind:src="feels=='웃어요' ? feelsgood : feelsbad"/>
  </div>
  
  
// 새로운 뷰를 정의합니다
var app = new Vue({
  el: '#app', // 어떤 엘리먼트에 적용을 할 지 정합니다
  // data 는 해당 뷰에서 사용할 정보를 지닙니다
  data: {
    feels:'울어요',
    feelsgood: 'https://imgh.us/feelsgood_1.jpg',
    feelsbad: 'http://imgh.us/feelsbad.jpg'
  }   
});
```

https://kr.vuejs.org/v2/guide/forms.html



### v- on 디렉티브 ,이벤트 핸들링 @

- `v-on` 디렉티브를 사용하여 DOM 이벤트를 듣고 트리거 될 때 JavaScript를 실행할 수 있습니다.

  > 메소드(함수)
  >
  > 자바스트립트 표현식 ... 

  ​

- 많은 이벤트 핸들러의 로직은 더 복잡할 것이므로, JavaScript를 `v-on` 속성 값으로 보관하는 것은 간단하지 않습니다. 이 때문에 `v-on`이 호출하고자 하는 메소드의 이름을 받는 이유입니다.

  ​

- 메소드를 준비할때는, 우리가 뷰 인스턴스에서 사용 할 데이터들을 `data` 안에 넣은 것 처럼, 함수들을 만들어서 뷰 인스턴스의 `methods` 안에 넣으면 됩니다.

  ​

- **v-on: 이벤트이름  =  "메소드이름" **

- `v-on:` 을 `@` 로 대체 가능 

  ​

```html html/vue.js
<div id="app">
  <h1>카운터: {{ number }}</h1>
 <button v-on:click="increment">증가</button>
  <button @click="decrement">감소</button>
</div>

var app = new Vue({
  el: '#app', 
  data: {
    number: 0
  },
  // app 뷰 인스턴스를 위한 메소드들
  methods: {
    increment: function() {
      // 인스턴스 내부의 데이터모델에 접근 할 땐,
      // this 를 사용한다
      this.number++;
    },
    decrement: function() {
      this.number--;
    }
  }
});
```

```html html/vue.js
<div id="example-1">
  <button v-on:click="counter += 1">Add 1</button>
  <p>위 버튼을 클릭한 횟수는 {{ counter }} 번 입니다.</p>
</div>

var example1 = new Vue({
  el: '#example-1',
  data: {
    counter: 0
  }
})
```

- 메소드 이름을 직접 바인딩 하는 대신 인라인 JavaScript 구문에 메소드를 사용할 수도 있습니다.





