프론트-서버 분리하여 개발하면 동일 출처 정책에 위배됨으로 CORS 문제가 발생할 수 있다.

## Same-origin policy (SOP)
프로토컬, 도메인, 포트가 모두 같을시 동일 출처 정책을 따른다
보통 api 사용할때, ajax에서 same-origin policy 를 따르지 않으면 오류가 나며 api 사용이 거절된다.
한 사이트에서 여러 도메인을 가지고 있을 경우, 동일 출처 정책을 따르지 않기 때문에 문제가 생긴다.  

## CORS (Cross-origin resource sharing) 
자원을 외부 도메인에서 접근을 허용해주는 메커니즘
Same-origin policy와 반대되는 개념

### CORS동작 과정
1. pre-flight request : OPTIONS 메소드 요청을 수행하고, 가능한지 확인
2. 실제 요청 



## Spring에서 해결 방법
보통 서버측에서 크로스도메인 설정하여 문제를 해결하는 것이 표준화된 방법이다.
기존에는 Filter 이용했지만, Spring 4.2 이후 버젼에서는 `@CrossOrigin` 사용 가능

### 컨트롤러에 클래스 단위로 `@CrossOrigin` 어노테이션 사용
```java java
@CrossOrigin(origins="*") // 혹은 도메인 설정
@RestController
public class CommonController {

}
```
특정 컨트롤러 클래스에 대해서만 설정한다.

### Global CORS 설정
```
Access-Control-Allow-Origin : 요청을 보내는 페이지의 출처 
Access-Control-Allow-Methods : 요청을 허용하는 메소드 
Access-Control-Max-Age : 클라이언트에서 preflight의 요청 결과를 저장할 시간 지정. 해당 시간 동안은 pre-flight를 다시 요청하지 않음.
Access-Control-Allow-Headers : 요청을 허용하는 헤더
```

```java java
@Configuration  
@EnableWebMvc  
public class WebConfig implements WebMvcConfigurer {  
  @Value("${mapping.url}")  
  private String mappingUrl;  
  
  @Override  
  public void addCorsMappings(CorsRegistry registry) {  
  registry.addMapping(mappingUrl)  
  .allowedOrigins("*")  
  .allowCredentials(true)  
  .allowedMethods("POST", "GET", "PUT", "PATCH", "DELETE", "OPTIONS")  
  .allowedHeaders("Content-Type", "Authorization");  
  }  
```
전체적으로 접근이 가능하도록 global로 설정했는데, 계속 pre-filght access 실패했다는 에러메시지가 발생했다. 

## Spring Security 에서 CORS 설정로 변경
시큐리티를 사용중이라면, CORS를 필터만으로 적용하면 안된다는 글이 있었다. 
```java java
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Override  
	protected void configure(HttpSecurity http) throws Exception {  
	  http  
	      .cors()  // 1
		  .and() 
		  // 중략..
		  .authorizeRequests()  
		  .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()	// 2
		  .antMatchers("/**")
		  .hasAnyRole("USER")
		  .anyRequest()  
		  .authenticated();
	}  
	  
	@Bean	// 3
	public CorsConfigurationSource corsConfigurationSource() {  
	  CorsConfiguration configuration = new CorsConfiguration();  
	  configuration.addAllowedOrigin("*");  
	  configuration.addAllowedMethod("*");  
	  configuration.addAllowedHeader("*");  
	  configuration.setAllowCredentials(true);  
	  configuration.setMaxAge(3600L);  
	  UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();  
	  source.registerCorsConfiguration("/**", configuration);  
	  return source;  
	}
}
```
필요에 의해서 corsConfigurationSource 부분의 origins, method, header 등을 설정한다. 
