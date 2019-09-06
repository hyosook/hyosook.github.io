package kr.co.apexsoft.jpaboot._support.aws.s3.exception;

import kr.co.apexsoft.jpaboot._support.exception.CustomException;

/**
 * s3 객체간 복사
 */
public class S3CopyException extends CustomException {

    public S3CopyException() {
        super();
    }

    public S3CopyException(String warningMessage) {
        super(warningMessage);
    }

    public S3CopyException(String message, String warningMessage) {
        super(message, warningMessage);
    }
}
