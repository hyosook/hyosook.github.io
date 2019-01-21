---
title : vue.js 파일업로드
tags : ["file","vue.js"]
---



# input file v-model

### v-model

- **예상됨:** 컴포넌트 폼 인풋 엘리먼트 또는 출력 값에 따라 다릅니다.
- **제한사항:**
  - `<input>`
  - `<select>`
  - `<textarea>`
  - components
- **수식어:**
  - [`.lazy`](https://kr.vuejs.org/v2/guide/forms.html#lazy) - `input`대신 `change` 이벤트를 듣습니다.
  - [`.number`](https://kr.vuejs.org/v2/guide/forms.html#number) - 문자열을 숫자로 변경합니다.
  - [`.trim`](https://kr.vuejs.org/v2/guide/forms.html#trim) - 입력에 대한 trim 을 합니다.



### file 

* 기존 v-model 사용 불가 

* 방법

  * ftl

  ```html html
   <template>
       <label class="file-select">
            <div class="select-button">
              
               <span v-if="value">Selected File
             name : {{value.name}}
             size: {{value.size}}
                  type: {{value.type}}
                  relativePath  : {{ value.webkitRelativePath }}
              
              </span>
              <span v-else>Select File</span>
            </div>
         
          <input type="file" @change="fileUpload($event)">
         
       </label>
   </template>	
  ```

  ​

  * vue js

  ```javascript vue.js
   data: {
          value: File
      },
      
       fileUpload(e) {
           const config = { headers: { 'Content-Type': 'multipart/form-data' } };
              this.value=e.target.files[0];
              var data = new FormData();
              data.append('uploadFile',  this.value);
              data.append('projectCode', projectCode);
              alert(projectCode);

              axios.put( '/api/v1/files/' + projectCode,data,config)
                  .then(res => {
                      /*this.projectResults = res.data;*/
                  })
                  .catch(err => {
                      console.log(err);
                  });

          }
  ```

* ​


| @Controller        | 해당 클래스가 Controller임을 나타내기 위한 어노테이션       |
| ------------------ | ---------------------------------------- |
| @RequestMapping    | 요청에 대해 어떤 Controller, 어떤 메소드가 처리할지를 맵핑하기 위한 어노테이션 |
| @RequestParam      | Controller 메소드의 파라미터와 웹요청 파라미터와 맵핑하기 위한 어노테이션 |
| @ModelAttribute    | Controller 메소드의 파라미터나 리턴값을 Model 객체와 바인딩하기 위한 어노테이션 |
| @SessionAttributes | Model 객체를 세션에 저장하고 사용하기 위한 어노테이션         |
| @RequestPart       | Multipart 요청의 경우, 웹요청 파라미터와 맵핑가능한 어노테이션(egov 3.0, Spring 3.1.x부터 추가) |
| @CommandMap        | Controller메소드의 파라미터를 Map형태로 받을 때 웹요청 파라미터와 맵핑하기 위한 어노테이션(egov 3.0부터 추가) |
| @ControllerAdvice  | Controller를 보조하는 어노테이션으로 Controller에서 쓰이는 공통기능들을 모듈화하여 전역으로 쓰기 위한 어노테이션(egov 3.0, Spring 3.2.X부터 추가 |

