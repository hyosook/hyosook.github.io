---
title : 확장
tags : ["pug","vue.js","extends"]
---



# pug

> templete 확장을 가능하게 하는 언어



# extend

> 컴포넌트 확장



### 예제 기본 구조

![](https://d33wubrfki0l68.cloudfront.net/23f398a1c554aa3937aa806cf82fbbe77fc1aa9d/27529/images/posts/extending_components_2.png)





### SurveyInputBase.pug

* .pub 파일 
* html에서 닫는 태그가 없다
* 들여쓰기한 이후에 공백까지 태그로 된다
* 태그 속성은  괄호( ) 사용

```javascript SurveyInputBase.pug
form
  div(class="form-group")
    label(for="title" class="form-label") Title
    textarea(v-model.lazy="element.title" class="form-control" name="title")
  block custom
```



### SurveyInputBase.vue (기본)

```html DialogFormBase.vue
<template lang="pug">
  include SurveyInputBase.pug
</template>
<script>
  export default {
    props: [ 'question' ]
  }
</script>
```



### SurveyInputText.vue (확장)

```html SurveyInputText.vue
<template lang="pug">
  extends SurveyInputBase.pug
  block input
    input(type="text" :placeholder="placeholder")
</template>
<script>
  import SurveyInputBase from './SurveyInputBase.vue';
  export default {
    extends: SurveyInputBase,
    props: [ 'placeholder' ],
  }
</script>
```



## Mixins

>  - vue 구성요소에 재사용 가능한 기능을 넣는 방법
>  - 재사용 가능한 기능은 기존 기능과 병합 
>  - 캡슐화 및 공통된 기능을 분리시켜 코드 재사용성을 높혀준다.
>  - 오버라이딩 기능도 사용할 수 있어 커스텀 및 확장에 용이하다.
>  - [참고 동영상](https://www.youtube.com/watch?v=B98iyg_brxQ&feature=youtu.be)
>  - [공식문서](https://kr.vuejs.org/v2/guide/mixins.html)



- extends: 유사한 기능의 컴포넌트들을 추상화 하여 상위 컴포넌트를 만들고 차이가 있는 기능들을 하위 컴포넌트에 구현한다.
- mixins: 서로 다른 기능의 컴포넌트에 동일한 기능을 배포하는 방법으로 예를 들면 로깅 기능을 aspect 단위로 추가하는 것을 들 수 있다.





참고 url https://vuejsdevelopers.com/2017/06/11/vue-js-extending-components/