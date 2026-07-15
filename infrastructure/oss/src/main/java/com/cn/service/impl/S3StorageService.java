package com.cn.service.impl;

import com.cn.config.OssConfigProperties;
import com.cn.exception.FileUploadException;
import com.cn.service.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Objects;
import java.util.Random;

/**
 * 抽象 S3 存储服务实现 (AWS SDK v2)
 *
 * @author ngcly
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class S3StorageService implements StorageService {
    private final S3Client s3Client;
    private final OssConfigProperties properties;
    private final Random rand = initRandom();

    private Random initRandom() {
        try {
            return SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) {
            log.warn("无法获取 SecureRandom，回退到默认 Random", e);
            return new Random();
        }
    }

    @Override
    public String uploadFile(MultipartFile file, String folder) {
        try {
            String suffix = Objects.requireNonNull(file.getOriginalFilename())
                    .substring(file.getOriginalFilename().lastIndexOf(".")).toLowerCase();
            String fileName = rand.nextInt(10000) + System.currentTimeMillis() + suffix;
            String fileKey = folder + "/" + fileName;

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(properties.getBucketName())
                    .key(fileKey)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            String returnedUrl;
            // 支持自定义 CDN 域名绑定替换
            if (properties.getEndpoint() != null && !properties.getEndpoint().trim().isEmpty()) {
                String protocol = properties.getHost().startsWith("https") ? "https" : "http";
                returnedUrl = protocol + "://" + properties.getEndpoint().trim() + "/" + fileKey;
            } else {
                String host = properties.getHost();
                if (host.endsWith("/")) {
                    host = host.substring(0, host.length() - 1);
                }
                returnedUrl = host + "/" + properties.getBucketName() + "/" + fileKey;
            }

            log.info("文件上传成功: key={}, url={}", fileKey, returnedUrl);
            return returnedUrl;
        } catch (IOException e) {
            log.error("文件上传发生 IO 异常", e);
            throw new FileUploadException();
        }
    }

    @Override
    public void deleteFile(String url) {
        if (url == null || url.trim().isEmpty()) {
            return;
        }
        try {
            String fileKey = getKeyFromUrl(url);
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(properties.getBucketName())
                    .key(fileKey)
                    .build();
            s3Client.deleteObject(deleteObjectRequest);
            log.info("文件删除成功: key={}", fileKey);
        } catch (Exception e) {
            log.error("删除文件失败: url={}", url, e);
        }
    }

    /**
     * 从 URL 中提取 S3 对象的 key
     */
    private String getKeyFromUrl(String url) {
        try {
            URL parsedUrl = new URL(url);
            String path = parsedUrl.getPath(); // 返回例如 "/folder/file.jpg" 或 "/bucket/folder/file.jpg"
            if (path.startsWith("/")) {
                path = path.substring(1);
            }
            // 如果使用默认域名，路径中会带有 bucketName 前缀，需将其剥离
            if (path.startsWith(properties.getBucketName() + "/")) {
                path = path.substring(properties.getBucketName().length() + 1);
            }
            return path;
        } catch (Exception e) {
            return url;
        }
    }
}
