package kr.co.apexsoft.jpaboot._support.aws.s3.exception;

import kr.co.apexsoft.jpaboot._support.exception.CustomException;

/**
 * s3 다운로드
 */
public class S3DeleteException extends CustomException {

    public S3DeleteException() {
        super();
    }

    public S3DeleteException(String warningMessage) {
        super(warningMessage);
    }

    public S3DeleteException(String message, String warningMessage) {
        super(message, warningMessage);
    }
}
