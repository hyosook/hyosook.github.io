---
title : v-cloak
tags: ["vue.js"] 
---



## v-cloak

실행될때, 머스태쉬 태그들이 짧은 순간 동안 깜박거리는 현상이 존재. `{{ element }}`

자바스크립트 코드가 실행되기 전이기 때문에. 

`v-cloak` 을 사용하면 html 코드를 숨길 수 있다.



```html html
<div id="app" v-cloak>
  ..
</div>
```



```css  css 
[v-cloak] {  
  display: none;
}
```

