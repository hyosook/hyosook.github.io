package kr.co.apexsoft.jpaboot._support.file;

import java.net.URL;

public interface FileService {

    FileResponse upload(FileRequest fileRequest);

    void delete(String fileKey);

    void copy(String sourceKey,String destinationKey);

    URL getGeneratePresignedUrl(String fileKey);


}
