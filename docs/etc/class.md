# class

## 선언

```javascript

class 클래스이름 {
    constructor(name) {
        this.name = name
    }

    메소드() {
        console.log(`Name : ${this.name}`);
    }
}


```



## 인스턴스

* 클래스 선언이 일어나고 , 할당이 이뤄 진다 

```javascript

const  = new 클래스이름(); //Uncaught ReferenceError: Foo is not defined

class 클래스이름 {

}
```



## constructor

* `constructor`는 인스턴스를 생성하고 `Class`의 `property`를 초기화한다.



### Class는 constructor를 반환하며 생략할 수 있다.

**const foo = new Foo()와 같이 선언했을 때 Foo는 Class명이 아닌 constructor다.**

```js
class 클래스이름 {

}

const name = new 클래스이름();

console.log(name == name.prototype.constructor); //true
```

> *  `new`와 함께 호출한 `클래스이름`는 `constructor`와 같음을 확인할 수 있다.
> *  `class `내부에 `constructor`를 선언하지 않았음에도 인스턴스의 생성이 잘 이뤄지는 것을 볼 수 있다.
>   이는 **Class내부에 constructor는 생략할 수 있으며 생략하면 Class에 constructor() {}를 포함한 것과 동일하게 동작**하기 때문이다.
> * 즉, 빈 객체를 생성하기 때문에 `property`를 선언하려면 인스턴스를 생성한 이후, `property`를 동적 할당해야 한다.

```js
console.log(foo); //Foo {}

foo.name = 'BKJang';

console.log(foo); //Foo {name: "BKJang"}
```

### Class의 property는 constructor 내부에서 선언과 초기화가 이뤄진다.

`Class`의 몸체에는 메서드만 선언 가능하며, `property`는 `constructor`내부에서 선언하여야 한다.

```js
class Foo {
    name = ''; //Syntax Error
}
class Bar {
    constructor(name = '') {
        this.name = name;
    }
}

const bar = new Bar('BKJang');
console.log(bar); //Bar {name: "BKJang"}
```

> `constructor`내부에서 선언한 `property`는 `Class`의 인스턴스를 가리키는 `this`에 바인딩 된다.

