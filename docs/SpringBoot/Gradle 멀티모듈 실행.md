MSA를 많이 사용하게 되면서 한 프로젝트 안에 멀티 모듈을 구현해서 서로 상호작용하는 프로젝트 형태가 된다. 

### Common 모듈
- 공통 모듈
- `XXApplication.java`가 없음 
- bean 주입
- Util 성 구현부 
- 다른 모듈에 의존성 없음

#### build.gradle
```
bootJar {  
  enabled = false  
}  
  
jar {  
  enabled = true  
}
```
실행하는 모듈과 달리 `adminCombineJar..` 이런 부분이 없음 

### Admin 모듈

#### build.gradle
```
dependencies {
	compile project(':entity')  
	compile project(':common')
	..
}
```
사용할 멀티 모듈 프로젝트 명을 작성 

#### XXApplication.java
```java
@SpringBootApplication(scanBasePackages =  
  {"kr.co.apexsoft.granet2.admin_api", "kr.co.apexsoft.gradnet2.common"})  
@EntityScan("kr.co.apexsoft.gradnet2.entity")  
@EnableJpaRepositories("kr.co.apexsoft.gradnet2.entity")  
public class AdminApiApplication {  
  
  public static void main(String[] args) {  
  SpringApplication.run(AdminApiApplication.class, args);  
  }  
  
}
```
`scanBasePackages` 부분이 없으면 실행했을 때 에러가 발생한다. 
이부분이 있어야 다른 모듈의 bean 주입을 사용가능하다. 
