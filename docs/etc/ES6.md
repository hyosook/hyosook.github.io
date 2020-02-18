# ES6

## 화살표 함수

``` JS
// 일반적인 자바스크립트 함수(ES5)
function (인자) {
	함수 로직
}

// 화살표 함수(ES6)
(인자) => {
	함수 로직
}
```

* 값을 `this`로 바인딩 하지 않는다 

* 인자값이 1개인 경우, `()`도 필요없다 > 간결

  * 기존

    ```js
    filterBySize() {
      return this.items.filter((item) => {
        return item.size === this.size;
      });
    }
    ```

  * 변경 후

    ```js
    filterBySize() {
      return this.items.filter(item => item.size === this.size);
    }
    ```



## 템플릿 리터럴

> 작은 따옴표(') 나 큰따옴표(") 대신에 ,  백틱(`)  사용을 의미한다 

* 문자열을 여러줄에 걸쳐서 표시  가능

  * 기존

    ```JS
    Vue.component({
      template: '<div>' +
                '<h1></h1>' +
                '<p></p>' +
                '</div>'
    });
    ```

  * ES6

    ```JS
    Vue.component({
      template: `<div>
                  <h1></h1>
                  <p></p>
                </div>`
    });
    ```


* 변수 삽입 표현식 `${}` 문법 활용

  * 기존

    ```JS
    'Hello ' + this.name + ' how are you?'
    ```

  * ES6 , `${}`사용

    ```JS
    `Hello ${this.name} how are you?`
    ```



## 모듈

> 1. 자바스크립트 애플리케이션을 여러 개의 파일로 분할할 수 있습니다.
> 2. 프로젝트 안에서 재사용이 가능한 코드를 생성할 수 있습니다.

```js
// file1.js
export default {
  myVal: 'Hello'
}
```

```js
// file2.js
import obj from './file1.js';
console.log(obj.myVal); // Hello
```

* 컴포넌트 모듈로 사용

  ```js
  // component1.js
  export default {
    // 컴포넌트 내용 정의
  };
  ```

  ```js
  // app.js
  import component1 from './component1.js';
  Vue.component('component1', component1);
  
  ```

## 

## Array Helers

> Functional Programming 트렌트에 따라서 ,
>
>  array 값을 for loop를 통해 가져오는 대신, 여러 array 메서드를 이용하는것을 권장한다 

* forEach

  * 전달받은 함수를 배열의 각각 원소에대해서 실행

    ```javascript
    let fruits = ['apple','banana','peach','blue berry'];
    
    fruits.forEach((fruit) =>
      console.log(fruit);
    });
    ```

* map

  * 각각의 배열 원소들에 대해서 전달받은 함수를  호출하고, 그결과를 모아서 새로운 배열 만든다

  * return 이 없는 함수인 경우, 원래 배열의 원소 갯수만틈 undefined로 채워진 배열 만듬

    * 과일이름이 들어있는 배열에, 각각원소에 `juice`	문자열을 덧붙여 새로운 배열 만듬

      ```javascript
      let fruits = ['apple','banana','peach','blue berry'];
      
      let juice = fruits.map(fruit => `${fruit} juice`);
      ```

    * 객체로 이루어진 배열에서 , 특정 원소의 맴버 변수로만 이루어진 배열을 만든다

      ```javascript
      let comments = [
        { id: 3, content: 'apexsoft' },
        { id: 6, content: '우리모두' },
        { id: 10, content: 'master가 되자' }];
      
      let idx = comments.map( comment => {
          return comment.id;
      });
      ```




* filter

  * 각각 배열의 원소에 대해서 전달받은 함수의 결과가 **true를 반환한 원소들로만 배열**을 만듭니다.

  * 객체 배열에서  어떤 특정한 조건에 맞는 원소들로만 배열을 만들고 싶다 !

    ```javascript
    let datas = [
      { id: 3, type: 'comment', content: 'apexsoft'},
      { id: 6, type: 'post', content: '우리모두' },
      { id: 10, type: 'comment' ,content: 'master가' },
      { id: 6, type: 'post', content: '되나요?' }];
    
    const filteredData = datas.filter( data => {
      return data.type === 'post';
    });
    ```

* find

  * 배열에서 **특정 값을 검색**할 때 

  * find함수로 조건에 만족하는 원소를 반환하지 못하는 경우 **undefined** 를 반환합니다.

  * find함수는 배열 원소에 대해서 주어진 함수연산을 하다가 함수가 true를 반환하면 find함수도 같이 종료됩니다.

    ```javascript
    var ret = datas.find( data => {
    	return data.id === 10;
    });
    ```

* every

  * 주어진 조건을 모두 만족하는지 확인
  * 배열의 모든 원소들이 제공된 함수로 구현된 테스트를 통과하는지 검사

* some

  * 해당 조건을 하나라도  만족하면 참 

* ```javascript
  const users=[
      {nickname:'master'},
      {nickname:'hskim'},
      {nickname:'master'},
  ]
  
  users.find(function(user){
      return user.nickname === 'master'
  }) // 맨처음 찾은 것만 리턴
  
  
  users.every(function(user){
      return user.nickname === 'master'
  }) // false
  
  users.some(function(user){
      return user.nickname === 'master'
  }) // true
  
  
  ```



* fill

  * 모든 배열 요소에 지정된 값으로 교체

    ```javascript
    const arr2 = new Array(2).fill(undefined);
    // [undefined, undefined]
    ```

    ```javascript
    const arr4 = new Array(2).fill('x');
    // ['x', 'x']
    ```

    * 첫 번째 파라미터에 설정할 값을 작성합니다.
    * 두 번째 파라미터는 선택으로 범위 시작 인덱스를 작성하며 디폴트 값은 0입니다. 
    * 세 번째 파라미터는 선택으로 범위 끝 인덱스를 작성하며 디폴트 값은 배열 length 값입니다

    ```javascript
    let one = [1, 2, 3];
    console.log(one.fill(7)); // [7, 7, 7]
    
    let two = [1, 2, 3, 4, 5];
    console.log(two.fill(7, 1)); // [1, 7, 7, 7, 7]
    
    
    
     let three = [1, 2, 3, 4, 5];
    
     console.log(three.fill(7, 1, 3)); // [1, 7, 7, 4, 5]
    
    ```

* reduce

  * 배열의 각 원소에 대해서 첫번째 원소부터 마지막 원소 순으로 연산한 값이 줄도록 함수 적용

    ```javascript
    let scores = [1,2,3,4,5];
    let sum = scores.reduce((sum, number) => sum + number,0);
    ```

    * ` (((( 1 + 2 ) + 3 ) + 4 ) + 5) 순으로 연산`
    * 0은 initial value로서 누산값의 초기값





## Enhancde Object Literals

> object의 property가 key와 value가 동일하다면, 하나로 축약 가능

```javascript
let obj ={x, y }
```







## Rest 매개변수

> Spread 연산자(`...`) 을 사용해서 `매개변수를 정의`하는것 
>
> 인자를 함수 내부에서 배열로 전달 받을수 있다 

* 인자를 함수 내부에서 배열로 전달 받을수 있다

  ```javascript
  function fruit(...rest){
      console.log(rest)   // ["사과","배","딸기","바나나"] 출력
  }
  fruit("사과","배","딸기","바나나")
  ```

* 인자는 입력된 순서대로 기존 매개변수와 Rest 매개변수에 할당 된다

  * 반드시 마지막 매개변수로 지정 되어야한다

  ```javascript
  function fruit(num1, num2,...rest){
      console.log(num1)    // 10 출력 
      console.log(num2)    // 20 출력 
      console.log(rest)   // ["사과","배","딸기","바나나"] 출력
  }
  fruit(10,20"사과","배","딸기","바나나")
  ```



## 변수 선언

> 축약 기법

```javascript
let name, id, type='var'
```



## Spread 연산자

* 함수의 `인자`로 사용하는 경우

  * 배열의 요소를 개별적으로 분리하여 순서대로 매개 변수에 할당 
    * Rest 매개변수는, spread 연산자를 사용하여 매개변수를 정의한것 ! 두개가 다름 

  ```javascript
  function num(num1, num2,num3){
       console.log(num1)    // 10 출력 
       console.log(num2)    // 20 출력 
       console.log(num3)    // 30 출력 
  }
  
  cnost arr =[10,20,30]
  num(...arr);
  ```

  * Rest 매개변수는 반드시 마지막에 작성 되어야 하지만, spread 연산자는 위치 상관 없이 자유롭다

  ```javascript
  function num(num1, num2,num3,num4){
       console.log(num1)    // 10 출력 
       console.log(num2)    // 20 출력 
       console.log(num3)    // 30 출력
       console.log(num4)    // 40 출력 
  }
  
  num(10,...[20,30],40);
  ```

* 배열에서 사용하는 경우 

* 객체에서 사용하는 경우

  *  더 편하게 객체의 값을 복사 가능

  - ```JS
    let myObj = {
      prop1: 'Hello',
      prop2: 'World'
    };
    
    let newObj = {
      name: 'George',
      prop1: myObj.prop1,
      prop2: myObj.prop2
    };
    
    console.log(newObj.prop1); // Hello
    ```

    ```JS
    let newObj = {
      name: 'George',
      ...myObj
    };
    
    console.log(newObj.prop1); // Hello
    ```

  - 기존 

    ```js
    // store.js
    new Vuex.Store({
      state: {
        prop1: ...,
        prop2: ...,
        prop3: ...
      }
    });
    ```

    ```js
    // app.js
    new Vue({
      computed: {
        prop1() {
          return store.state.prop1;
        },
        prop2() {
          return store.state.prop2;
        }
        ...
      }
    });
    ```

  - mapState 사용

    ```js
    import { mapState } from 'vuex';
    
    var state = mapState(['prop1', 'prop2', 'prop3']);
    console.log(state.prop1) // { ... }
    ```

  - `...` 연산자 사용

    ```js
    // app.js
    import { mapState } from 'vuex';
    
    new Vue({
      computed: {
        someLocalComputedProp() { ... },
        ...mapState(['prop1', 'prop2', 'prop3'])
      }
    });
    ```



## for

### foreach

```javascript
let arr = ['arr1', 'arr2', 'arr3'];

arr.forEach(function(item) {
    console.log(item);
});
// 출력 결과: arr1, arr2, arr3
```



### for-in

```javascript
var obj = {
    a: 1, 
    b: 2, 
    c: 3
};

for (var key in obj) {
    console.log(key, obj[key]); // a 1, b 2, c 3
}
```



* 객체의 속성 
* 인텍스가 문자로 반환 
* 루프 순서가 무작위 

### for-of

```javascript
var iterable = [10, 20, 30];

for (var value of iterable) {
  console.log(value); // 10, 20, 30
}
```



* 객체의 요소 (data)
* break , continue , return 가능 





## Object

### **Object.keys()**

> 개체 고유 속성의 이름을 배열로 반환

```java
const object1 = {
  a: 'somestring',
  b: 42,
  c: false
};

console.log(Object.keys(object1)); //  Array ["a", "b", "c"]

```









````javascript
const object = {a:2, b:4, c:6, d:8};



for (const [key, value] of Object.entries(object)) {
    console.log(`${key}: ${value}`);
}


Object.entries(object).forEach(([key, value], index) => {
  console.log(`${index}: ${key} = ${value}`);
});





Object.values(object).forEach((value, index)=>  {
    console.log(`${index}: ${value}`);
})
````



## 묵시적 반환

* **한줄**로만 작성한 작성한 애로우 함수 는 별도 `return`  명시 없이도 자동 반환 됨

  ```javascript
  함수명 = 인자 => ( 리던값 )
  ```

  * 중괄호 `{ }` 없어야 한다



## 파라미터 기본값 지정

* 함수 선언문자체에 기본값 지정 가능

  ```javascript
  함수명 =  (name , id=1 , type='var') => ( id+name+type)
  ```

  



##  여러줄 문자열

```javascript
let 약관동의 = `약관동의 내요 
			    내요 내요 내요 
				내용 내용 내용`
```

