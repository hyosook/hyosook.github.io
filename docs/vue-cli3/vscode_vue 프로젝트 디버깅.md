---
title : Chrome 브라우저로 Vue.js 디버깅하기
tags : ["vue.js", "vscode", "디버깅"]
---



## [vsCode] Chrome 브라우저로 Vue.js 디버깅하기

### 설치

* Vue CLI 

  ```
  > npm install -g @vue/cli
  ```

* Debugger for Chrome 

  마켓플레이스에서 검색하여 설치 

### 설정

* `config/index.js` 

```javascript
dev: {
  devtool: 'source-map'
}
```

* `webpack.dev.config.js` 

```javascript
devtool: 'source-map'
```

* 디버그설정 

좌측메뉴에 디버그 메뉴 클릭 

좌측상단에 디버그 구성 선택 > `launch.json`파일 생성됨 

```json
{
  "version": "0.2.0",
    "configurations": [
        {
            "type" : "chrome",
            "request" : "launch",
            "name" : "vuejs: chrome",
            "url" : "http://localhost:3000",
            "webRoot" : "${workspaceFolder}/src",
            "breakOnLoad" : true,
            "sourceMapPathOverrides": {
                "webpack:///src/*": "${webRoot}/*"
            }
        }
    ]
}
```

### 디버그 실행

* 중단점 설정
* 프로젝트 실행

```
> npm start 
```

* 디버그 실행

디버그 화면에서 실행버튼 클릭