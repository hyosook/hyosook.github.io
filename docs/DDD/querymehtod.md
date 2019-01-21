---
title : query mehtod
tags : ["DB","JPA"]
---



**Repository 제공 기능**

| method 이름 | 기능                                      |
| --------- | --------------------------------------- |
| save()    | 레코드 저장 (insert, update)                 |
| findOne() | primary key로 레코드 한건 찾기                  |
| findAll() | 전체 레코드 불러오기. 정렬(sort), 페이징(pageable) 가능 |
| count()   | 레코드 갯수                                  |
| delete()  | 레코드 삭제                                  |



**query mehtod 이름 생성 전략**

아래 이름으로 시작하는 메서드는 query method 임을 스프링에게 알린다.

| **mthod 이름 ** | **설명 **                    |
| ------------- | -------------------------- |
| findBy로 시작    | 쿼리를 요청하는 메서임을 알림           |
| countBy로 시작   | 쿼리 결과 레코드 수 요청하는 메서드 임을 알림 |



필드 쿼리 표현식**

위의 findBy… 에 이어 아래와 같이 해당 repository 도메인의 필드이름을 입력하면 검색 쿼리를 실행한 결과를 전달한다. SQL의 where 절을 메서드이름을 통해서 전달한다고 보면 된다.

메서드의 반환형이 도메인 객체이면 하나의 결과만 전달하고

반환형이 List 이면 쿼리에 해당하는 모든 객체를 전달한다.

**query method에 포함할 수 있는 키워드**

| 메서드이름 키워드        | 샘플                                       | 설명                   |
| ---------------- | ---------------------------------------- | -------------------- |
| And              | findByEmailAndUserId(String email, String userId) | 여러필드를 and 로 검색       |
| Or               | findByEmailOrUserId(String email, String userId) | 여러필드를 or 로 검색        |
| Between          | findByCreatedAtBetween(Date fromDate, Date toDate) | 필드의 두 값 사이에 있는 항목 검색 |
| LessThan         | findByAgeGraterThanEqual(int age)        | 작은 항목 검색             |
| GreaterThanEqual | findByAgeGraterThanEqual(int age)        | 크거나 같은 항목 검색         |
| Like             | findByNameLike(String name)              | like 검색              |
| IsNull           | findByJobIsNull()                        | null 인 항목 검색         |
| In               | findByJob(String … jobs)                 | 여러 값중에 하나인 항목 검색     |
| OrderBy          | findByEmailOrderByNameAsc(String email)  | 검색 결과를 정렬하여 전달       |

생각할 수 있는 거의 모든 연산자가 가능하다. 예를들어

List<User> **findFirst10ByNameAndAgeGreaterThanEqualOrderByBirthday**(String name, int age);



### 페이징

**spring data jpa의 web 프로젝트 지원**

query method의 입력 변수로 Pageable을 추가하면 Page 타입을 반환형으로 사용할 수 있다.

Pageable 객체를 통해 페이징과 정렬을 위한 파라미터를 전달한다.

아래와 같이 controller에서 부터 Pageable을 전달받는다.

![img](http://cfile27.uf.tistory.com/image/241A7336576804DC04E04F)



| query parameter 명 | 설명                                       |
| ----------------- | ---------------------------------------- |
| page              | 몇번째 페이지 인지를 전달                           |
| size              | 한 페이지에 몇개의 항목을 보여줄것인지 전달                 |
| sort              | 정렬정보를 전달. 정렬정보는 필드이름,정렬방향 의 포맷으로 전달한다. 여러 필드로 순차적으로 정렬도 가능하다.예: sort=createdAt,desc&sort=userId,asc |

아래는 위 controller를 통해 http 요청으로 페이징과 정렬된 데이터를 전달 받는 URI 샘플

**GET /users?page=1&size=10&sort=createdAt,desc&sort=userId,asc**

위와 같이 웹 페이지 개발에 필수적인 정렬과 페이징정보를 접속 url에서 부터 Repository 객체가지 바로 전달이 가능하다.

```java
 PageRequest pageRequest = new PageRequest(page, 10);

        Page<Project> result = projectRepository.findAll(pageRequest);
```

