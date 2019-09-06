package kr.co.apexsoft.jpaboot._support.aws.s3.exception;

import kr.co.apexsoft.jpaboot._support.exception.CustomException;

/**
 * s3업로드
 */
public class S3UploadException extends CustomException {

    public S3UploadException() {
        super();
    }

    public S3UploadException(String warningMessage) {
        super(warningMessage);
    }

    public S3UploadException(String message, String warningMessage) {
        super(message, warningMessage);
    }
}
