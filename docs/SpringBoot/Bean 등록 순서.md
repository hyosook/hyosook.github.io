## Bean 등록 순서

*  Annotation을 이용해서 bean을 등록하게되면
* 패키지에서 존재하는 순서대로(위에서 아래) 스캔하면서 bean을 생성한다.
* 알파벳순서에서 밀린 패키지, 클래스는 생성 순서를 맞춰주지 않는 문제가 생긴다.



### Bean 순서 결정 법

**@DependsOn 애노테이션을 사용하자**

결국은 스프링한테 "이 빈(Bean)은 어떤 X라는 빈을 참조하고 있어(의존하고 있어)" 라고 알려주는 것과 같다.

> 하지만 무한루프의 에러가 생길수있따 

**@PostConstruct 애노테이션을 사용하자**

위의 애노테이션은 해당 컴포넌트가 완전히 생성된 후(주입된 후)에 한 번 실행해야할 일들을 코딩한 메소드에 붙이는 것이다.

즉, 해당 Bean이 완전히 생성된 후 무언가 작동하므로 NullPointerException이 일어나지 않는다.

물론 생성자에 붙이는 것은 여지없이 에러가 난다.

위와 같이 beanTest3.hello();가 빈이 완전히 생성된(@Autowired로 주입까지 완료된) 상태에서 실행되다보니 에러가 해결된다.

\* 참고로 이 방법이 다른 빈들에게 의존성을 부여하지도 않고 깔끔한 코드가 되기 때문에 **가장 적절한 방법**이다.



#  Bean

>  Spring Bean Container에 존재하는 객체를 말한다. 

Bean Container는 의존성 주입을 통해 Bean 객체를 사용할 수 있도록 해준다.

* Bean은 보통 싱글턴으로 존재한다.
* Bean은 기본적으로 싱글톤의 스코프를 가진다.
  *  즉, **Bean으로 지정된 Class는 Container에서 1개의 인스턴스로만 존재** 할 수 있다.
*  하지만 필요에 따라서 빈 주입마다 새로운 빈을 생성해야할 필요가 있을 경우도 있다. 이럴 경우에는 빈 생성시 Scope를 prototype으로 주면 빈 주입마다 새로운 인스턴스가 생성되는 것을 보장한다



* 스프링에서 빈을 생성할 때, 기본 전략은 모든 빈이 싱글톤으로 생성된다.

  * **Bean으로 지정된 Class는 Container에서 1개의 인스턴스로만 존재** 할 수 있다.

  *  어디에서든지 빈을 주입받는 다면 동일한 빈을 주입받는 것을 보장한다.

    

  

### spring singleton과 java static 기반 singleton 차이

> 여러 객체들이 하나의 인스턴스를 공유한다는 개념이 동일하다
>
> 프로그램 시작시 자동으로 딱 한번 



인스턴스의 생명주기 (생성, 사용, 소멸) 에서의 차이가 있따 





### SingleTon Pattern

> singleton Pattern이란 인스턴스를 1개로 제한하며, global하게 어디서든 접근 할 수 있도록 하는 객체생성 디자인패턴 입니다.

**싱글톤 패턴**

애플리케이션이 시작될 때 어떤 클래스가 **최초 한번만** 메모리를 할당하고(Static) 그 메모리에 인스턴스를 만들어 사용하는 디자인패턴.

생성자가 여러 차례 호출되더라도 실제로 생성되는 객체는 하나고 최초 생성 이후에 호출된 생성자는 최초에 생성한 객체를 반환한다. (자바에선 생성자를 private로 선언해서 생성 불가하게 하고 getInstance()로 받아쓰기도 함)

=> 싱글톤 패턴은 단 하나의 인스턴스를 생성해 사용하는 디자인 패턴이다.

(인스턴스가 필요 할 때 똑같은 인스턴스를 만들어 내는 것이 아니라, 동일(기존) 인스턴스를 사용하게함)

#### 주의

싱글톤 특성상 여러 스레드가 동시에 접근해서 사용하는 오브젝트이므로, 상태관리에 주의를 기울여야 합니다.

**상태를 갖고 있지 않은 무상태(stateless) 형태로 만들어져야 합니다.**

각 요청 정보나, 생성 정보는 **로컬변수, 파라미터, 리턴 값 등**을 이용하도록 해줍니다.



https://joont.tistory.com/144

https://jeong-pro.tistory.com/86

* Singleton Class는 본인을 호출하는 static method를 가지고 있으며, 이 method를 호출하면 본인을 반환하도록 설계됩니다.



spring의 bean들은 beanfactory에 의해서 관리되고 있습니다. 그리고 기본적으로 이러한 bean의 생명주기는 scope는 singleton을 따르고 있습니다. 즉, 우리가 getBean을 아무리 많이 하더라도 결국 1개의 인스턴스라는 이야기 입니다.









# @Configuration

