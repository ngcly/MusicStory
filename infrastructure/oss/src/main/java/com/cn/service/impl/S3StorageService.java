package com.cn.service.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.cn.config.OssConfigProperties;
import com.cn.exception.FileUploadException;
import com.cn.service.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Objects;
import java.util.Random;

/**
 * 抽象 S3 存储服务实现
 *
 * @author ngcly
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class S3StorageService implements StorageService {
    private final AmazonS3 s3Client;
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

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());

            s3Client.putObject(properties.getBucketName(), fileKey, file.getInputStream(), metadata);

            URL generatedUrl = s3Client.getUrl(properties.getBucketName(), fileKey);
            String returnedUrl;

            // 支持自定义 CDN 域名绑定替换
            if (properties.getEndpoint() != null && !properties.getEndpoint().trim().isEmpty()) {
                returnedUrl = generatedUrl.getProtocol() + "://" + properties.getEndpoint().trim() + "/" + fileKey;
            } else {
                returnedUrl = generatedUrl.toString();
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
            s3Client.deleteObject(properties.getBucketName(), fileKey);
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
            String path = parsedUrl.getPath(); // 返回例如 "/folder/file.jpg"
            if (path.startsWith("/")) {
                path = path.substring(1);
            }
            return path;
        } catch (Exception e) {
            // 如果不是合法的 URL，将其作为 key 本身返回
            return url;
        }
    }
}
