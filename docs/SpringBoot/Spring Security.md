---
title : spring security
tags: ["springboot", "security"] 
---



#### 1. build.gradle

```java
compile('org.springframework.boot:spring-boot-starter-security')
```

#### 2. DB, Domain에 USER와 ROLE 필요

* User

```java java
@Entity
@Table(name = "USER")
@Getter @Setter
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
                  property = "id")
public class User extends AbstractBaseEntity implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    @NotNull
    @Column(name = "user_name", unique = true)
    @Size(min = 8)
    private String userName;

    @NotNull
    @Column(name = "password")
    @Size(min = 8)
    private String password;

    @Column(name = "email")
    private String email;

    @ManyToMany
    @JoinTable(name = "user_role",
               joinColumns = @JoinColumn(name = "user_id"),
               inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;
}
```

* Role

```java java
@Entity
@Table(name = "role")
@Getter @Setter
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
                  property = "id")
public class Role extends AbstractBaseEntity implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @NonNull private String name;

    public Role() {
    }

    public Role(String name) {
        this.name = name;
    }

    @ManyToMany(mappedBy = "roles")
    private Set<User> users;
}
```



#### 3. UserServiceDetailImpl 생성

`UserDetailService`를 구현해야만 Spring Security가 정상적으로 조회 가능

`InMemoryAuthentication`설정이 없다면 기본적으로 `UserDetailsService`구현 객체를 찾아 로그인 요청시 이용한다.

`InMemoryAuthentication`설정이 있으면, `UserDetailsService`구현 객체가 있어도 해당 흐름을 타지 않는다.

```java java
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceDetailImpl implements UserDetailsService {

    @NonNull private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = this.userRepository
                            .findByUserName(username)
                            .orElseThrow(() -> new UsernameNotFoundException("사용자 이름을 찾을 수 없습니다."));

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();

        for (Role role: user.getRoles()) {
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
        }

        return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(), grantedAuthorities);

    }
}
```

* `loadUserByUsername`메소드를 반드시 구현해야한다. 스프링시큐리티에서 User 정보를 읽을 때 사용된다.
* User를 읽어왔으면 권한을 부여해준다. 





#### 4. 비밀번호 암호화

비밀번호 뿐만 아니라 개인 식별정보는 필히 암호화를 거쳐 저장하여야 한다.
암호화는  BCrypt  방식을 이용하며 Spring에서 기본적으로 제공되는 클래스는  `BCryptPasswordEncoder` 이다.
`passwordEncoder` 라는 이름의 빈으로 등록하면 자동으로 적용된다.

```java java
@Bean
public BCryptPasswordEncoder passwordEncoder() {
  return new BCryptPasswordEncoder();
}
```

회원가입 시 패스워드를 DB에 저장할때는  `PasswordEncoder` 를 통해 암호화하는 로직을 필히 명시해줘야 한다.

```java java
@Service
public class UserServiceImpl implements UserService {
  @NonNull private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public User saveUserUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        if (Objects.isNull(user.getRoles()) || user.getRoles().isEmpty()) {
            Set<Role> roles = new HashSet<>();
            roles.add(roleRepository.findByName("ROLE_USER")
                                    .orElseThrow(() -> new IllegalStateException("ROLE is not set")));
            user.setRoles(roles);
        }
        return this.userRepository.save(user);
    }
}
```



#### 5. 로그인

##### 1) WebSecurityConfig.java > 로그인 관련 부분

```java java
.formLogin()
	.loginPage("/user/view/membership/sign-in")
    .loginProcessingUrl("/user/auth/login")
    .usernameParameter("userName")
    .passwordParameter("password")
    .successHandler(loginSuccessHandler())
    .failureHandler(loginFailureHandler())
    .permitAll()
.and()
.logout()
	.logoutRequestMatcher(new AntPathRequestMatcher("/user/auth/logout"))
    .logoutSuccessHandler(logoutSuccessHandler())
```



`.loginPage("/user/view/membership/sign-in")` : 로그인 페이지 지정

