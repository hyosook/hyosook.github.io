# Render Functions & JSX

Vue CLI를 통해 생성된 `main.js` 파일을 살펴보면 다음과 같이 Vue 인스턴스를 선언하고 있다.

```
new Vue({
  router,
  store,
  render: h => h(App),
}).$mount('#app');
```

`router`와 `store`는 각각 Vue-router와 Vuex를 사용 설정하면서 주입된 객체이고, 실제로 Root Component인 `App`을 연결하는 것은

```
render: h => h(App)
```

이 부분이다. 하지만 그저 `#app` div의 템플릿을 지정해 주는 것이니 `template` 메소드를 사용해서 다음과 같이 만들 수도 있지 않을까?

```
new Vue({
	components: { App }
	template: `<div> 
		<app></app>
	</div>`
}).$mount('#app');
```

가능하다. 대신 이 상태로 실행하려면 `vue.config.js`에

```
module.exports = {
	  runtimeCompiler: true,
}
```

`runtimeCompiler` 옵션을 추가해 줘야 한다. 그렇지 않으면 `You are using the runtime-only build of Vue where the template compiler is not available. Either pre-compile the templates into render functions, or use the compiler-included build.` 라는 에러 메시지가 뜬다.

여기서 `template`과 `render`의 차이점이 보인다. `render`를 사용해서 pre-compile을 하거나 compiler-included build를 사용하라는 것이다. 즉, `render`의 역할이 단순히 템플릿을 연결해주는 것이 아니라 ‘컴파일’도 포함되어 있다는 것을 알 수 있다.

