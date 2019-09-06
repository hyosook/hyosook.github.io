package kr.co.apexsoft.jpaboot._support.aws.mail.exception;

import kr.co.apexsoft.jpaboot._support.exception.CustomException;

/**
 * SES 메일
 */
public class SESMailException extends CustomException {

    public SESMailException() {
        super();
    }

    public SESMailException(String warningMessage) {
        super(warningMessage);
    }

    public SESMailException(String message, String warningMessage) {
        super(message, warningMessage);
    }
}
