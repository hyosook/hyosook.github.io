---
title : promise
tags : ["ES6","javascript"]
---




## promise

* 비동기 동작의 흐름을 제어할 때 사용
* JavaScript ES6에 추가
* 아직은 아니지만 나중에 완료될 것으로 기대 되는 연산을 의미
* async(비동기)연산에서 사용
* Promise object는 new를 통해 initiate될 수 있으며, 함수를 패스받을 수 있다.
* 이 함수에는 resolve, reject함수가 매개변수로 차례로 들어가있다.



```javascript javascript
function task(resolve, reject) {
    console.log('task start');
    setTimeout(function() {
        console.log('task end');
        resolve('task success');
    }, 300);
}

function resolveed(result) {
    console.log('resolve : '+result);
}

function rejected(err) {
    console.log('rejected : ', err);
}

new Promise(task).then(resolveed, rejected);
```

* promise 이후의 동작 : then
  * 성공과 실패일 때 각각 콜백처리 한다



### resolve

* promise가 성공적으로 종료될 때 promise의 뒤에 따라오는 then의 콜백에 arg로 사용될 value를 넘겨받을 수 있다.

###  reject

*  promise에서 에러가 발견된 경우, 혹은 조건에 맞지 않아 에러를 발생시켜야 하는 경우 호출
*  promise의 메서드인 catch안에 들어간다. 
*  보통 resolve는 error의 이유가 들어간다



```javascript 
let func = function(resolve){
  resolve();
};
let promise1 =new Promise(func);
promise1.then(()=>alert('resolve1'));

let promise =new Promise((resolve,reject)=>{
   reject();
 });
promise.then(()=>alert('resolve'));

promise.catch(()=>alert('reject'));
```





https://jsbin.com/nusaxemero/2/edit?html,js,console,output