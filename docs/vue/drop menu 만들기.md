# drop menu 만들기

```javascript
<template>
  <div class="drop-menu" v-on="$listeners">
    <slot />
  </div>
</template>

<script>
export default {
  name: 'drop-menu',
  props: {
    active: { tyep: Boolean, default: false },
    fullWidth: Boolean,
    closeOnSelect: {
      type: Boolean,
      default: true
    }
  },
  data () {
    return {
      triggerEl: null,
      dropMenu: {
        instance: this,
        active: this.active,
        fullWidth: this.fullWidth,
        closeOnSelect: this.closeOnSelect, // 아이템 셀렉시 닫힐것인가
        $el: this.$el
      }
    }
  },
  provide () {
    return {
      dropMenu: this.dropMenu
    }
  },
  computed: {
    isActive () {
      return this.dropMenu.active
    }
  },
  watch: {
    active: {
      immediate: true,
      handler (isActive) {
        this.dropMenu.active = isActive
      }
    },
    isActive (isActive) {
      this.$emit('update:active', isActive)
    },
    closeOnSelect () {
      this.dropMenu.closeOnSelect = this.closeOnSelect
    }
  },
  methods: {
    toggleContent ($event) {
      this.dropMenu.active = !this.dropMenu.active
    }
  },
  mounted () {
    this.dropMenu.$el = this.$el

    this.$nextTick().then(() => {
      this.triggerEl = this.$el.querySelector(
        '[drop-menu-trigger]'
      ) /* 이거 붙은것만 할수잇다 클래스명 항상 이렇게 해야함 */

      if (this.triggerEl) {
        this.triggerEl.addEventListener('click', this.toggleContent)
      }
    })
  },
  beforeDestroy () {
    if (this.triggerEl) {
      this.triggerEl.removeEventListener('click', this.toggleContent)
    }
  }
}
</script>

```





```javascript
<template>
    <div name="drop-menu-content" class="drop-menu-content"  :css="didMount" v-if="shouldRender" v-on="$listeners">
      <div :class="[ mdContentClass]" :style="menuStyles" ref="menu">
          <ul  v-bind="filteredAttrs"  v-on="$listeners">
            <slot />
          </ul>
      </div>
    </div>
</template>

<script>
export default {
  name: 'drop-menu-content',
  components: {

  },
  props: {
    mdContentClass: [String, Boolean]
  },
  inject: ['dropMenu'],
  data: () => ({
    didMount: false,
    menuStyles: ''
  }),
  computed: {
    filteredAttrs () {
      const attrs = this.$attrs
      delete attrs.id
      return attrs
    },

    shouldRender () {
      return this.dropMenu.active
    }

  },
  watch: {
    shouldRender (shouldRender) {
      if (shouldRender) {
        // button으로 active 값을 조정하더라도 여기서 걸림 추후 사용에 따라서 조정
        this.$nextTick().then(() => {
          this.createClickEventObserver()
        })
      }
    }
  },
  methods: {
    clickOutside ($event) {
      $event.stopPropagation()
      if (!this.isMenuContentEl($event) && this.isBackdropExpectMenu($event)) {
        this.dropMenu.active = false
      }
    },
    isMenuContentEl ({ target }) {
      return this.$refs.menu ? this.$refs.menu.contains(target) : false
    },
    isBackdropExpectMenu ($event) {
      return !this.$el.contains($event.target) && !this.isMenu($event)
    },
    isMenu ({ target }) {
      return this.dropMenu.$el ? this.dropMenu.$el.contains(target) : false
    },
    createClickEventObserver () {
      if (document) {
        document.body.addEventListener('click', this.clickOutside)
      }
    },
    setStyles () {
      if (this.dropMenu.fullWidth) {
        this.menuStyles = `
            width: ${this.dropMenu.instance.$el.offsetWidth}px;
            max-width: ${this.dropMenu.instance.$el.offsetWidth}px
          `
      }
    }
  },
  mounted () {
    this.$nextTick().then(() => {
      this.setStyles()
      this.didMount = true
    })
  },
  beforeDestroy () {
    document.body.removeEventListener('click', this.clickOutside)
  }
}
</script>
<style lang="scss" scoped>
.drop-menu-content{
    background: yellow;
    z-index: 9999;

  }

</style>

```



```javascript
<template>
  <li
    class="drop-menu-item"
    v-bind="$attrs"
    :disabled="disabled"
    v-on="listeners"
  >
    <slot />
  </li>
</template>

<script>

export default {
  name: 'drop-menu-item',
  props: {
    disabled: Boolean
  },
  inject: ['dropMenu'],
  data: () => ({
  }),
  computed: {

    listeners () {
      if (this.disabled) {
        return {}
      }

      if (!this.dropMenu.closeOnSelect) {
        return this.$listeners
      }

      let listeners = {}
      let listenerNames = Object.keys(this.$listeners)

      listenerNames.forEach(listener => {
        if ([
          'click',
          'dblclick'
        ].includes(listener)) {
          listeners[listener] = $event => {
            this.$listeners[listener]($event)
            this.closeMenu()
          }
        } else {
          listeners[listener] = this.$listeners[listener]
        }
      })

      return listeners
    }
  },
  methods: {
    closeMenu () {
      this.dropMenu.active = false
    },

    triggerCloseMenu () {
      if (!this.disabled) {
        this.closeMenu()
      }
    }
  },
  mounted () {
    // a태그 경우, 클릭시 닫힘
    if (this.$el.children && this.$el.children[0]) {
      let listItem = this.$el.children[0]

      if (listItem.tagName.toUpperCase() === 'A') {
        this.$el.addEventListener('click', this.triggerCloseMenu)
      }
    }
  },

  beforeDestroy () {
    this.$el.removeEventListener('click', this.triggerCloseMenu)
  }
}
</script>

```



```javascript
props :{
 show: { tyep: Boolean, default: false },
},

  data () {
    return {
	
	active: this.show,}
	
	
	
	  computed: {
    isActive () {
      return this.active
    }
  },
  
    watch: {
    show: {
      immediate: true,
      handler (isActive) {
        this.active = isActive
      }
    },
    isActive (isActive) {
      this.$emit('update:show', isActive)
    }
  },
	
```





### active

> drop down에서 아주 중요 이걸로 제어함 



```html

md-menu-trigger
>> md-menu방아쇠md-menu-trigger



메뉴에 클래스로 
넣어야한다 
    mounted () {
      this.MdMenu.$el = this.$el

      this.$nextTick().then(() => {
        this.triggerEl = this.$el.querySelector('[md-menu-trigger]')

        if (this.triggerEl) {
          this.triggerEl.addEventListener('click', this.toggleContent)
        }
      })
    },
    beforeDestroy () {
      if (this.triggerEl) {
        this.triggerEl.removeEventListener('click', this.toggleContent)
      }
    }

```

```html


```

## 사용



### provide / inject

* 쌍은 함께 사용
* 상위 컴포넌트가 컴포넌트 계층 구조의 깊이에 관계없이 모든 하위 항목에 대한 종속성을 주입하는 역할을 하도록 허용합니다. 
* 상위 컴포넌트

```javascript

data () {
    return {
      dropMenu: {
        instance: this,
        active: this.active,
        fullWidth: this.fullWidth,
        closeOnSelect: this.closeOnSelect, // 아이템 셀렉시 닫힐것인가
        $el: this.$el
      }
    }
  }, 

provide () {
    return {
      dropMenu: this.dropMenu
    }
  },
```

* 하위 컴포넌트 

```javascript
 inject: ['dropMenu'],
     
// this.dropMenu >> 접근     
```







