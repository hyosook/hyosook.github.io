---
title : env
tag : ["vue-cli3","vue.js"]
---



`.env`

`.env.aws`

`.env.production`



### 호출

process.env.VUE_APP_API_DOMAIN,



### 정의

**VUE_APP**_API_DOMAIN=  'hyosook.com'  



### package.json

```javascript
"scripts": {
		"aws-build": "cp ./.env.aws ./.env.production && vue-cli-service build --mode production",
		"dev-build": "cp ./.env.dev ./.env.production && vue-cli-service build --mode production"
	},
```

