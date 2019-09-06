package kr.co.apexsoft.jpaboot._support.file;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * Class Description
 *
 * @author 김혜연
 * @since 2019-06-27
 */
@Component
public class FileUtils {
    /**
     * request에서 multipart 파일 1개 추출
     * @param request
     * @return
     */
    public static FileRequest getFileRequest(HttpServletRequest request) {
        StandardMultipartHttpServletRequest multipartReq = null;
        if (StandardMultipartHttpServletRequest.class.isAssignableFrom(request.getClass())) {
            multipartReq = (StandardMultipartHttpServletRequest) request;
        }

        Objects.requireNonNull(multipartReq);
        return new FileRequest(multipartReq);
    }
}
