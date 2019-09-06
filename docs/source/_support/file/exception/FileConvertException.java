package kr.co.apexsoft.jpaboot._support.file.exception;

import kr.co.apexsoft.jpaboot._support.exception.CustomException;

/**
 * MultipartFile -> file 전환
 */
public class FileConvertException extends CustomException {

    public FileConvertException() {
        super();
    }

    public FileConvertException(String warningMessage) {
        super(warningMessage);
    }

    public FileConvertException(String message, String warningMessage) {
        super(message, warningMessage);
    }
}
