# vue cli3에서 IE11지원

* bable/polyfill 설치

 * npm install --save @babel/polyfill 
 * yarn install

* src/main.js에  `babel/polyfill`  import

```js
import '@babel/polyfill'
import Vue from "vue"
......
```
* babel.config.js 수정

```js
module.exports = {
  presets: [
    ['@vue/app', {
      'useBuiltIns': 'entry',

      polyfills: [          
        'es6.promise',     // IE11에서 promise 패턴 사용 경우 추가
        'es6.symbol'
      ]
    }]
  ]
}
```

