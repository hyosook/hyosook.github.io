---
title : 컴포넌트와 플러그인
tags : ["vue.js", "component", "plugin"]
---



## Component

* 기본 `html` 엘리먼트를 확장하여 재사용 가능한 코드 단위로 묶음
* 컴포넌트를 **지역/전역**적으로 사용할 수 있는 소스코드가 들어있음 
  * 일반적인 외부 컴포넌트들이 사용가능한 소스코드를 제공하는 것으로 보임
  * 외부 컴포넌트의 경우, `webpack`을 사용해 `bundle`하여 `js`파일만 제공하는 컴포넌트가 많음

#### 외부 컴포넌트 사용방법

* `npm install` 및 cdn


* `import`  (`npm install`인 경우)

```javascript javascript
import apexCalendar from 'vue-apex-calendar'
```

* 지역

```javascript javascript
new Vue({
  components: {'apex-calendar': apexCalendar}
})
```

* 전역

```javascript javascript
Vue.component('apex-calendar', apexCalendar)
```

## Plugin

* 일반적으로 여러 컴포넌트들을 묶어서 기능을 만든 경우, 하나의 플러그인 형태로 배포
* 배포하는 쪽 (`install`할 때) 에서 **지역/전역** 선택하여 배포

#### 전역 배포 및 사용

* install

```javascript javascript
function install(Vue){
  Vue.component("vuetable", Vuetable);
  Vue.component("vuetable-pagination", VueTablePagination);
  Vue.component("vuetable-pagination-dropdown", VueTablePaginationDropDown);
  Vue.component("vuetable-pagination-info", VueTablePaginationInfo);
}
```

* 사용

```javascript javascript
Vue.use(SurveyEditor);
```

```html html
<draggable-canvas slot="viewport"></draggable-canvas>
```

`Vue.use()` 하면, 범위 안에서 컴포넌트를 전역으로 사용할 수 있다.

#### 지역 배포 및 사용

* install (survey 플러그인에서 발췌)

```javascript javascript
import SurveyEditor from './components/SurveyEditor';
import DraggableCanvas from './components/questionEditor/DraggableCanvas';
import Toolbox from './components/Toolbox';
import EditDialog from './components/dialog/EditDialog';

function install(Vue, options) {
}
export {
  SurveyEditor,
  DraggableCanvas,
  Toolbox,
  EditDialog,
  install,
};

export default SurveyEditor;
```

> https://kr.vuejs.org/v2/guide/plugins.html 참고

* 사용
  * 사용을 원하는 컴포넌트를, 지역으로 선언하여 사용한다.

```javascript javascript
import SurveyEditor from 'survey-editor';

Vue.use(SurveyEditor);

new Vue({
    el: '#app',
    components: {
        canvas: SurveyEditor.DraggableCanvas,
    }
})
```

> `import` : 소스코드를 읽어옴
>
> `use()` : survey 플러그인 내부 install 호출

```html html
<canvas slot="viewport"></canvas>
```

## 결론

**일반적으로 단일 컴포넌트인 경우, 컴포넌트로 사용하고, 여러컴포넌트들을 모아서 기능을 만든 경우는 플러그인 형태로 배포하여 사용한다.**
