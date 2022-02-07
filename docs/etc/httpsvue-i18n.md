https://vue-i18n.intlify.dev/guide/essentials/scope.html#global-scope-1



You can use **vue-i18n** outside template(outside components) by importing **i18n** then, use "**t**" from **i18n.global**.

"**t**" doesn't need "**$**" and you can change **Locale** with **i18n.global.locale**.

```js
import i18n from '../i18n';
    
const { t } = i18n.global;
    
i18n.global.locale = 'en-US'; // Change "Locale"
    
const data = { 
  name: t('name'), // "t" doesn't need "$"
  description: t('description'), // "t" doesn't need "$"
};
```



You can use **VueI18n** outside components by importing **i18n** then, use "**t**" from **i18n.global**.

"**t**" doesn't need "**$**" and you can change **Locale** with **i18n.global.locale**.

```js
import i18n from '../i18n';

const { t } = i18n.global;

i18n.global.locale = 'en-US'; // Change "Locale"

const data = { 
  name: t('name'), // "t" doesn't need "$"
  description: t('description'), // "t" doesn't need "$"
};
```





https://github.com/intlify/bundle-tools/tree/main/packages/vue-i18n-loader

 setup() {

  const { t, locale } = useI18n({});

  return {

   t,

   locale,

  };

 },

<i18n>

{

 "en": {

  "login_label": "login"

 },

 "ko": {

  "login_label": "로그인"

 }

}

</i18n>

   <button>{{ t("login_label") }}</button>

    <p>{{ $t("home.test", { msg: "hello" }) }}</p>
    <p>영어모드 {{ $t("home.test", {}, { locale: "en" }) }}</p>
    <p>한글모드 {{ $t("home.test", {}, { locale: "ko" }) }}</p>