`.loginProcessingUrl("/user/auth/login")` 

> 로그인을 수행하는 url 지정
>
> 해당 url  controller를 별도로 지정하지 않음
>
> `UserDetailService` 구현부분을 찾아서 로그인을 수행함

`.usernameParameter("userName")` : `User` 도메인 내 아이디 변수를 그대로 작성 

`.passwordParameter("password")` : `User` 도메인 내 비밀번호 변수를 그대로 작성

`.successHandler(loginSuccessHandler())` : 로그인 성공핸들러, 대부분 url을 리다이렉트 함

`.failureHandler(loginFailureHandler())` : 로그인 실패 핸들러, 대부분 url을 리다이렉트 함

`.logoutRequestMatcher(new AntPathRequestMatcher("/user/auth/logout"))` : method를 지정할 수 있기 때문에 csrf를 적용하고, get을 이용하는 경우에 사용

`.logoutSuccessHandler(logoutSuccessHandler())` : 로그아웃 핸들러, 대부분 url을 리다이렉트 함

* RestLoginSuccessHandler

```java java
public class RestLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    Logger logger = LoggerFactory.getLogger(RestLoginSuccessHandler.class);

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        logger.info("로그인 성공");
        response.sendRedirect("/");
    }
}
```

* RestLoginFailureHandler

```java java
public class RestLoginFailureHandler implements AuthenticationFailureHandler {
    Logger logger = LoggerFactory.getLogger(RestLoginFailureHandler.class);

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        int cuase=0;
        if(exception.getClass().equals(BadCredentialsException.class)){
            cuase=1;
        }
        logger.info("로그인 실패");
        response.sendRedirect("/?cause="+cuase);
    }
}
```

* RestLogoutSuccessHandler

```java java
public class RestLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
                                Authentication authentication) throws IOException, ServletException {
        String targetUrl = determineTargetUrl(request, response);
        if (response.isCommitted()) {
            logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
        }
        logger.info("로그아웃 성공");
        response.sendRedirect("/");
    }
}
```

##### 2) 로그인 화면

* sign-in.ftl

```html html
<div id="app">
        <div class="text-center">
            <h1>로그인</h1>
            <form class="pure-form pure-form-aligned" id="form-login">
                <fieldset>
                    <div class="pure-control-group">
                        <label for="userName">UserName</label>
                        <input name="userName" v-model="userName" id="userName" class="pure-input-1-3" value=""
                               placeholder="UserName" type="text">
                    </div>
                    <div class="pure-control-group">
                        <label for="password1">Password</label>
                        <input name="password" v-model="password1" id="password1" class="pure-input-1-3" value=""
                               placeholder="Password" type="password">
                    </div>

                    <div class="pure-controls">
                        <button class="pure-button pure-button-primary pure-u-1-3" @click.prevent="onSignIn()">로그인
                        </button>
                    </div>
                    <input type="hidden"
                           name="${_csrf.parameterName}"
                           value="${_csrf.token}"/>
                </fieldset>
            </form>
        </div>
    </div>
```

* sign-in.js

```javascript vue.js
new Vue({
    el: '#app',
    i18n: apex_lang,
    data: {
        userName: '',
        password1: '',
    },
    methods: {
        onSignIn() {
            console.log('onSignIn');

            let form = document.getElementById("form-login");
            form.method = 'post';
            form.action = '/user/auth/login';
            form.submit();
        }
    }
});
```

`.loginProcessingUrl("/user/auth/login")` 시큐리티 설정시, 지정했던 url과 동일한 url을 사용



#### 6. 계정 만료 및 잠금에 의한 로그인 실패처리

`loadUserByUsername` 메소드의 반환값은 	`UserDetails` 타입이다.

* `org.springframework.security.core.userdetails.User` 

```java java
private String password;
private final String username;
private final Set<GrantedAuthority> authorities;
private final boolean accountNonExpired;
private final boolean accountNonLocked;
private final boolean credentialsNonExpired;
private final boolean enabled;
```

