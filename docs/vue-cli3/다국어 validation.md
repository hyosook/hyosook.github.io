## Vee-validate 사용

> https://logaretm.github.io/vee-validate/guide/validation-observer.html#validate-before-submit

### 사용방법



```html
<ValidationObserver v-slot="{ passes }">
    
 <form @submit.prevent="passes(_login)">
 
    </form>
    
</ValidationObserver>

```

* `ValidationProvider` 컴포넌트

```html
 <ValidationProvider :name="$t('attributes.userId')" rules="required" v-slot="{ errors }">
       <input class="form-control" v-model="userId" type="text">
       <h6 class="description text-center">{{ errors[0] }}</h6>
   
  </ValidationProvider>
```



### 선언 (다국어 적용됨)

> `i18n` 과 함께 사용하며 아래와 같이 선언한다

```json
import Vue from 'vue'
import { ValidationProvider, extend, ValidationObserver, localize } from 'vee-validate'
import * as rules from 'vee-validate/dist/rules'
import ko from 'vee-validate/dist/locale/ko.json'
import en from 'vee-validate/dist/locale/en.json'
import {
  i18n
} from '@/plugins/i18n'

// loop over all rules
for (let rule in rules) {
  extend(rule, rules[rule])
}

localize(i18n.locale)
localize({
  en,
  ko
})

Vue.component('ValidationProvider', ValidationProvider)
Vue.component('ValidationObserver', ValidationObserver)

```



# 다국어

(i18n 사용)[http://kazupon.github.io/vue-i18n/installation.html#compatibility-note]

##  읽기

```javascript
import Vue from 'vue'
import VueI18n from 'vue-i18n'
import Cookies from 'js-cookie'
import store from '@/store'

Vue.use(Cookies)
Vue.use(VueI18n)

function loadLocaleMessages () {
  const locales = require.context('@/assets/lang', true, /[A-Za-z0-9-_,\s]+\.json$/i)
  const messages = {}
  locales.keys().forEach(key => {
    const matched = key.match(/([A-Za-z0-9-_]+)\./i)
    if (matched && matched.length > 1) {
      const locale = matched[1]
      let tmpMessages = messages[locale] || {}
      messages[locale] = Object.assign(tmpMessages, locales(key))
    }
  })
  return messages
}
export const i18n = new VueI18n({
  silentTranslationWarn: true,
  locale: Cookies.get('locale') || process.env.VUE_APP_I18N_LOCALE,
  fallbackLocale: Cookies.get('locale') || process.env.VUE_APP_I18N_FALLBACK_LOCALE,
  messages: loadLocaleMessages()
})

export const setLocale = (lang, meta) => {
  i18n.locale = lang
  Cookies.set('locale', lang)
  store.commit('i18N/SET_LOCALE', lang)
  document.title = i18n.t(meta.title)
}
```

json 파일위치
`src/assets/lang/`
바뀌게 된다면
`i18n.jg`에서 `  const locales = require.context('@/assets/lang', true, /[A-Za-z0-9-_,\s]+\.json$/i)` 바꾸어준다

true로 설정해서, 폴더 구조를 읽어올수 있게 한다 


## 사용
* 컴포넌트에서
해당 컴포넌트에 선언가능 
lang/x.json 폴더 보다 이게 더 우선순위 가진다

```javascript
<i18n>
{
  "en": {
    "hello": "이게 먼저 되나 영어"
  },
  "ko": {
    "hello": "이게 먼저 되는거? 한국어"
  }
}
</i18n>
````

```
<i18n>
{
  "en": {
    "hello": "hello world!"
  },
  "ja": {
    "hello": "こんにちは、世界!"
  }
}
</i18n>

<template>
  <p>{{ $t('hello') }}</p>
</template>

<script>
export default {
  name: 'app',
  // ...
}
</script>
you also can:

<i18n src="./myLang.json"></i18n>
````

* 사용

```javascript
<template>
<div>
  <p>{{ $t('hello') }}</p>
  <p>{{ $t('msg.error.global') }}</p>
   <button @click="setLang('ko')">한국어</button>
    <button @click="setLang('en')">English</button>
</div>
</template>

<script>
export default {
  name: 'vue-lang',
  methods: {
    setLang (lang) {
      this.$i18n.locale = lang
    }
  }
}
</script>

<i18n>
{
  "en": {
    "hello": "이게 먼저 되나 영어"
  },
  "ko": {
    "hello": "이게 먼저 되는거? 한국어"
  }
}
</i18n>

````


* js 파일안에서 사용시 

```
import { i18n } from '@/plugins/i18n'
 i18n.t('error')
```

* app 아래 컴포넌트 에서 사용시

```
template : {{ $t("msg.index.test") }}
methods : this.$t("msg.index.test")
```

### 사용하기
1. validation 과 매핑해서 사용하는경우, validatite 폴더 아래 함께 선언해서 사용한다
2. 해당 컴포넌트에서 일회성으로 사용하는경우, `<i18n>`으로 선언해서 사용한다
3. 그외 전역으로 사용하는경우 , `폴더를 만들어서 사용하며, 폴더명과 json root명을 일치시킨다`


## 외부에서 받아온 값 다국어처리 (mixins 사용)
`valueByCurrentLang` 사용한다

* text  (db에서 공통코드로 가져온경우)

```html
{{valueByCurrentLang(KrName, EnName)}}
```

* 이미지  

```html
  <img :src="valueByCurrentLang(require('@/assets/img/ko.png'),require('@/assets/img/en.png'))">
```



