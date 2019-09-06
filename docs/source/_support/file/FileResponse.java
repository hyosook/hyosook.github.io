package kr.co.apexsoft.jpaboot._support.file;

import kr.co.apexsoft.jpaboot.common.util.FilePathUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.net.URLEncoder;

@Getter
public class FileResponse {


    private String fileName;

    private String originalFileName;

    private Long fileSize;

    private String fileDirPath;

    private String fileKey;

    private String filePathKey;

    @Builder
    public FileResponse(String fileDirPath, String fileName, String originalFileName, Long fileSize, String fileKey) {
        this.fileDirPath = FilePathUtil.recoverSingleQuote(FilePathUtil.recoverAmpersand(fileDirPath));
        this.fileName = FilePathUtil.recoverSingleQuote(FilePathUtil.recoverAmpersand(fileName));
        this.originalFileName = FilePathUtil.recoverSingleQuote(FilePathUtil.recoverAmpersand(originalFileName));
        this.fileSize = fileSize;
        this.fileKey = fileKey;
        //this.filePathKey = URLEncoder.encode(fileKey, "UTF-8");
    }
}
