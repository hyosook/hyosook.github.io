# applNo  인터셉터

## 조건 

* applNo pathvariable 있으면, 지원자 본인이거나 시스템관리자인지 체크

````java
@PathVariable("applNo")
```

```
package kr.co.apexsoft.gradnet2.user_api._config.webconfig;

import kr.co.apexsoft.gradnet2.entity.applicant.appl.repository.ApplRepository;
import kr.co.apexsoft.gradnet2.entity.role.domain.RoleType;
import kr.co.apexsoft.gradnet2.user_api._common.util.MessageUtil;
import kr.co.apexsoft.gradnet2.user_api._config.security.UserPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

/**
 * Class Description
 * <p>
 * appl url 이하 인터셉터
 *
 * @author 김혜연
 * @since 2020-01-08
 */
@Slf4j
@Component
public class ApplAuthInterceptor implements HandlerInterceptor {
    @Autowired
    private ApplRepository applRepository;


    /**
     * controller 전에 applNo pathvariable 있으면, 지원자 본인이거나 시스템관리자인지 체크
     *
     * @param request
     * @param response
     * @param handler
     * @return
     */
    @Transactional(readOnly = true)
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {
        HashMap<String, String> pathVariables = (HashMap<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean isAdmin = userPrincipal.getRoles().stream()
                .anyMatch(role -> (role.getType().equals(RoleType.ROLE_SYS_ADMIN)));

        if (!isAdmin) {
            if (pathVariables.get("applNo") != null) {
                if (applRepository.existsApplNoAndUserNo(Long.parseLong(pathVariables.get("applNo")), userPrincipal.getId())==1) {
                    return true;
                } else {
                    throw new AccessDeniedException(MessageUtil.getMessage("UNAUTHORIZED_ACCESS"));
                }
            }
        }
                /*TODO: APPL 데이터 전달가능 , 범위 필요성 논의 필요
                request.setAttribute("authApplInfo", appl); //전달
                @RequestAttribute(name="authApplInfo") Appl appl //controller 사용
                */
        return true;
    }

}

```

