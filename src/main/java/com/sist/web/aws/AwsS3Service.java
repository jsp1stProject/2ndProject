package com.sist.web.aws;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.sist.web.common.exception.code.CommonErrorCode;
import com.sist.web.common.exception.domain.CommonException;
import com.sist.web.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class AwsS3Service {
    private final AmazonS3 amazonS3;

    @Value("${aws.bucketname}")
    private String bucketName;

    public String uploadFile(MultipartFile file, String fileDir) throws IOException {
        String originalFileName = file.getOriginalFilename();
        String uuidFileName = fileDir + getUuidFileName(originalFileName);
        log.debug("uploading file "+uuidFileName);

        amazonS3.putObject(new PutObjectRequest(bucketName, uuidFileName, file.getInputStream(), null));
        log.debug(amazonS3.getUrl(bucketName, uuidFileName).toString());

        return uuidFileName;
    }

    public String ResizeAndUploadFile(MultipartFile file, String fileDir, int width, int height) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Thumbnails.of(file.getInputStream())
                .size(width, height)
                .crop(Positions.CENTER)
                .outputFormat("jpg")
                .toOutputStream(os);
        byte[] resizedBytes = os.toByteArray();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(resizedBytes);

        String originalFileName = file.getOriginalFilename();
        String uuidFileName = fileDir + getUuidFileName(originalFileName);

        amazonS3.putObject(new PutObjectRequest(bucketName, uuidFileName, inputStream, null));
        log.debug(amazonS3.getUrl(bucketName, uuidFileName).toString());

        return uuidFileName;
    }

    public void deleteFile(String storedFileName) {
        try {
            amazonS3.deleteObject(new DeleteObjectRequest(bucketName, storedFileName));
        } catch (AmazonServiceException | IllegalStateException e) {
            log.error("can't delete orignal file: {}",e.getMessage());
        }
    }

    private static String getUuidFileName(String originalFileName) {
        String ext = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
        return UUID.randomUUID() + "." + ext;
    }
}
