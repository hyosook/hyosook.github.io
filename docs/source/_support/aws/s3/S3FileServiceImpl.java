package kr.co.apexsoft.jpaboot._support.aws.s3;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;
import kr.co.apexsoft.jpaboot._support.MessageUtils;
import kr.co.apexsoft.jpaboot._support.aws.s3.exception.S3CopyException;
import kr.co.apexsoft.jpaboot._support.aws.s3.exception.S3DeleteException;
import kr.co.apexsoft.jpaboot._support.aws.s3.exception.S3UploadException;
import kr.co.apexsoft.jpaboot._support.file.FileRequest;
import kr.co.apexsoft.jpaboot._support.file.FileResponse;
import kr.co.apexsoft.jpaboot._support.file.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.Date;

@Service
public class S3FileServiceImpl implements FileService {

    private static final Logger logger = LoggerFactory.getLogger(S3FileServiceImpl.class);

    @Value("${aws.s3.bucketName}")
    private String bucketName;

    @Value("${aws.s3.URL}")
    private String s3URL;

    @Autowired
    private AmazonS3 amazonS3;


    @Override
    public FileResponse upload(FileRequest fileRequest) {
        try {
            PutObjectRequest request = createPutObjectRequest(fileRequest);
            TransferManager tm = createTransferManager();

            Upload upload = tm.upload(request);
            upload.waitForCompletion();
        } catch (Exception e) {
            throw new S3UploadException(MessageUtils.getMessage("S3_UPLOAD_FAIL"));
        }

        return
                FileResponse.builder()
                        .fileDirPath(fileRequest.getFileDirPath())
                        .fileName(fileRequest.getFileName())
                        .originalFileName(fileRequest.getOriginalFileName())
                        .fileSize(fileRequest.getFileSize())
                        .fileKey(fileRequest.getFileKey())
                        .build();
    }

    private TransferManager createTransferManager() {
        return TransferManagerBuilder
                .standard()
                .withS3Client(amazonS3)
                .withMultipartUploadThreshold((long) (5 * 1024 * 1025))
                .build();
    }

    private PutObjectRequest createPutObjectRequest(FileRequest fileRequest) {
        return new PutObjectRequest(bucketName, fileRequest.getFileKey(),
                fileRequest.getFile())
                .withCannedAcl(CannedAccessControlList.Private)
                .withMetadata(createObjectMetadata(fileRequest));
    }

    private ObjectMetadata createObjectMetadata(FileRequest fileRequest) {
        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentLength(fileRequest.getFileSize());
        meta.setContentEncoding("UTF-8");
        return meta;
    }

    @Override
    public void delete(String fileKey) {
        try {
            amazonS3.deleteObject(bucketName, fileKey);
        } catch (Exception e) {
            throw new S3DeleteException(MessageUtils.getMessage("S3_DELETE_FAIL"));
        }
    }

    @Override
    public void copy(String sourceKey, String destinationKey) { //TODO: 에디터 디벨롭 이후 메소드 수정
        try {
            CopyObjectRequest copyObjRequest = new CopyObjectRequest(bucketName, sourceKey, bucketName, destinationKey);
            amazonS3.copyObject(copyObjRequest);
        } catch (Exception e) {
            throw new S3CopyException(MessageUtils.getMessage("S3_COPY_FAIL"));
        }
    }

    @Override
    public URL getGeneratePresignedUrl(String fileKey) {
        try {
            Date expiration = new java.util.Date();
            long expTimeMillis = expiration.getTime();
            expTimeMillis += 1000 * 60 * 5; //FIXME: 5분
            expiration.setTime(expTimeMillis);

            GeneratePresignedUrlRequest generatePresignedUrlRequest =
                    new GeneratePresignedUrlRequest(bucketName, fileKey)
                            .withMethod(HttpMethod.GET)
                            .withExpiration(expiration);
            return amazonS3.generatePresignedUrl(generatePresignedUrlRequest);
        } catch (Exception e) {
            throw new S3CopyException(MessageUtils.getMessage("S3_COPY_FAIL")); //TODO: 에디터 디벨롭 이후 메소드 수정
        }
    }

}
