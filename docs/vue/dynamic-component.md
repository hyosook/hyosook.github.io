

# 컴포넌트 

## 동적 컴포넌트 사용 

```html
 <component v-bind:is="compoentsName"  :file-seq="item.seq" @update="_onFileSave(item.seq)"></component>


  data: () => ({
    compoentsName: 'input-file'
  }),

 components: { 'input-file': () => import('@/components/input-file') },
```

* file-seq  : prop

* `@update="_onFileSave(item.seq)`  : `item.seq`  괄호 안은 부모꺼 

  ```javascript
   
    _onFileSave (seq) {
       
      },
      
   this.$emit('update')
  ```

* `@update="_onFileSave"   `

  ```javascript
    _onFileSave (data) {
       
      },
      
   this.$emit('update' ,data)
  ```

* `@update="_onFileSave(item.seq,$event)"` 

  ```javascript
  
    _onFileSave (seq ,data ) {
   
      },
      
  this.$emit('update', data)
  ```



```html
<!-- 비활성 컴포넌트는 캐싱! -->
<keep-alive>
   <component v-bind:is="compoentsName"  :file-seq="item.seq" @update="_onFileSave(item.seq)"></component>
</keep-alive>
```



### 동적 컴포넌트 이름 

```html
  computed: {
componentName() {
      return () =>
        import(
          `@/components/appl-form-basis-${this.getSchoolNameByCode(
            this.schoolCode
          )}`
        )
    },
```

### 이름 없는경우 , 미리 랜더

```js
 <template #school>
      <component v-bind:is='shoolcomponent' v-model="basisInfo"></component>
    </template>


  computed: {
    shoolcomponent () {
      return this.$options.components[this.componetName] ? this.componetName : null
    },
    componetName () {
      return this.getSchoolNameByCode(this.schoolCode)
    }
  },
  methods: {
    setComponent () {
      let files = require.context('@/components/appl-form-basis-school', false, /\.vue$/)
      files.keys().forEach((key) => {
        if (this.componetName === key.replace(/(\.\/|\.vue)/g, '')) {
          this.$options.components[this.componetName] = files(key).default
        }
      })
    }
  }
```



### 폴더아래 찾기 / 없는경우 디폴트 설정가능

```js

  methods: {
nc getCareerFormPath () {
      const files = await require.context('@/components/appl-form-career-school', false, /\.vue$/)
      return this.getComponent(files)
        .then(_res => {
          if (_res) return `appl-form-career-school/${this.componetName}`
          else return 'appl-form-career-school/career-frame'
        })
    },
    getComponent (files) {
      return new Promise((resolve, reject) => {
        let component = null
        files.keys().forEach((key) => {
          if (this.componetName === key.replace(/(\.\/|\.vue)/g, '')) {
            component = files(key).default
          }
        })
        resolve(component)
      })
    }
  }
}

```



## 이슈 

> input file 관련해서 컴포넌트를 만듬 
>
> dom 초기화가 안되서 , 시간을 두고 진행 다시 확인 필요

```html
 setTimeout(() => {
        this.compoentsName = 'input-file'
      }, 10)
```



###  자식 이벤트를 부모에서  호출 

> `ref` 선언해서 사용한다

* 부모 

```html

<post-file :dbAttachments="dbAttachments"  ref="file">
                 <template #db-file-delete="slotProps">
                  <i
                    class="material-icons"
                    v-show="slotProps.isShow"
                    @click.prevent="slotProps.delete(slotProps.seq)"
                  >delete_outline</i>
                </template>
              </post-file>



this.$refs.file.uploadingEnd()
```

* 자식  `post-file` 

  ```javascript
    methods: { 
  	uploadingEnd () {
        this.isUploading = false
      }
    }
  ```

