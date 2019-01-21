---
title : web 서버
tags : ["web","server"]
---



### web 서버

* 웹 브라우저와 같은 클라이언트로부터 HTTP 요청을 받아들이고, HTML 문서와 같은 웹 페이지를 반환하는 컴퓨터 프로그램

  * 초기 웹은 정보교환이 목적이었기 때문에 전송할 데이터가 정적인 데이터뿐이었다. (html, img, xml...) 

    >따라서 그에 맞는 기능에 충실하였고, 지금도 정적인 데이터를 전송하는데는 웹서버가 안정적이다
    >
    > 웹서버의 표준으로 불리는 것이 있다. 바로 'Apache'이다. 

* 시간이 흐르면서 웹을 사용하는 용도는 변화했습니다. 

  >데이터를 주고 받으면서 화면이 동적으로 움직여야 했습니다. 
  >
  >입력된 값에 따라 출력된 결과가 나타나는 것이죠. 
  >
  >그런 요구에 따라 나타난게 Web Application Server입니다.

**핵심정리 : apache = 웹서버**



### Web Application 서버

- WAS

- 동적인 변화를 위해 만들어진 서버입니다. 

  > 웹서버는 정적인 데이터를 처리했다면, WAS는 동적인 데이터를 담당하겠죠. 

- 사용자의 요청으로 데이터의 조작이 가능하고 그에 따라 화면의 동적으로 변하는 것을 가능하게 도와주는 서버입니다. 

- 동적인 데이터 뿐만 아니라 정적 데이터도 처리가 가능하다는 것입니다. 

  > 그러나 큰 프로젝트나 많은 사용자를 가진 웹서비스는 흔히 웹서버와 WAS를 함께 사용합니다.
  >
  > 그 이유는 각각의 역할을 구분하여 더 효율적인 일을 하기 위해서이죠. 
  >
  > 위에서도 밝혔듯이 웹서버는 정적인 데이터를 WAS에 비해 더 안정적이고 빠르게 다룰 수 있습니다. 
  >
  > 그런 이유로 apache 와 WAS를 연동하여 사용하는 경우가 대부분입니다

  ​

**핵심정리 :  Web Application Server (웹서버 + 웹 컨테이너) = tomcat(WAS)**