공식 문서에서 소개하는 render의 필요성은 [이 항목](https://greenmon.dev/2019/02/25/[Render Functions & JSX — Vue.js]https://kr.vuejs.org/v2/guide/render-function.html#기본)에서 잘 나와 있다.

```
 render: function (createElement) {
     return createElement(
       'h' + this.level,   // 태그 이름
       this.$slots.default // 자식의 배열
     )
   },
```

위와 같이 `template`과 `v-if` 속성을 사용해서 중복된 코드를 작성하는 것보다 바로 조건에 해당하는 element만 반환할 수 있기 때문이다.

이러한 코드의 이점 외에도, 앞에서 언급했듯이 render가 pre-compile을 수행하기 때문에 실제 템플릿에 더 가까운 동작을 한다는 사실도 중요하다. 이를 이해하려면 브라우저의 작동 방식, 즉 Node, Tree, 그리고 Virtual DOM 개념 또한 알아야 한다.

렌더 함수를 알아보기 전에 브라우저 작동 방식을 알아야합니다. 아래 HTML 예제를 보세요





##  DOM Node Tree

```html
<div>
  <h1>My title</h1>
  Some text content
  <!-- TODO: Add tagline  -->
</div>
```

브라우저가 이 코드를 읽게 되면, 모든 내용을 추적하기 위해 가계도처럼 [“DOM 노드” 트리](https://javascript.info/dom-nodes)를 만듭니다.

위 HTML의 DOM 노드 트리는 아래와 같습니다.

![DOM Tree Visualization](https://kr.vuejs.org/images/dom-tree.png)

모든 엘리먼트는 노드입니다. 각 텍스트도 노드입니다. 심지어 주석도 노드입니다! 노드는 페이지를 이루는 각각의 조각입니다. 그리고 트리에서 보듯 각 노드는 자식을 가질 수 있습니다. (즉, 각 조각들은 다른 조각들을 포함할 수 있습니다.)

노드를 효율적으로 갱신하는 것은 어렵지만 수동으로 할 필요는 없습니다. 템플릿에서 Vue가 페이지에서 수정하기 원하는 HTML만 지정하면 됩니다.

```html
<h1>{{ blogTitle }}</h1>
```

또는 렌더 함수에서

```javascript
render: function (createElement) {
  return createElement('h1', this.blogTitle)
}
```

두가지 경우 모두 Vue는 페이지를 자동으로 갱신합니다. `blogTitle`의 변경 또한 마찬가지입니다.



## Virtual DOM

Vue는 실제 DOM에 필요한 변경사항을 추적하기 위해, 기본으로 브라우저에서 형성하는 “DOM Node” Tree 외에도 자체적으로 실제 DOM에 필요한 변경사항을 추적하기 위해 **virtual DOM**을 만듭니다. 

이를 자세히 살펴보면 아래와 같습니다.

```
return createElement('h1', this.blogTitle)
```

`createElement`는 실제로 무엇을 반환할까요? 실제 DOM 엘리먼트와 *정확하게* 일치하지는 않습니다. Vue에게 자식노드에 대한 설명을 포함하여 페이지에서 렌더링해야하는 노드의 종류를 설명하는 정보를 포함하기 때문에 더 정확하게 `createNodeDescription`이라는 이름을 지정할 수 있습니다. 이 노드에 관한 설명을 **VNode**로 축약된 가상 노드라고 부릅니다.

 “버추얼 DOM”은 Vue 컴포넌트 트리로 만들어진 VNode 트리입니다.

바로 `createElement` 명령을 통해 반환되는 객체가 Virtual DOM의 노드가 된다. 이를 **VNode**라고 부른다.



`render` 함수는 Vue의 실행 환경에서 `createElement` 함수를 인자로 받고, 이를 통해 만들어진 VNode를 반환하는 구조를 가진다.

즉, Vue CLI의 `main.js`에 있는

```
render: h => h(App)
```

이라는 암호(?)같은 표현은 사실

```
render: (createElement) => { return createElement(App) }
```

이었던 것이다.

## JSX

사실 `h`라는 알쏭달쏭한 파라미터 이름은 JSX에서 관용적으로 쓰이는 표현이다. JSX는 Javascript + XML을 합친 기존 자바스크립트의 확장 문법이다.

이를 통해, 자바스크립트 내부에 마크업 코드를 바로 작성할 수 있다. 물론 이게 그대로 실행되는 것이 아니라, babel과 같은 transpile 도구를 통해 일반 javascript로 변환되는 것이다.

`h`는 `hyperscript`의 약자로 javascript 코드를 인자로 받아 또다른 javascript 코드를 생성해 주는 것을 의미한다.



`render` 함수를 많이 작성하면 다음과 같이 작성하는 것이 고통스럽게 느껴질 수 있습니다.

```
createElement(
  'anchored-heading', {
    props: {
      level: 1
    }
  }, [
    createElement('span', 'Hello'),
    ' world!'
  ]
)
```

템플릿 버전이 아래 처럼 너무 간단한 경우에 특히 더 그럴 것 입니다.

```
<anchored-heading :level="1">
  <span>Hello</span> world!
</anchored-heading>
```

그래서 Vue와 JSX를 함께 사용하기 위해 [Babel plugin](https://github.com/vuejs/babel-plugin-transform-vue-jsx)를 이용할 수 있습니다.

```
import AnchoredHeading from './AnchoredHeading.vue'

new Vue({
  el: '#demo',
  render: function (h) {
    return (
      <AnchoredHeading level={1}>
        <span>Hello</span> world!
      </AnchoredHeading>
    )
  }
})
```

`createElement`를 별칭 `h`로 이용하는 것은 Vue 생태계에서 볼 수 있는 공통된 관습이며 실제로 JSX에 필요합니다.

## [기본](https://kr.vuejs.org/v2/guide/render-function.html#기본)

Vue는 템플릿을 사용하여 대다수의 경우 HTML을 작성할 것을 권장합니다. 그러나 JavaScript가 완전히 필요한 상황이 있습니다. 바로 여기에서 템플릿에 더 가까운 컴파일러인 **render 함수**를 사용할 수 있습니다.

`render` 함수가 실용적 일 수있는 간단한 예제를 살펴 보겠습니다. 링크를 포함한 헤더를 생성한다고 가정한 예제 입니다.

```html
<h1>
  <a name="hello-world" href="#hello-world">
    Hello world!
  </a>
</h1>
```

위의 HTML의 경우 이 컴포넌트 인터페이스가 필요하다고 결정합니다.

```html
<anchored-heading :level="1">Hello world!</anchored-heading>
```

`level` prop를 기반으로 방금 제목을 생성하는 컴포넌트를 이용하면 다음과 같이 빠르게 만들 수 있습니다.

```html
<script type="text/x-template" id="anchored-heading-template">
  <h1 v-if="level === 1">
    <slot></slot>
  </h1>
  <h2 v-else-if="level === 2">
    <slot></slot>
  </h2>
  <h3 v-else-if="level === 3">
    <slot></slot>
  </h3>
  <h4 v-else-if="level === 4">
    <slot></slot>
  </h4>
  <h5 v-else-if="level === 5">
    <slot></slot>
  </h5>
  <h6 v-else-if="level === 6">
    <slot></slot>
  </h6>
</script>
Vue.component('anchored-heading', {
  template: '#anchored-heading-template',
  props: {
    level: {
      type: Number,
      required: true
    }
  }
})
```

이 템플릿은 별로 좋지 않습니다. 이것은 장황할 뿐만 아니라 모든 헤딩 수준에 대해 `<slot> </slot>`을 중복으로 가지고 있으며 앵커 엘리먼트를 추가 할 때도 똑같이 해야합니다.

템플릿은 대부분의 컴포넌트에서 훌륭하게 작동하지만 분명하지는 않습니다. 이제 `render` 함수로 다시 작성해 봅니다.

```javascript
Vue.component('anchored-heading', {
  render: function (createElement) {
    return createElement(
      'h' + this.level,   // 태그 이름
      this.$slots.default // 자식의 배열
    )
  },
  props: {
    level: {
      type: Number,
      required: true
    }
  }
})
```

훨씬 간단 합니다! 이 코드는 더 짧지만 Vue 인스턴스 속성에 더 익숙해야합니다. 이 경우 `anchored-heading` 안에 `Hello world!`와 같이 `slot` 속성 없이 자식을 패스 할 때 그 자식들은 `$slots.default` 에있는 컴포넌트 인스턴스에 저장된다는 것을 알아야합니다. 아직 구현하지 않았다면 **render 함수로 들어가기 전에 instance properties API를 읽는 것이 좋습니다.**



## [`createElement` 전달인자](https://kr.vuejs.org/v2/guide/render-function.html#createElement-전달인자)

다음으로 살펴볼 것은 `createElement` 함수에서 템플릿 기능을 사용하는 방법입니다. 다음은 `createElement`가 받아들이는 전달인자입니다.

```javascript
// @returns {VNode}
createElement(
  // {String | Object | Function}
  // HTML 태그 이름, 컴포넌트 옵션 또는 함수 중
  // 하나를 반환하는 함수입니다. 필수 사항.
  'div',

  // {Object}
  // 템플릿에서 사용할 속성에 해당하는 데이터 객체입니다
  // 데이터 객체입니다. 선택 사항.
  {
    // (아래 다음 섹션에 자세히 설명되어 있습니다.)
  },

  // {String | Array}
  // VNode 자식들. `createElement()`를 사용해 만들거나,
  // 간단히 문자열을 사용해 'text VNodes'를 얻을 수 있습니다. 선택사항
  [
    'Some text comes first.',
    createElement('h1', 'A headline'),
    createElement(MyComponent, {
      props: {
        someProp: 'foobar'
      }
    })
  ]
)
```









### [데이터 객체 깊이 알아 보기](https://kr.vuejs.org/v2/guide/render-function.html#데이터-객체-깊이-알아-보기)

한가지 주의 해야 할 점은 `v-bind:class` 와 `v-bind:style`이 템플릿에서 특별한 처리를 하는 것과 비슷하게, VNode 데이터 객체에 최상위 필드가 있습니다. 이 객체는`innerHTML`과 같은 DOM 속성뿐 아니라 일반적인 HTML 속성도 바인딩 할 수 있게 합니다.(이것은`v-html` 디렉티브를 대신해 사용할 수 있습니다.)

```javascript
{
  // `v-bind:class` 와 같음
  // accepting either a string, object, or array of strings and objects.
  class: {
    foo: true,
    bar: false
  },
  // `v-bind:style` 와 같음
  // accepting either a string, object, or array of objects.
  style: {
    color: 'red',
    fontSize: '14px'
  },
  // 일반 HTML 속성
  attrs: {
    id: 'foo'
  },
  // 컴포넌트 props
  props: {
    myProp: 'bar'
  },
  // DOM 속성
  domProps: {
    innerHTML: 'baz'
  },
  // `v-on:keyup.enter`와 같은 수식어가 지원되지 않으나
  // 이벤트 핸들러는 `on` 아래에 중첩됩니다.
  // 수동으로 핸들러에서 keyCode를 확인해야 합니다.
  on: {
    click: this.clickHandler
  },
  // 컴포넌트 전용.
  // `vm.$emit`를 사용하여 컴포넌트에서 발생하는 이벤트가 아닌
  // 기본 이벤트를 받을 수 있게 합니다.
  nativeOn: {
    click: this.nativeClickHandler
  },
  // 사용자 지정 디렉티브.
  // Vue는 이를 관리하기 때문에 바인딩의 oldValue는 설정할 수 없습니다.
  directives: [
    {
      name: 'my-custom-directive',
      value: '2',
      expression: '1 + 1',
      arg: 'foo',
      modifiers: {
        bar: true
      }
    }
  ],
  // 범위 지정 슬롯. 형식은
  // { name: props => VNode | Array<VNode> } 입니다.
  scopedSlots: {
    default: props => createElement('span', props.text)
  },
  // 이 컴포넌트가 다른 컴포넌트의 자식인 경우, 슬롯의 이름입니다.
  slot: 'name-of-slot',
  // 기타 최고 레벨 속성
  key: 'myKey',
  ref: 'myRef',
  // If you are applying the same ref name to multiple
  // elements in the render function. This will make `$refs.myRef` become an
  // array
  refInFor: true
}
```

### [전체 예제](https://kr.vuejs.org/v2/guide/render-function.html#전체-예제)

이 지식과 함께 이제 컴포넌트를 마칠 수 있습니다.

```javascript
var getChildrenTextContent = function (children) {
  return children.map(function (node) {
    return node.children
      ? getChildrenTextContent(node.children)
      : node.text
  }).join('')
}

Vue.component('anchored-heading', {
  render: function (createElement) {
    // kebabCase id를 만듭니다.
    var headingId = getChildrenTextContent(this.$slots.default)
      .toLowerCase()
      .replace(/\W+/g, '-')
      .replace(/(^-|-$)/g, '')

    return createElement(
      'h' + this.level,
      [
        createElement('a', {
          attrs: {
            name: headingId,
            href: '#' + headingId
          }
        }, this.$slots.default)
      ]
    )
  },
  props: {
    level: {
      type: Number,
      required: true
    }
  }
})
```

### [제약사항](https://kr.vuejs.org/v2/guide/render-function.html#제약사항)

#### VNodes는 고유해야 합니다

컴포넌트 트리의 모든 VNode는 고유 해야 합니다. 아래 예제는 다음 렌더링 함수가 유효하지 않음을 의미합니다.

```javascript
render: function (createElement) {
  var myParagraphVNode = createElement('p', 'hi')
  return createElement('div', [
    // 이런 - Vnode가 중복입니다!
    myParagraphVNode, myParagraphVNode
  ])
}
```

같은 엘리먼트 / 컴포넌트를 여러 번 복제하려는 경우 팩토리 기능을 사용하여 여러 번 반복 할 수 있습니다. 예를 들어, 다음 렌더링 함수는 20개의 같은 p태그를 완벽하게 렌더링하는 방법입니다.

```javascript
render: function (createElement) {
  return createElement('div',
    Array.apply(null, { length: 20 }).map(function () {
      return createElement('p', 'hi')
    })
  )
}
```

## [템플릿 기능을 일반 JavaScript로 변경하기](https://kr.vuejs.org/v2/guide/render-function.html#템플릿-기능을-일반-JavaScript로-변경하기)

### [`v-if` 와 `v-for`](https://kr.vuejs.org/v2/guide/render-function.html#v-if-와-v-for)

일반 JavaScript를 사용할 수 있는 환경이면 어디든지 Vue 렌더링 함수는 한가지 방법만을 제공하지는 않습니다. 예를 들어,`v-if`와`v-for`를 사용하는 템플릿에서 :

```html
<ul v-if="items.length">
  <li v-for="item in items">{{ item.name }}</li>
</ul>
<p v-else>No items found.</p>
```

이것은 render 함수에서 `if` /`else` 와 `map`을 사용하여 재 작성 될 수 있습니다.

```javascript
props: ['items'],
render: function (createElement) {
  if (this.items.length) {
    return createElement('ul', this.items.map(function (item) {
      return createElement('li', item.name)
    }))
  } else {
    return createElement('p', 'No items found.')
  }
}
```

### [`v-model`](https://kr.vuejs.org/v2/guide/render-function.html#v-model)

렌더 함수에는 직접적으로 `v-model`에 대응되는 것이 없습니다. 직접 구현해야합니다.

```javascript
props: ['value'],
render: function (createElement) {
  var self = this
  return createElement('input', {
    domProps: {
      value: self.value
    },
    on: {
      input: function (event) {
        self.$emit('input', event.target.value)
      }
    }
  })
}
```

이것은 더 깊은 수준으로 건드려야 하지만 `v-model`에 비해 상호 작용에 대한 세부 사항을 훨씬 더 많이 제어 할 수 있습니다.

### [이벤트 및 키 수식어](https://kr.vuejs.org/v2/guide/render-function.html#이벤트-및-키-수식어)

`.passive`, `.capture` 및 `.once` 이벤트 수식어를 위해 Vue는 `on`과 함께 사용할 수있는 접두사를 제공합니다

| 수식어                               | 접두어 |
| :----------------------------------- | :----- |
| `.passive`                           | `&`    |
| `.capture`                           | `!`    |
| `.once`                              | `~`    |
| `.capture.once` 또는 `.once.capture` | `~!`   |

예제

```javascript
on: {
  '!click': this.doThisInCapturingMode,
  '~keyup': this.doThisOnce,
  '~!mouseover': this.doThisOnceInCapturingMode
}
```

다른 모든 이벤트 및 키 수식어의 경우 처리기에서 이벤트 메서드를 간단하게 사용할 수 있으므로 고유한 접두사는 필요하지 않습니다.

| 수식어                                             | 동등한 핸들러                                                |
| :------------------------------------------------- | :----------------------------------------------------------- |
| `.stop`                                            | `event.stopPropagation()`                                    |
| `.prevent`                                         | `event.preventDefault()`                                     |
| `.self`                                            | `if (event.target !== event.currentTarget) return`           |
| 키: `.enter`, `.13`                                | `if (event.keyCode !== 13) return` (`13`을 다른 키 수식어의 [다른 키 코드](http://keycode.info/)로 변경합니다.) |
| Modifiers Keys: `.ctrl`, `.alt`, `.shift`, `.meta` | `if (!event.ctrlKey) return` (`ctrlKey`를 `altKey`, `shiftKey` 또는 `metaKey`로 각각 변경하십시오.) |

다음은 위의 수식어들이 사용된 예제 입니다.

```javascript
on: {
  keyup: function (event) {
    // 이벤트를 내보내는 요소가 이벤트가 바인딩 된 요소가 아닌 경우
    // 중단합니다.
    if (event.target !== event.currentTarget) return
    // 키보드에서 뗀 키가 Enter키 (13)이 아니며
    // Shift키가 동시에 눌러지지 않은 경우
    // 중단합니다.
    if (!event.shiftKey || event.keyCode !== 13) return
    // 전파를 멈춥니다.
    event.stopPropagation()
    // 엘리먼트 기본 동작을 방지합니다.
    event.preventDefault()
    // ...
  }
}
```

### [Slots](https://kr.vuejs.org/v2/guide/render-function.html#Slots)

[`this.$slots`](https://kr.vuejs.org/v2/api/#vm-slots)에서 정적 슬롯 내용을 VNodes의 배열로 접근할 수 있습니다.

```javascript
render: function (createElement) {
  // `<div><slot></slot></div>`
  return createElement('div', this.$slots.default)
}
```

또한 특정 범위를 가지는 슬롯 [`this.$scopedSlots`](https://kr.vuejs.org/v2/api/#vm-scopedSlots)에서 VNode를 반환하는 함수로 접근할 수 있습니다.

```javascript
props: ['message'],
render: function (createElement) {
  // `<div><slot :text="message"></slot></div>`
  return createElement('div', [
    this.$scopedSlots.default({
      text: this.message
    })
  ])
}
```

범위 함수 슬롯을 렌더링 함수를 사용하여 하위 컴포넌트로 전달하려면 VNode 데이터에서 `scopedSlots` 필드를 사용하십시오.

```javascript
render: function (createElement) {
  return createElement('div', [
    createElement('child', {
      // 데이터 객체의 `scopedSlots`를 다음 형식으로 전달합니다
      // { name: props => VNode | Array<VNode> }
      scopedSlots: {
        default: function (props) {
          return createElement('span', props.text)
        }
      }
    })
  ])
}
```

## [JSX](https://kr.vuejs.org/v2/guide/render-function.html#JSX)

`render` 함수를 많이 작성하면 다음과 같이 작성하는 것이 고통스럽게 느껴질 수 있습니다.

```
createElement(
  'anchored-heading', {
    props: {
      level: 1
    }
  }, [
    createElement('span', 'Hello'),
    ' world!'
  ]
)
```

템플릿 버전이 아래 처럼 너무 간단한 경우에 특히 더 그럴 것 입니다.

```
<anchored-heading :level="1">
  <span>Hello</span> world!
</anchored-heading>
```

그래서 Vue와 JSX를 함께 사용하기 위해 [Babel plugin](https://github.com/vuejs/babel-plugin-transform-vue-jsx)를 이용할 수 있습니다.

```
import AnchoredHeading from './AnchoredHeading.vue'

new Vue({
  el: '#demo',
  render: function (h) {
    return (
      <AnchoredHeading level={1}>
        <span>Hello</span> world!
      </AnchoredHeading>
    )
  }
})
```

`createElement`를 별칭 `h`로 이용하는 것은 Vue 생태계에서 볼 수 있는 공통된 관습이며 실제로 JSX에 필요합니다. Starting with [version 3.4.0](https://github.com/vuejs/babel-plugin-transform-vue-jsx#h-auto-injection) of the Babel plugin for Vue, we automatically inject `const h = this.$createElement` in any method and getter (not functions or arrow functions), declared in ES2015 syntax that has JSX, so you can drop the `(h)` parameter. With prior versions of the plugin, your app would throw an error if `h` was not available in the scope. 사용하는 범위에서 `h`를 사용할 수 없다면, 앱은 오류를 발생시킵니다.

JSX가 JavaScript에 매핑되는 방법에 대한 [자세한 내용](https://github.com/vuejs/jsx#installation)을 확인하세요.