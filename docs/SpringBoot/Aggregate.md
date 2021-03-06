# Aggregate

관련 객체를 하나로 묶은 군집

## **정의**

- 애그리거트는 관련 객체를 하나로 묶은 군집이다.

  - ENTITY와 VALUE OBJECT의 묶음
  - 애그리거트에 속한 객체는 유사하거나 **동일한 라이프 사이클**을 가진다.

- 애그리거트에서 엔터티는 **루트 엔터티 하나**이다. 그러나, 필요에 따라 루트 엔터티 외에 다른 엔터티가 나올 수도 있다.

  - 이론상으로 루트 엔터티만 두도록 권장하기 때문에 다른 엔터티가 있다면, 진짜 엔터티인지 혹은 애그리거트가 잘못 구성되었는지 꼭 의심해야 한다.

- AGGREGATE 1개당 REPOSITORY 1개

  - **루트 엔터티 하나**를 통해서 밖에서 AGGEGATE 안의 객체로 접근함
  - 애그리거트 내의 엔티티 및 밸류 객체의 데이터 수정은 애그리거트 루트를 통해서만 수행한다.

  

## **식별시 의식할 점**

- CUD + 단순R(findById)에 집중
  - 모든 R을 다 포용하려고 한다면 깊은 객체 그래프가 나옴
- Cascade를 써도 되는 범위인가



## **Aggregate 간의 참조**

- 다른 AGGREGATE의 Root를 직접 참조하지 않고 ID로만 참조



## **여러 AGGREGATE에 걸친 조회 방법**

### **1. Service 레이어에서 조합**

- DB 성능에 더 유리할 수 있음
  - 각각의 쿼리가 단순해짐
  - Application/DB 레벨의 캐쉬에 더 유리함

### 2. Join 사용

- WHERE절에 다른 AGGRAGATE의 속성이 필요한 경우 
  - Repository에 조회조건 정도를 추가하고 Service단에서 다른 AGGREGATE을 다시 을 다시 조합할 수도 있음
- SELECT 결과까지 다른 AGGRAGATE의 속성을 포함할 경우
  - 전용 DTO를 만들어서 처리



## **Lazy loading 다시 생각하기**

- AGGREGATE를 정리하고 복합조회용 객체를 따로 정의하면 Lazy loading이 필수일지 한번 더 생각해볼 수 있음
- 반대로 Lazy loading이 있어서 깊은 객체 그래프의 ENTITY를 설계하는 유혹에 빠질 수도 있음
- Lazy loading이 필요하다는 것은 모델링을 다시 생각해봐야한다는 신호일수도 있음