package com.adoonge.seedzip.content.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class S3Service {
    private final AmazonS3 s3Client;

    @Value("${cloud.aws.s3.docBucket}")
    private String docBucketName;

    @Value("${cloud.aws.s3.imgBucket}")
    private String imgBucketName;

    public S3Service(@Value("${cloud.aws.credentials.accessKey}") String accessKey,
                     @Value("${cloud.aws.credentials.secretKey}") String secretKey,
                     @Value("${cloud.aws.region.static}") String region) {
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);
        this.s3Client = AmazonS3ClientBuilder.standard()
                .withRegion(Regions.fromName(region))
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .build();
    }

    public String uploadDocFile(MultipartFile file) throws IOException {
        // 파일명을 고유하게 지정
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

        // S3에 파일 업로드
        s3Client.putObject(docBucketName, fileName, file.getInputStream(), null);

        // 업로드된 파일의 URL 반환
        return s3Client.getUrl(docBucketName, fileName).toString();
    }

    public String uploadImgFile(MultipartFile file) throws IOException {
        // 파일명을 고유하게 지정
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

        // S3에 파일 업로드
        s3Client.putObject(imgBucketName, fileName, file.getInputStream(), null);

        // 업로드된 파일의 URL 반환
        return s3Client.getUrl(imgBucketName, fileName).toString();
    }

    public void deleteDocFile(String fileUrl) {
        // S3 버킷에서 파일 삭제 로직
        s3Client.deleteObject(docBucketName, extractKeyFromUrl(fileUrl, docBucketName));
    }

    public void deleteImgFile(String fileUrl) {
        // S3 버킷에서 파일 삭제 로직
        s3Client.deleteObject(imgBucketName, extractKeyFromUrl(fileUrl, imgBucketName));
    }

    public static String extractKeyFromUrl(String url, String bucketName) {
        String baseUrl = "https://" + bucketName + ".s3.amazonaws.com/";

        if (url.startsWith(baseUrl)) {
            return url.substring(baseUrl.length());
        } else {
            throw new IllegalArgumentException("Invalid S3 URL: " + url);
        }
    }
}
