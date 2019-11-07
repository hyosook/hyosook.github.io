# v-model

> `value`  속성과 `input` 이벤트를 함꼐 사용하는것 
>
>  `input` 이벤트가 발생했을때, `value`값을 변경 한다 



```javascript
<input v-model="searchText">
```

```javascript
<input
  :value="searchText"
  @input="searchText = $event.target.value"
>
```

1.  input 이벤트 감지 -> `$event.target.value를 인자값`으로 받는다
2. **searchText** 를 갱신
3.  searchText의 변경에 따라서 **:value** 가 변경



## 부모컴포넌트에서 사용자가 정의한 input 컴포넌트를 사용하는 경우

* `custom-input`

  ```javascript
  <template>
      <input
        :value="searchText"
        @input="searchText = $event.target.value"
      >
  </template>
  
  <template>
        <input  :value="searchText" @input="$emit('input', $event.target.value)">
  </template>
  
  <script>
  export default {
    name: 'custom-input',
          props: {
              value: {
              type: null,
              required: true
         }
    },
    method: {
      onInput (value) {
        this.$emit('input', value) // 부모에서 input이벤트 감지 후 데이터를 자식에게 전달한   
      },
    }
  }
  ```

  

* 부모

  ```javascript
  <custom-input
    v-bind:value="searchText"
    v-on:input="searchText = $event"
  ></custom-input>
  ```

  ```javascript
  <custom-input v-model="searchText"></custom-input>
  ```

  * `value` attribute는 `custom-input`의 `value` prop와 바인딩 됩니다.
  * `input` 이벤트는 `custom-input`에서 `$emit`으로 호출되는 이벤트입니다.





* 부모 input 컴포넌트 & 자식  (prpo 변경시 )

```javascript
//child component

<template>
    <input  @input="onInput"  v-model="valueClone">
</template>

<script>
export default {
  name: 'ChildInput',
        props: {
            value: {
            type: null,
            required: true
       }},
          data () {
               return {
                valueClone: this.value, // prop value변경해야 해서 사용함 ,이벤트 같이 일어나게 하려고 걸어둠
      
    }
  },
  method: {
    onInput (value) {
      this.$emit('input', value) // 부모에서 input이벤트 감지 후 데이터를 자식에게 전달한   
    },
  }
}
</script>
//parent component

<template>
    <child-input  v-model="navSearchvalue">
</template>

export default {
  name: 'Parent',
  data () {
    return  {
     navSearchvalue: '',
    }
  }
}
</script>

```







## slot

> 랜더링  교체되는 구멍
>
> 비워놓고 사용한다

* 선언

```javascript
 <slot name="md-autocomplete-item" :item="item" :term="valueClone">
     
     <!-- slot없을때 기본값 -->
              <template v-if="isOptionsString">{{ item }}</template>
              <template v-else>{{ item.name }}</template>
     <!-- slot없을때 기본값 -->
              
</slot>
 data() {
    return {
      item: "hello",
      valueClone: false
    };
  },

computed: {
    hasScopedAutocompleteItemSlot () {
      return this.$scopedSlots['md-autocomplete-item']
    }
  }
```

> * 이름  :md-autocomplete-item
> * 슬롯 속성 : item, term
>   * 데이터를 넘길수 있다
> * 기본값지정 :  slot 사이이에 넣으면 ,기본값 지정 된다 
>
> 



* 사용

```javascript
 <template #md-autocomplete-item="{ item, term }">
 
        <md-highlight-text :md-term="term">{{ item.name }}</md-highlight-text>
        <button v-if="_isMoveable(item.id)" @click="_clickDetail(item)">         			{{$t('label.click')}}</button>

      </template>
```

> * `#` 슬롯이름 = " { 슬롯속성 이름}"
>
>   



#### 자식 컴포넌트의 이벤트를 부모에서 사용하게 

```html

<template>
  <div>
      <slot name="header" :close="close">   
      </slot>
    <div>

</template>

<script>
export default {
  name: "자식",
  data() {
    
  },
  methods: {
    close() {
      console.log('close메소드')
    }
  }
}
</script>
```

```html
<div>
  <자식>
    <template  #header="slotProps">
      <button @click="slotProps.close">닫기</button>
    </template>
   
  </자식>
</div>
```

