package kr.co.apexsoft.jpaboot._support.exception;

import lombok.Getter;

/**
 * Class Description
 *
 * @author 김혜연
 * @since 2019-02-19
 */
@Getter
public class CustomException extends RuntimeException {
    private String warningMessage;

    public CustomException() {
        super();
    }

    public CustomException(String warningMessage) {
        this.warningMessage = warningMessage;
    }

    public CustomException(String message, String warningMessage) {
        super(message);
        this.warningMessage = warningMessage;
    }
}
