package kr.co.apexsoft.jpaboot._support.mail.exception;

import kr.co.apexsoft.jpaboot._support.exception.CustomException;

/**
 * 메일
 */
public class MailTemplateException extends CustomException {

    public MailTemplateException() {
        super();
    }

    public MailTemplateException(String warningMessage) {
        super(warningMessage);
    }

    public MailTemplateException(String message, String warningMessage) {
        super(message, warningMessage);
    }
}
