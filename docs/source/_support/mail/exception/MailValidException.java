package kr.co.apexsoft.jpaboot._support.mail.exception;

import kr.co.apexsoft.jpaboot._support.exception.CustomException;

/**
 * 메일
 */
public class MailValidException extends CustomException {

    public MailValidException() {
        super();
    }

    public MailValidException(String warningMessage) {
        super(warningMessage);
    }

    public MailValidException(String message, String warningMessage) {
        super(message, warningMessage);
    }
}
