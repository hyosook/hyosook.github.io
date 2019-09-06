package kr.co.apexsoft.jpaboot._support.exception;


import kr.co.apexsoft.jpaboot._config.security.UnauthenticatedAccessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.security.InvalidParameterException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 밸리데이션 값 검증 에러
     * @param e
     * @param principal
     * @return
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({InvalidException.class, InvalidParameterException.class})
    public ApiError handleInvaliduserException(InvalidException e, @Nullable Principal principal) {
        log(e, principal);
        log.info(e.getErrors().toString());

        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, LocalDateTime.now().toString(),
                e.toString(), e.getMessage());
        Optional.ofNullable(e.getErrors())
                .ifPresent(errors -> apiError.setFieldErrors(errors.getFieldErrors()));
        return apiError;
    }

    /**
     * UNAUTHORIZED 에러
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({ BadCredentialsException.class, UnauthenticatedAccessException.class })
    public ApiError handleBadCredentialsException(BadCredentialsException e) {
        log(e);
        return new ApiError(HttpStatus.UNAUTHORIZED, LocalDateTime.now().toString(),
                e.toString(), e.getMessage());
    }

    /**
     * 그 외 사용자 정의 에러 & 서버 에러
     * @param e
     * @param principal
     * @return
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(CustomException.class)
    public ApiError handleDefaultException(CustomException e, @Nullable Principal principal) {
        log(e, e.getWarningMessage(), principal);
        return new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, LocalDateTime.now().toString(),
                e.toString(), e.getWarningMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ApiError handleDefaultException(Exception e, @Nullable Principal principal) {
        errLog(e, principal);

        return new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, LocalDateTime.now().toString(),
                e.toString());
    }

    private void log(Exception e) {
        log.error(e.toString());
        log.info("[ TIME : " + LocalDateTime.now().toString() + " ]");
    }

    private void log(Exception e, @Nullable Principal principal) {
        log.info(e.toString());
        log.info("[ TIME : " + LocalDateTime.now().toString() + " ]");
        Optional.ofNullable(principal)
                .ifPresent(p -> log.info("[ userId : " + p.getName() + " ]"));
    }

    private void log(Exception e, String warningMessage, @Nullable Principal principal) {
        log.info(e.toString());
        log.info("[ TIME : " + LocalDateTime.now().toString() + " ]");
        log.info("[ Alert Message : " + warningMessage +" ]");
        Optional.ofNullable(principal)
                .ifPresent(p -> log.info("[ userId : " + p.getName() + " ]"));
    }

    private void errLog(Exception e, @Nullable Principal principal) {
        log.error(e.toString());
        log.info("[ TIME : " + LocalDateTime.now().toString() + " ]");
        Optional.ofNullable(principal)
                .ifPresent(p -> log.info("[ userId : " + p.getName() + " ]"));
    }
}
