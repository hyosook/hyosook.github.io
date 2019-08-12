##  jackson 이란?

jackson은 자바진영 json 라이브러리로 잘 알려져 있지만 json 뿐만 아니라 XML, YAML, CSV 등 다양한 형식의 데이타를 지원하는 data-processing 툴이다.

스트림 방식이므로 속도가 빠르며 유연하며 다양한 third party 데이터 타입을 지원하며 annotation 방식으로 메타 데이타를 기술할 수 있으므로 JSON 의 약점중 하나인 문서화와 데이타 validation 문제를 해결할 수 있다.





##  어노테이션

- @JsonIgnore : VO의 멤버변수 위에 선언해서 제외처리 (비밀번호 같은거)

- @JsonProperty : json으로 변환 시에 사용할 이름이다. (DB 칼럼과 이름이 다르거나 api 응답과 이름이 다르지만 매핑시켜야 할 때)

- @JsonGetter : 어떤 필드값을 가져올 떄 이 메소드로 접근해서만 가져오라고 jackson에게 명시적으로 알린다.

- @JsonSetter : 위와 동일, Deserialize 때