![img](http://cfile29.uf.tistory.com/image/21433D4558DB217F0DACFA)

- Web서버와 Web Container의 역할을 둘다 감당하고 있는 것을 볼 수 있습니다.

- tomcat만으로도 웹서비스를 할 수 있습니다. 

- 그러나 웹서버 apache와 함께 연동해서 역할을 구분해주면 더욱 강력하게 사용할 수 있습니다.

  ​

#### Web Container

- 웹컨테이너는 자바언어에서 사용하는 개념입니다. 

- 서블릿(Servlet) & JSP를 구동하기위해 필요한 것이 웹컨테이너이죠. [(서블릿에 대한 더 자세한 설명](http://viewa.tistory.com/39)[)](http://viewa.tistory.com/39) 

  > 서블릿은 자바로 작성되어 있기 때문에 따로 이를 구동시켜줄 프로그램이 필요합니다. 그것이 바로 웹컨테이너라 이해하며 되겠습니다.

  ​

### Web 서버, WAS의 차이

**웹서버**는 정적인 데이터 (html, img, xml...) 를 처리합니다. 웹서버를 통하면 WAS 보다 빠르고 안정적입니다. 

**WAS**는 동적인 데이터를 처리하는 서버이고 데이터베이스에 연결되어 데이터를 주고받아 웹어프리케이션을 운영합니다. 



#### IOC 제어의 역전

- 객체 생성을 직접 하지 않고 WAS(Web Application Service / 톰캣)  또는 프레임워크(스프링)가 생성해주는 객체를 가져다 사용 하는 것

- WAS는 대부분 서블릿 객체를 자동으로 생성  Spring은 설정파일에 만든 Bean을 자동으로 생성

- Ioc를 이용하게 되면 생성과 소멸을 WAS나 프레임워크가 담당하게 되어  싱글톤을 만드는 코드나 메모리 정리 등의 코드를 우리가 하지 않도 되기 때문에  관리가 편하다

  #### @Component

- @Contoller, @Service, @Repository의 상위 개념으로 자동으로 Bean을 등록하고 싶은 곳에 사용된다.

- 객체를 등록하고 싶은 클래스 위에 썾면 Bean으로 등록 해달라는 표시

-  빈으로 등록할 Class에 선언하는 annotation

-  @Configuration의 @ComponentScan에 의해서 scan되고 빈으로 등록 됨

- Stereotype annotation >  @Component를 용도에 따라 미리 정형화 해놓은 타입 

  stereotype들은 내부적으로 @Component를 가지고 있음
  > @Repository - DAO 또는 레포지토리 클래스에 사용 >예외를 모두 DataAccessException으로 변환 처리
  >
  > @Service - 서비스 계층의 클래스에 사용
  >
  > @Controller - 프레젠테이션 계층의 MVC 컨트롤러에 사용
  >
  > presentation layer의 controller에 적용
  >
  > spring web servlet에 의해 웹 요청을 처리하는 controller bean 선정

##### 객체풀

- 부팅과 동시에 자신이 가져야할 객체를 생성(스프링빈)한다. 객체를 쌓아두었다가 필요할 때 주입.
- 쌓아둔 모양이 콩처럼 겼다고 해서 beans.



#### DI (의존성 주입)  Dependence Injection

- 원하는 부품 목록을 작성하면, Spring이 조립해준다.

- 필요한 객체는 스프링을 통해서 주입받는 구조.

- xml이나 어노테이션(@)를 통하여 객체 간 의존 관계를 설정.

- 객체 자체를 직접 생성 또는 검색 할 필요가 없다

  #### @Autowired

  ● 컨테이너에 들어있는 빈 객체 중에서 타입이 일치하는 빈 객체를 주입시킴

  ● 타입이 일치하는 빈 객체가 없으면 ? ! ? ! ?

  → 에러 !! 해당 빈 객체 자체가 등록이 안되고, 그러면 컨테이너 자체가 빌드가 안됨

  → 에러는 안띄우고 그냥 주입없이 가고 싶으면 .. ? required값을 false로 지정   :   @Autowired(required = **false**)

  컨테이너가 생성안돼서 생기는 BeanCreateException 에서 생성은 되지만 넣을게 없는 NullPointerException

  ​

  ​

  ​

  ​



# spring boot

### 최소한의 노력으로 spring 기반의 프로젝트를 시작 할 수 있도록 도와주는 기술



## 사용 이유

* 단독으로 상용제품 수준의 스프링 기반 애플리케이션

  > 자바 기반에서는 WAS (웹 컨테이너 / 웹 서버 역활 )가 없이는 웹 애플리케이션이 구동하지 않았습니다.
  >
  > 하지만, Spring Boot를 사용하면 이런 복잡한 WAS 설치나 배포 없이 stand-alone 으로 웹 프로젝트를 생성하고 
  >
  > java –jar 명령어를 통해 tomcat이나 jetty를 내장한 상태로 웹 애플리케이션을 실행할 수 있습니다

* 최소한의 설정으로 spring 플랫폼과 서드파티 라이브러리 사용

  > 구성시 설정을 위한 xml  코드를 생성하거나 요구하지 않음



### Java-based configuration

* 메인 클래스에  [@Configuration](mailto:%EB%A9%94@Configuration) 과 main method를 두자



* @ImportResource

  > xml 설정을 import

* @EnableAutoConfiguration

  > classpath 의 jar dependencies를 참고해서 자동으로 설정을 해준다.
  >
  > 현재 자동 설정된 내용을 알고 싶으면  스프링부트를 **--debug **모드로 시작하면 콘솔 로그를 통해 확인 가능 하다
  >
  > Classpath의 내용에 기반해서 알아서 설정 작업들을 해준다.
  >
  > 즉, classpath에 tomcat-embed-core.jar 가 존재 하면 톰캣 서버가 세팅되고, spring-webmvc.jar가 존재하면 자동으로
  >
  > web.xml을 생성해 DispatcherServlet을 등록해준다.
  >
  > =

* **@RestController  = @Controller + @ResponseBody**

  별도의 @RequestMapping된 메소드들 마다 일일이 @ResponseBody를 붙이지 않아도 된다.

  ​

### yml

#### yml 설정하기

```yaml yml
---
spring.profiles: aws
file.rootDir: gradnet
pay:
    lgdacom: /opt/lgdacom
    casnoteurl: https://casnote.gradnet.co.kr/yonsei/casnote/notice

---
spring.profiles: rds
spring.datasource:
    type: org.apache.tomcat.jdbc.pool.DataSource
    driver-class-name: com.mysql.jdbc.Driver
---
spring.profiles: prod
pay.platform: service
aws.s3.bucketName: apexsoft.service.gradnet.upload
```

* --- 구분 된다

#### 사용

* vm argument 에 정보를 넣는다

```java

-Dspring.profiles.active=aws,rds,prod

$ java -jar -Dspring.profiles.active=aws,rds,prod -Dfile.encoding=UTF-8 -jar build/libs/gradnet-0.0.1-SNAPSHOT.jar



```
