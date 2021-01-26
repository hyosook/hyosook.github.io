# Typescript +Vue  Lint

## vscode 설치

* Eslint
* Prettier



## 환경세팅



## 프로젝트 세팅 

ESLint Configuration은 `eslintrc.*` 파일 또는 `package.json`의`eslintConfig` 필드에 작성할 수 있습니다.

ESLint의 핵심 구성 정보는 세가지 영역으로 나누어져 구성되어 있습니다.

- Environments: 스크립트 실행환경에 대한 설정, 사전 전역 변수 설정등
- Globals: 사용자가 추가하는 전역 변수
- Rules: 활성화 규칙 및 오류 수준

### 파서 옵션 (parserOptions)

`parserOptions`은 ESLint 사용을 위해 지원하려는 Javascript 언어 옵션을 지정할 수 있습니다.

- ecmaVersion: 사용할 ECMAScript 버전을 설정
- sourceType: parser의 export 형태를 설정
- ecmaFeatures: ECMAScript의 언어 확장 기능을 설정
  - globalReturn: 전역 스코프의 사용 여부 (node, commonjs 환경에서 최상위 스코프는 module)
  - impliedStric: strict mode 사용 여부
  - jsx: ECMScript 규격의 [JSX](https://facebook.github.io/jsx/) 사용 여부

```js
{
  "parserOptions": {
    "ecmaVersion": 6,
    "sourceType": "module",
    "ecmaFeatures": {
      "jsx": true
    }
  }
}
```

### 파서 (parser)

ESLint는 구문 분석을 위해 기본적으로 [Espree](https://github.com/eslint/espree) 파서를 사용합니다.
`Babel`과 함께 사용되는 파서로는`babel-eslint`가 있고 `Typescript` 구문 분석을 위해 사용되는`@typescript-eslint/parser`가 있습니다.

```js
{
    "parser": "babel-eslint",
 // "parser": "@typescript-eslint/parser"
}
```

### 프로세서 (processor)

`processor`는 다른 형식의 파일로 부터 Javascript 코드를 추출해내고, 추출한 코드를 대상으로 Lint를 수행하는 전처리기(preprocessor)와 후처리기(postprocessor)를 작성하는 용도로 사용할 수 있습니다.

```js
{
  "plugins": ["a-plugin"],
  "processor": "a-plugin/a-processor"
}
```

많이 사용되는 processor로 `eslint-plugin-markdown`이 있습니다.
이 플러그인은 eslint의 `preprocessor`와 `postproceesor`를 구현하여 markdown 문서의 Javascript 코드블록에 대해서 정적분석을 수행하고 린팅을 적용합니다.

`eslint-plugin-markdown`는 여러 확장자를 지원하고 있습니다.

```js
// eslint-plugin-markdown/src/index.js
{
  ...
  module.exports = {
    processors: {
      ".markdown": processor,
      ".mdown": processor,
      ".mkdn": processor,
      ".md": processor
    }
  }
}
```

https://github.com/eslint/eslint-plugin-markdown
https://eslint.org/docs/developer-guide/working-with-plugins

### 환경(env)

`env`는 사전 정의된 전역 변수 사용을 정의합니다.
자주 사용되는 설정으로는 browser, node가 있습니다.

```js
"env": {
  "browser": true,
  "node": true
}
```

> 사전 정의된 전역변수는 [공식 문서](https://eslint.org/docs/user-guide/configuring#specifying-environments)에서 확인할 수 있습니다.

당연한 얘기지만 browser, node 설정을 하지 않을경우 `console`,`require` 같은 사전 정의된 전역변수 환경에 있는 static 메서드를 인식할 수 없어서 에러가 발생합니다.

이외에도 선언되지 않은 전역변수를 사용하는 경우 ESLint 경고가 발생하지 않도록, `globals`를 이용하여 사용자 전역 변수를 추가할 수 있습니다.

```js
{
  "globals": {
    "$": true
  }
}
```

### 플러그인 (plugin)

ESLint는 서드파티 플러그인 사용을 지원합니다.
플러그인 패키지를 설치하고, 해당 플러그인을 `plugins`에 추가하여 사용할 수 있습니다.

```js
{
  "plugins": [
    "eslint-plugin-react"
  ]
}
```

플러그인을 추가할 때, `eslint-plugin-` 접두사는 생략이 가능합니다.
예를들어, `eslint-plugin-react`는 아래처럼 선언할 수 있습니다.

```js
{
  "plugins": [
    "react"
  ]
}
```

### 확장 (extends)

`extends`는 추가한 플러그인에서 사용할 규칙을 설정합니다.
플러그인은 일련의 규칙 집합이며, 플러그인을 추가하여도 **규칙은 적용되지 않습니다**.
규칙을 적용하기 위해서는 추가한 플러그인 중, 사용할 규칙을 추가해주어야 적용이 됩니다.

예를들어, `eslint:recommended`와 `react/recommended`를 사용할 수 있습니다.

```js
// e.g. React
{
  "plugins": [
    "react"
  ],
  "extends": [
    "eslint:recommended",
    "plugin:react/recommended" 
  ],
}
  
```

여기서 `eslint-plugin-` 접두사를 생략하고 `plugin:`를 사용하여 사용할 플러그인을 지정할 수 있습니다. `eslint:all`과 `eslint:recommended`는 ESLint에 기본으로 제공되는 확장입니다. ESLint는 `eslint:all`를 프로덕션 용도로 사용하지 않기를 권장하고 있습니다. (~~에러 지옥...~~)

> [using-eslint-all](https://eslint.org/docs/user-guide/configuring#using-eslint-all)

또, 자주 사용되는 Typescript는 `@typescript-eslint/eslint-plugin`를 사용할 수 있습니다. 이 플러그인은 접미사 `eslint-plugin`을 생략하고 설정할 수 있습니다.

```js
// e.g. Typescript
{
  "parser": "@typescript-eslint/parser",
  "plugins": ["@typescript-eslint"],
  "extends": [
    "plugin:@typescript-eslint/eslint-recommended"
  ]
}
```

### 규칙 (rules)

ESLint에는 프로젝트에서 사용하는 규칙을 수정할 수 있습니다. 규칙을 변경하는 경우, 다음과 같은 방법으로 설정해야 합니다.
-`"off"` 또는 `0`: 규칙을 사용하지 않음
-`"warn"` 또는 `1`: 규칙을 경고로 사용
-`"error"` 또는 `2`: 규칙을 오류로 사용

**규칙에 추가 옵션이 있는 경우**에는 배열 리터럴 구문을 사용하여 지정할 수 있습니다.

```js
{
  "rules": {
    "eqeqeq": "off",
    "curly": "error",
    "quotes": ["error", "double"]
  	"comma-style": ["error", "last"],
  }
}
```

플러그인에서 규칙을 지정할 때는 `eslint-plugin-`를 **반드시** 생략해야 합니다. ESLint는 내부적으로 접두사없이 이름을 사용하여 규칙을 찾습니다.

### 기타

#### 인라인으로 규칙 비활성화

```js
// 전체 파일 규칙 경고 비활성화, 파일 맨위에 아래 블록 주석 추가
/* eslint-disable */
alert('foo');

// 경고 비활성화 블록 주석
/* eslint-disable */
alert('foo');
/* eslint-enable */

// 특정 규칙 경고 비활성화
/* eslint-disable no-alert, no-console */
alert('foo');
console.log('bar');
/* eslint-enable no-alert, no-console */
```

#### 파일 그룹에 대해서만 규칙 비활성화

```js
{
  "rules": {...},
  "overrides": [
    {
      "files": ["*-test.js","*.spec.js"],
      "rules": {
        "no-unused-expressions": "off"
      }
    }
  ]
}
```

#### 파일 디렉토리 제외

`ignorePatterns` 필드 또는 `eslintignore` 파일을 작성하여 파일 및 디렉토리를 제외하도록 지정할 수 있습니다.

```js
// .eslintrc 파일 ignorePatterns 설정
{
  "ignorePatterns": ["temp.js", "node_modules/"],
    "rules": {
      //...
  }
}

//.eslintignore 파일 생성
/root/src/*.js
```

#### 대체파일 사용

`.eslintignore`를 현재 작업 디렉토리가 아닌 다른 파일을 사용하려면 `--ignore-path` 옵션을 사용하여 명령행에 파일을 지정할 수 있습니다.

```null
eslint --ignore-path .gitignore file.js
```

#### 구성 파일 사용

ESLint는 기본적으로 모든 상위 폴더에서 루트 디렉토리까지 구성파일을 찾습니다.
`.eslintrc`와 `package.json` 파일이 같은 디렉토리에 있는 경우, `.eslintrc`가 우선 순위를 갖게 되며, `package.json`은 사용되지 않습니다.
ESLint를 특정 프로젝트로 제한하는 경우, 아래 선언을 사용할 수 있습니다.

```js
// package.json
eslintConfig = {
  root: ture,
  ...
}

// .eslintrc.*
{ 
  root: true,
  ...
}
```

> root의 기본 값은 true 입니다.



https://analogcoding.tistory.com/183

### 유용한 도구

[ESLint 데모](https://eslint.org/demo)를 사용하면 UI를 이용해 ESLint 설정을 연습해볼 수 있습니다.

### 참고

[https://eslint.org](https://eslint.org/)
https://github.com/typescript-eslint/typescript-eslint#what-about-babel-and-babel-eslint
https://www.robertcooper.me/using-eslint-and-prettier-in-a-typescript-project





https://eslint.vuejs.org/rules/no-unused-vars.html









## git hook

```bash
yarn add -D lint-staged
```

```
  "gitHooks": {
    "pre-commit": "lint-staged"
  },
  "lint-staged": {
    "*.{js,ts,vue}": [
      "eslint --ext .vue,.ts,.js src/ --fix",
      "git add"
    ]
  }
```

