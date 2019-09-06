package kr.co.apexsoft.jpaboot._support.file;

import kr.co.apexsoft.jpaboot._support.MessageUtils;
import kr.co.apexsoft.jpaboot._support.aws.s3.exception.S3UploadException;
import kr.co.apexsoft.jpaboot._support.file.exception.FileConvertException;
import kr.co.apexsoft.jpaboot.common.ApexAssert;
import kr.co.apexsoft.jpaboot.common.util.FilePathUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.Set;


@Getter
public class FileRequest {

    private static final Logger logger = LoggerFactory.getLogger(FileRequest.class);

    private FileType fileType;

    private String fileName;

    private String originalFileName;

    private Long fileSize;

    private File file;

    private String fileDirPath;


    public enum FileType {
        IMAGE, MOVIE, DOC
    }

    public FileRequest(StandardMultipartHttpServletRequest multipartReq) {
        Map<String, MultipartFile> fileMap = multipartReq.getFileMap();
        Set<Map.Entry<String, MultipartFile>> entrySet = fileMap.entrySet();

        ApexAssert.isTrue(entrySet.size() == 1, "파일은 1개만 업로드 가능합니다", S3UploadException.class);
        Map.Entry<String, MultipartFile> fieldName_MultipartFile = entrySet.iterator().next();
        MultipartFile multipartFile = fieldName_MultipartFile.getValue();


        this.originalFileName = FilePathUtil.removeSeparators(multipartFile.getOriginalFilename());

        this.file = convert(multipartFile).orElseThrow(() -> new FileConvertException(MessageUtils.getMessage("S3_UPLOAD_FAIL")));
        this.fileSize = multipartFile.getSize();

        this.fileName = this.originalFileName; // 따로 지정하지 않는경우, 원본파일명으로 됨

    }

    private Optional<File> convert(MultipartFile file) {
        File convertFile = null;
        try {
            convertFile = File.createTempFile("upload", "temp");
        } catch (IOException e) {
            return Optional.empty();
        }
        convertFile.deleteOnExit();
        try {
            file.transferTo(convertFile);
        } catch (IOException e) {
            return Optional.empty();
        }
        return Optional.of(convertFile);
    }


    public void setCustomFileDirPath(String fileDirPath) {
        this.fileDirPath = fileDirPath;
    }

    public void setCustomFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileKey() {
        return this.fileDirPath + "/" + this.fileName;
    }
}
