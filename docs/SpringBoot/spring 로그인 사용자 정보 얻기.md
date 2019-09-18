# spring 로그인 사용자 정보 얻기

## 1. Bean `SecurityContextHolder`

```java
Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal(); 
String username = principal.getUsername(); 
String password = principal.getPassword();

```



## 2. `Principal` 

```java
@RestController 
public class controller { 
    @GetMapping
    public String currentUserName(Principal principal , Authentication authentication) { 
        
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
         userDetails.getUsername()
        return principal.getName();
    } 
}

```



## 3. `@AuthenticationPrincipal`

> 현재 로그인한 사용자 객체를 인자에 주입
>
>  `UserDetails` 를 구현한 `UserPrincipal` 클래스가 있고
>
> `UserDetailsService` 구현체에서 `UserPrincipal`객체를 반환

```java
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CustomUserDetailsService implements UserDetailsService {

    @Value("${login.failMaxCnt}")
    private Integer failMaxCnt;

    @NonNull
    private UserService userService;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) {
        User user = this.userService.findByUserId(email);

        Collection<GrantedAuthority> grantedAuthorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getType().name()))
                .collect(Collectors.toList());

        return new UserPrincipal(user.getUserNo().getId(), user.getEmail(), user.getPassword(),
                user.getNickname(), user.getStatus(), user.isSocialSignup(), user.getProfilePath(),
                !user.checkUserLock(failMaxCnt), user.checkActiveUser(), grantedAuthorities);
    }
}

```



```java

@NoArgsConstructor
public class UserPrincipal implements UserDetails {

    private Long id;

    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private String nickname;

    private User.Status status;

    private boolean socialSignup;

    private String profilePath;

    private boolean accountNonLocked;

    private boolean enabled;

    private Collection<? extends GrantedAuthority> authorities;

    public UserPrincipal(Long id,
                          String email,
                          String password,
                          String nickname,
                          User.Status status,
                          boolean socialSignup,
                          String profilePath,
                          boolean accountNonLocked,
                          boolean enabled,
                          Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.status = status;
        this.socialSignup = socialSignup;
        this.profilePath = profilePath;
        this.accountNonLocked = accountNonLocked;
        this.enabled = enabled;
        this.authorities = authorities;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getNickname() { return nickname; }

    public User.Status getStatus() { return status; }

    public boolean getSocialSignup() { return socialSignup; }

    public String getProfilePath() { return profilePath; }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserPrincipal that = (UserPrincipal) o;

        if (!id.equals(that.id)) return false;
        if (!email.equals(that.email)) return false;
        if (!password.equals(that.password)) return false;
        return authorities != null ? authorities.equals(that.authorities) : that.authorities == null;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + email.hashCode();
        result = 31 * result + password.hashCode();
        result = 31 * result + (authorities != null ? authorities.hashCode() : 0);
        return result;
    }


}

```

```java
@RestController 
public class controller { 
    @GetMapping
    public String currentUserName(@AuthenticationPrincipal UserPrincipal userPrincipal) { 
        
    } 
}
```

