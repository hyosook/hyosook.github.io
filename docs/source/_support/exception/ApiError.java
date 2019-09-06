package kr.co.apexsoft.jpaboot._support.exception;

import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;

import java.util.List;

/**
 * created by hanmomhanda@gmail.com
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ApiError {

    @NonNull
    private HttpStatus status;

    @NonNull
    private String timestamp;

    private String debugMessage;

    private String warningMessage = "서버에서 에러가 발생했습니다.";

    private List<FieldError> fieldErrors;

    public ApiError(@NonNull HttpStatus status, @NonNull String timestamp, String debugMessage,
                    String warningMessage) {
        this.status = status;
        this.timestamp = timestamp;
        this.debugMessage = debugMessage;
        this.warningMessage = warningMessage;
    }

    public ApiError(@NonNull HttpStatus status, @NonNull String timestamp, String debugMessage) {
        this.status = status;
        this.timestamp = timestamp;
        this.debugMessage = debugMessage;
    }

    public void setDebugMessage(String debugMessage) {
        this.debugMessage = debugMessage;
    }

    public void setFieldErrors(List<FieldError> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }
}
