# DDD가 화두가 된 이유 

MSA 이전엔 **모놀리식 아키텍처**(Monolithic Architecture) 였다. 모놀리식 아키텍처에서 **MSA** 로 전환해야 하는 이슈가 생겼다. 

하나의 큰 아키텍처를 어떤 기준으로 **나누어야 하는데**,  그 **기준**이 무엇인지가 큰 이슈였다. 

DDD(도메인 주도 설계) 에서 이미 그 해결법을 다루고 있었고, 화두가 되었다. 



## Monolithic Architecture

**하나**의 서비스 또는 어플리케이션이 **거대한 아키텍처**를 가지는 것이다. 

- 장점
  - 개발 초기에는 단순한 아키텍처 구조와 개발의 용이함
  - 개발, 테스트 및 배포가 간편하다
- 단점
  - 규모가 커짐에 따라 복잡도가 심각하게 증가한다.
  - 상호 의존성이 높아 개발 추가 및 변경 어려움
  - 작은 변경에도 전체 프로그램을 다시 빌드, 배포 해야한다. 
  - 프로그래밍 언어와 프레임워크를 변경할 수 없다.

![](https://imgur.com/RRseBH2.png)



## MSA (Microservice Architecture)

큰 애플리케이션 하나를 **여러 개의 작은 서비스**로 나누고, 이 서비스를 **조합**해 비즈니스 로직을 수행하는 아키텍처이다. 
서비스를 기준으로 팀을 나눈다. 각 팀에서 UI~DB까지 전담한다. 

- 장점
  - 정교한 확장성
    - 서비스별로 나누어 모듈성이 향상된다.
  - 분산화된 데이터 관리 : 서비스마다 별도의 데이터베이스를 사용한다. (라이프사이클을 가진다.)
  - 개발 생산성 증가
    - 서비스를 충분히 작은 크기로 나누어 독립적으로 개발
    - 개발자가  해당 비즈니스 로직에만 집중해서 개발 가능 
    - 테스트시에도 연관 마이크로서비스만 테스트를 수행 하면 되므로 일정이 줄어들수있다
- 단점 
  - 코드, 데이터 중복 문제가 생긴다.
  - 통합 테스트가 어렵다.

![](https://imgur.com/nxQ5E9q.png)





## SRP _ 단일 책임 원칙 

- 어떤 클래스를 변경해야 하는 이유는 오직 하나 뿐이다
  - 객체는 단 한 개의 책임(역할)만을 가져야 한다
  - 클래스가 제공하는 모든 서비스는 그 하나의 책임을 수행하는되 집중 되 있어야 한다 
    - 유연하고 , 확장성 높고 유지보수 하기 쉬운 객체지향 시스템의 효과를 얻을수 있다
- 객체는 유일하며, 다른 객체와 구별이 가능
  - 그들 객체만이 제일 잘 할 수있는 독립적인 책임을 가지고 있다



### CRC 카드 

- 객체가 변화의 요인이 하나뿐이라면 , SPR가 지켜진것, 이런 명제를 확인 하기 위한 방법

![](https://imgur.com/GReFxKB.png)

#### 예제 분석

![](https://imgur.com/wtBa4Ou.png)

![](https://imgur.com/nztwbVY.png)

### SPR를 지키지 않는 사례

- 속성이 SRP를 지키지 않은 경우

  - 하나의 속성이 여러 의미를 갖는 경우

- 메서드가 SRP를 지키지 않은 경우 

  - 분기 처리가 존재한다

  - ```java
    class Man {
        private boolean sex;
        
        public void setSex(boolean sex) {
        
            this.sex = sex;
        }
        
        public void chromosome() {
            
            if(sex == true) {
                System.out.println("xy입니다");
            } else {
                System.out.println("xx입니다");
            }
        }
    }
    
    ```

    - (**해결**) 공통기능 추상 클래스를 만들고 상속 받아서 사용 

    - ```java
      abstract class Man {
          abstract void chromosome();
      }
       
      class Male extends Man{
       
          @Override
          void chromosome() {
              System.out.println("xy입니다");
          }
      }
       
      class Female extends Man{
          
          @Override
          void chromosome() {
              System.out.println("xx입니다");
          }
      }
      
      ```
