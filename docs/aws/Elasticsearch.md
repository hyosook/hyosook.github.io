# Elasticsearch

## 1. Elasticsearch란

> Elastic에서 제공하는 확장성이 뛰어난 오픈 소스 텍스트 검색 및 분석 엔진입니다. 
>
> 대량의 데이터를 신속하고 거의 실시간으로 저장, 검색 및 분석 할 수 있습니다.
>
>  일반적으로 복잡한 검색 기능과 요구 사항이 있는 응용 프로그램을 구동하는 기본 엔진 / 기술로 사용됩니다.





## 개념 

> db와 컨셉이 비슷 하다

| **ElasticSearch** | **DB**   |
| ----------------- | -------- |
| Index             | Database |
| Type              | Table    |
| Document          | Row      |
| Field             | Column   |
| Mapping           | Schema   |

다만 저장 방식은 DB와 차이를 보인다.

RDB의 경우 document=Item로 저장되어, 특정 Item을 검색하기 위해서는 모든 document를 조회하며 조건에 맞는 Item을 검색하는 반면,

Elasticsearch의 경우 Item=document의 방식으로 저장되어, 특정 Item을 가지는 document가 이미 저장 되어있어 검색시 매우 빠른 속도를 가진다. 

### JSON 기반의 스키마 없는 저장소

elasticsearch는 검색엔진이지만, NoSQL처럼 사용할 수 있다. 데이터 모델을 JSON으로 사용하고 있어서, 요청과 응답을 모두 JSON 문서로 주고받고 소스 저장도 JSON 형태로 저장한다. 스키마를 미리 정의하지 않아도, JSON 문서를 넘겨주면 자동으로 인덱싱한다. 숫자나 날짜 등의 타입은 자동으로 매핑한다.

### Multi-tenancy

elasticsearch는 multi-tenancy를 지원한다. 하나의 elasticsearch 서버에 여러 인덱스를 저장하고, 여러 인덱스의 데이터를 하나의 쿼리로 검색할 수 있다. <예제 1>의 경우 날짜별로 인덱스를 분리해 로그를 저장하고 있고, 검색 시에는 검색 범위에 있는 날짜의 인덱스를 하나의 쿼리로 요청하고 있다.

**6. 역색인**

그런데 Elasticsearch는 왜 빠를까요??

그 이유는 inverted index( 역색인 )에 있습니다.



먼저 index와 inverted index의 차이에 대해 알아보겠습니다.

쉽게 말해서 책에서 맨 앞에 볼 수 있는 목차가 index이고,

책 맨 뒤에 키워드마다 찾아볼 수 있도록 찾아보기가 inverted index입니다.



![img](https://t1.daumcdn.net/cfile/tistory/99CC10405C98CC4826)            ![img](https://t1.daumcdn.net/cfile/tistory/996B89405C98CC491E)

​                                              [ Index( 색인 ) - 목차 ]                                                       [ Reverted Index( 역색인 ) - 찾아보기 ]





Elasticsearch는 텍스트를 파싱해서 검색어 사전을 만든 다음에 inverted index 방식으로 텍스트를 저장합니다.



"Lorem Ipsum is simply dummy text of the printing and typesetting industry"

예를 들어, 이 문장을 모두 파싱해서 각 단어들( Lorem, Ipsum, is, simply .... )을 저장하고,

대문자는 소문자 처리하고, 유사어도 체크하고... 등의 작업을 통해 텍스트를 저장합니다.

때문에 RDBMS보다 전문검색( Full Text Search )에 빠른 성능을 보입니다.





## 3. 만들기 

[https://mykumi.tistory.com/entry/AWS-ElasticSearchKibanaFluentd-1-AWS-ElasticSearch-%EC%83%9D%EC%84%B1?category=503302](https://mykumi.tistory.com/entry/AWS-ElasticSearchKibanaFluentd-1-AWS-ElasticSearch-생성?category=503302)