# security 

> 1. 스프링은 크게 2가지인터페이스를 구현하여 인증/인가 처리를 도와준다. (AuthenticationProvider,UserDetailsService)
> 2. UserDetailsService 는 데이터베이스의 유저정보를 불러오는 역활을한다
> 3. AuthenticationProvider 는 UserDetailsService에서 리턴해준 유저정보와 사용자가 입력한 유저의 비밀번호를 매칭하여 로그인 인증처리를 한다

## Spring security + jwt 
1. UserDetailsService (및 UserDetails)
2. Authentication (jwt 를 사용 할 것이다)
3. Filter (인증을 위한 사용자 구현 필터)

## WebSecurityConfig

* `  @EnableWebSecurity`  &  `WebSecurityConfigurerAdapter`  상속 configure 메서드를 오버라이딩
  *  웹기반 보안을 위해 함께 작동 
    * 우리의 어플리케이션에 어떤 URL 로 들어오던간에 사용자에게 인증을 요구할수있음.
    * 사용자이름과 비밀번호 및 롤기반으로 사용자를 생성할수있다.
    * HTTP Basic 과 폼기반 인증을 가능하게함.
    * 스프링 시큐리티는 자동적으로 로그인페이지,인증실패 url 등을 제공한다.

```java
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true
)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Profile("!basicAuth")
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
       
    }
}



```

*   AuthenticationManagerBuilder.userDetailsService().passwordEncoder()를 통해 패스워드 암호화에 사용될 PasswordEncoder 구현체를 지정할 수 있습니다

```java
@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder());
    }
```

* AuthenticationManager

```java
  @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
```

* Spring Security에서 인증 사용할때 최상위 인터페이스 AuthenticationProviderManager Interface를 사용한다. 이인터페이스는 단순하게 아래와같이 정의 되어있다.
* 스프링 시큐리티가 사용자를 인증하는 방법이 담긴 객체 
* configure 정책 설정 

```java
@Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                    .and()
                .csrf()
                    .disable()
                .addFilterBefore(jwtAuthenticationFilter, AnonymousAuthenticationFilter.class)
                .exceptionHandling()
                    .authenticationEntryPoint(unauthorizationHander)
                    .accessDeniedHandler(accessDeniedHandler)
                    .and()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                .headers()
                    .frameOptions()
                    .sameOrigin()
                    .and()
                .authorizeRequests()
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                    .antMatchers(
                            "/",
                            "/favicon.ico",
                            "/**/*.png",
                            "/**/*.css",
                    ).permitAll()
                    .antMatchers(
                            "/users/sign-up",
                            "/register/**",
                            "/login",
                            "/global/**",
                            "/h2-console/**"
                    ).permitAll()
                    .antMatchers("/post/**")
                    .hasAnyRole("USER")
                    .anyRequest()
                        .authenticated();

        http
                .authorizeRequests()
                .antMatchers("/admin/**")
                .hasRole("ADMIN");

    }
```



## CustomUserDetailsService

> UserDetailsService 주입
>
> configure(AuthenticationManagerBuilder authenticationManagerBuilder) 메소드에서 사용자 인증 부분으로 사용하게 된다

```java

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CustomUserDetailsService implements UserDetailsService {
    @NonNull
    private UserService userService;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String userId) {
        User user = this.userService.findByUserId(userId);

        return new UserPrincipal(user.getUserNo().getId(),
                user.getUserId(), user.getPassword(), user.getRoles(), true, user.checkActiveUser());
    }
}

```



## JwtAuthenticationFilter 

configure(HttpSecurity http) 메소드에서 pre-filter 로 동작하게 된다