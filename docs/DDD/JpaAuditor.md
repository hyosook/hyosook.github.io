# Jpa Auditing

> jpa 사용시 중복으로 사용 되는부분 `id` `생성시간` 등을  ` Auditing Entity`를 통해 중복을 제거하고 자동화 시킨다



* AbstractBaseEntity

  ```java
  
  @EntityListeners(AuditingEntityListener.class)
  @MappedSuperclass
  @Access(AccessType.FIELD)
  @Getter
  public abstract class AbstractBaseEntity implements Serializable {
  
      @CreatedDate
      @Column(name = "CREATED_DATE", updatable = false)
      protected LocalDateTime createdDateTime;
  
      @LastModifiedDate
      @Column(name = "LAST_MODIFIED_DATE", updatable = true)
      protected LocalDateTime lastModifiedDateTime;
  
      @Column(name = "CREATED_BY", updatable = false)
      @CreatedBy
      protected String createdBy;
  
      @Column(name = "LAST_MODIFIED_BY", updatable = true)
      @LastModifiedBy
      protected String lastModifiedBy;
  }
  
  
  ```
  
  

* `EnableJpaAuditing`

  

  *  createDate와 modifiedDate에 대해서만 자동

    ```java
    @EnableJpaAuditing
    @Configuration
    public class JpaAuditingConfig {
    
    }
    ```

  * createdBy나 lastModifiedBy도 자동

    ```java
    /**
     * JPA 사용할 때 시큐리티 통해서 Auditor 설정됨
     */
    @Configuration
    @EnableJpaAuditing(auditorAwareRef="auditorProvider")
    public class PersistenceConfig {
    
        @Bean
        AuditorAware<String> auditorProvider() {
            return new AuditorAware<String>() {
                @Override
                public Optional<String> getCurrentAuditor() {
                    return Optional.ofNullable(SecurityContextHolder.getContext())
                            .map(SecurityContext::getAuthentication)
                            .filter(Authentication::isAuthenticated)
                            .map(Authentication::getPrincipal)
                            .map(principal -> {
                                if (principal instanceof String)
                                    return (String) principal;
                                if (principal instanceof UserPrincipal)
                                    return ((UserPrincipal) principal).getUsername();
                                return "INVALID-USER";
                            });
                }
            };
    }
    }
    ```
    
    