package kr.co.apexsoft.jpaboot._support.exception;

import lombok.Getter;
import org.springframework.validation.Errors;

/**
 * 밸리데이션 에러처리
 * @author 김혜연
 */
@Getter
public class InvalidException extends RuntimeException {

    private Errors errors;

    public InvalidException() {
        super();
    }

    public InvalidException(Errors errors) {
        this.errors = errors;
    }

    public InvalidException(String message) {
        super(message);
    }

    public InvalidException(String message, Errors errors) {
        super(message);
        this.errors = errors;
    }

    public InvalidException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidException(String message, Throwable cause, Errors errors) {
        super(message, cause);
        this.errors = errors;
    }

    public InvalidException(Throwable cause) {
        super(cause);
    }

    public InvalidException(Throwable cause, Errors errors) {
        super(cause);
        this.errors = errors;
    }

    protected InvalidException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
