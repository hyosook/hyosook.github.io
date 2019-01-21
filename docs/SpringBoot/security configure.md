---
title : Spring security config 예제
tags : ["Security", "springboot","java"]
---



## 간결한 spring security configure

```java 
@Override
protected void configure(HttpSecurity http) throws Exception {
  http
    .antMatcher("/unreal/**")
    .csrf()
//  	.disable()
    	.ignoringAntMatchers("/unreal/**")
    	.and()
    .formLogin()
    	.loginProcessingUrl("/unreal/auth/login")
    	.usernameParameter("username")
    	.passwordParameter("password")
    	.successHandler(loginSuccessHandler())
    	.failureHandler(loginFailureHandler())
    	.and()
    .logout()
    	.logoutRequestMatcher(new AntPathRequestMatcher("/unreal/logout"))
    	.logoutSuccessHandler(logoutSuccessHandler())
    	.and()
    .authorizeRequests()
    	.antMatchers("/user/**").hasRole("USER")
    	.antMatchers("/admin/**").hasRole("ADMIN")
    	.anyRequest().permitAll();
}
```