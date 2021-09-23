# GrqphQL

* ## REST API와 비교

  - REST API는 URL, METHOD등을 조합하기 때문에 다양한 Endpoint가 존재 합니다.
    - 반면, gql은 단 하나의 Endpoint가 존재
    - 따라서, gql API를 사용하면 여러번 네트워크 호출 필요 없이, 단 한번의 네트워크 호출로 처리가 가능함





1. HTTP 요청의 횟수를 줄일 수 있다.

   - RESTful 은 각 Resource 종류 별로 요청을 해야하고, 따라서 요청 횟수가 필요한 Resource 의 종류에 비례한다.
     반면 GraphQL 은 원하는 정보를 하나의 Query 에 모두 담아 요청하는 것이 가능하다.

   

   ### 그럼 어떨 때 사용하는것이 좋은가?

   그렇다면 GraphQL 과 RESTful 중 어떤 것을 선택해서 사용해야하는가?
   다음과 같은 기준으로 선택하면 될 것이다.

   1. GraphQL
      - 서로 다른 모양의 다양한 요청들에 대해 응답할 수 있어야 할 때
      - 대부분의 요청이 CRUD(Create-Read-Update-Delete) 에 해당할 때
   2. RESTful
      - HTTP 와 HTTPs 에 의한 Caching 을 잘 사용하고 싶을 때
      - File 전송 등 단순한 Text 로 처리되지 않는 요청들이 있을 때
      - 요청의 구조가 정해져 있을 때

   그러나 더 중요한 것은, **둘 중 하나를 선택할 필요는 없다**는 것이다.