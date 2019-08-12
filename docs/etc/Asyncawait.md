## Async/await

- asnyc/await 는 비동기 코드를 작성하는 새로운 방법이다.

  



### 사용

1. 함수의 앞에 `async` 라는 단어가 오게된다.

   `await` 키워드는 오직 `async`로 정의된 함수의 내부에서만 사용될 수 있다. 



```javascript
let name = await 변수
```

* Promise 아닌경우 (일반 문자열, 숫자...) = 값 자체 반환

* Promise 인 경우 :  기다렸다가   **resolve(fulfill)**되면 결과값이 왼쪽 변수에 대입된다

  * 만약 reject 되었다면,  변수에 결과가 저장되지 않고  해당 오류를 그자리에서 **throw** 

  

```javascript

let hello = (name)=> new Promise((resolve,reject)=> {
   if(name) resolve(name+'님, 이건 정상')
        else reject('비정상 이름이 없슈')
});


const asyncHello = async (name) => {
 		let result = await hello(name)
        console.log(result)
    return 'asyncHello 종료' //promise 형태로 반환 한다
};

asyncHello('hs').then((result)=>{
    console.log(result) // 비동기 끝을 알리는 형태로 사용하면 good
})

```

*  `async` 키워드가 붙어있는 함수를 호출하면 명시적으로 Promise 객체를 생성하여 리턴하지 않아도 Promise 객체가 리턴됩니다.

  






## 에러핸들링

async/await는 동기와 비동기 에러 모두를 `try/catch`를 통해서 처리할 수 있게 한다.







