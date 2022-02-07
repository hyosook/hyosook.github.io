Singletoon scope

빈을 등록할때 아무런 설정을 하지않으면 기본적으로 빈은 싱글톤 scope을 갖는다 

어플리케이션 전반에 걸쳐 해당 빈의 인스턴스를 오직 하나만 생성해서 사용하는것 



## component-scan 이란?

- **빈으로 등록 될 준비를 마친 클래스들을 스캔하여, 빈으로 등록해주는 것이다.**

@Component  어노테이션을 빈 등록 대상으로 포함 

```
@Controller`, `@Service`,  `@Repository 은 @Component  을 포함하고있음 
```

 ## 설정 방법

```java
@Configuration
@ComponentScan(basePackages = "com.rcod.lifelog")
public class ApplicationConfig {
}
```

`@Configuration` 은 이 클래스가 xml을 대체하는 설정 파일임을 알려준다.

`@ComponentScan`을 통하여`basePackages`를 설정해준다.





싱글톤 패턴

객체 지향 프로그램에서 인스턴스를 단 한번만 생성하는 디자인 패턴을 싱글톤 패턴