- 클래스가 하나 이상의 `@Bean` 메소드를 제공하고 스프링 컨테이가 Bean정의를 생성하고 런타임시 그 Bean들이 요청들을 처리할 것을 선언하게 된다. 

- Configuration을 클래스에 적용하고 @Bean을 해당 클래스의 메소드에 적용하면 @Autowired로 빈을 부를 수 있다.

  ```java
  @Configuration
  public class AwsConfig {
          @Bean
      public AmazonS3 amazonS3Client() {}
  
  @Autowired
  private AmazonS3 amazonS3;
  ```

# @Bean  

- 개발자가 컨트롤이 불가능한 **외부 라이브러리들을 Bean으로 등록하고 싶은 경우**에 사용된다.

- @Configuration 으로 선언된 클래스 내에 있는 메소드 정의 

  ```java
    @Bean
       public ModelMapper modelMapper() {
           ModelMapper modelMapper = new ModelMapper();
           modelMapper.getConfiguration()
                   .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
                   .setFieldMatchingEnabled(true);
           return modelMapper;
       }
  
      @Bean
      public CustomModelMapper customModelMapper(ModelMapper modelMapper) {
          return new CustomModelMapper(modelMapper);
      }
  ```

  ```java
  public class CustomModelMapper {
       private final ModelMapper modelMapper;
  
       public CustomModelMapper(ModelMapper modelMapper) {
           this.modelMapper = modelMapper;
       }
  
       public static <D> D convert(Object source, Class<D> classLiteral) {
           return modelMapper.map(source, classLiteral);
       }
  
       public static <D, E> List<D> convert(Collection<E> sourceList, Class<D> classLiteral) {
           return sourceList.stream()
                   .map(source -> modelMapper.map(source, classLiteral))
                   .collect(Collectors.toList());
       }
  
       public static <D, E> Page<D> convert(Page<E> sourceList, Class<D> classLiteral, Pageable pageable) {
           List<D> list = sourceList.getContent().stream()
                   .map(source -> modelMapper.map(source, classLiteral))
                   .collect(Collectors.toList());
           return new PageImpl<>(list, pageable, sourceList.getTotalElements());
       }
   }
  ```

  

# @Component

- 개발자가 **직접 작성**한 class를 **Bean으로 등록**하기 위한 어노테이션

  ```java
  @Component
  @Slf4j
  public class CustomAccessDeniedHandler implements AccessDeniedHandler {
      @Override
      public void handle(HttpServletRequest request,
                         HttpServletResponse response,
                         AccessDeniedException e) throws IOException, ServletException {
          log.error("Access Denied Error: {}", e.getMessage());
          response.sendError(
                  HttpServletResponse.SC_FORBIDDEN,
                  MessageUtils.getMessage("UNAUTHORIZED_ACCESS")
          );
      }
  }
  ```

  

# @Autowired

- 빈 객체를 주입

- Autowired는 타입으로, Resource는 이름으로 연결해준다.

- @Autowired 애너테이션을 사용하는 변수의 타입이 Map 이거나, List 이면 제네릭 타입에 해당되는 모든 빈을 할당

  - EX)

    ```JAVA
    @Service
    @RequiredArgsConstructor(onConstructor = @__(@Autowired))
    public class DuplicateApplLocator {
    @NonNull
    private List<DuplicateService> duplicateServices;
    
    # UserIdCheckService 와  RegistrationCheckService 주입되어있다
    ```

    ```JAVA
    public class UserIdCheckService implements DuplicateService 
    
    public class RegistrationCheckService implements DuplicateService 
    
    ```

    

# @Resource

- @Autowired와 마찬가지로 빈 객체를 주입
- Resource는 이름으로 연결해준다.



## util 클래스 / 컴포넌트 / Bean

- **util 클래스** 
  - 보통 상태를 가지지 않고 
  - static 메서드만 가지며 
  - 생성자도 가지지 않는 게 보통
- 컴포넌트 
  - 개발자가 **직접 작성**한 class를 **Bean으로 등록**하기 위한 어노테이션
- Bean
  - 개발자가 컨트롤이 불가능한 **외부 라이브러리들을 Bean으로 등록하고 싶은 경우**에 사용된다.



# @RequiredArgsConstructor

- @Autowired나 @Resource @Inject 어노테이션 없이 DI 주입이 가능
- `@NonNull`이나 `final`이 붙은 필드에 대해서 생성자를 생성
  - `private final XXXService xxxService;` 
  - final로 선언해주면 해당 필드를 파라미터로 가지는 생성자가 생성

```JAVA
 /** Autowired를 사용한 의존성 주입 */
  @Controller
  public class AutowiredUsedController {
    @Autowired
    private XXXService xxxService;
  }

  /** @RequiredArgsConstructor를 사용한 의존성 주입 */
  @Controller
  @RequiredArgsConstructor
  public class RacUseController {
    private final XXXService xxxService;
  }
```



