# class바인딩

* 객체 구문

  ```html
  <div :class="{ active: isActive }"></div>
  ```

  * `active` 클래스의 존재는 , ` isActive`  true일경우 

    

* 사용

  ```html
    <div id='drop-down'
      :class="[
        { open: isOpen },
        { dropdown: direction === 'down' },
        { dropup: direction === 'up' }
      ]"
      @click="toggleDropDown"
      v-click-outside="closeDropDown"
    >
    
    data:{
    isOpen : true ,
    direction : 'down'
    }
  ```

  

* 결과

  ```html
    <div id='drop-down' class="open dropdown"
      @click="toggleDropDown"
      v-click-outside="closeDropDown"
    >
  ```

  

# style 바인딩

* 사용1

  ```html
  <div v-bind:style="{ color: activeColor, fontSize: fontSize + 'px' }"></div>
  
  data: {
    activeColor: 'red',
    fontSize: 30
  }
  ```

  

* 스타일 객체에 직업 바인딩

  ```html
  <div v-bind:style="styleObject"></div>
  
  data: {
    styleObject: {
      color: 'red',
      fontSize: '13px'
    }
  }
  ```

  ```html
   <i :style="_getTitleStyleObj">subdirectory_arrow_right</i>
  
    computed: {
      _getTitleStyleObj () {
        return {
          display: (this.item.parentPostNo || this.parentNo) ? 'inline' : 'none',
          'margin-left': `${(this.item.depth || this.depth) * this.margin}px`
        }
      }
    }
  ```
  
  

* 배열구문 

  ```html
  <div
    :style="[styleObject, {width:inputWidth+'px'}]"
   >
      
      
    props: {
      inputWidth: {
        type: String,
        default: '40px'
      }
    
    },
    data: () => ({
      styleObject: {
        'z-index': 100,
        position: 'absolute',
        cursor: 'pointer',
        opacity: 0
      }
    })
  ```

  