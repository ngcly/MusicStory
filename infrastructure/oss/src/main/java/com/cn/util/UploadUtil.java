package com.cn.util;

import cn.hutool.extra.spring.SpringUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.cn.config.OssConfigProperties;
import com.cn.exception.FileUploadException;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.Nonnull;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

/**
 * 文件上传工具类
 *
 * @author ngcly
 */
public final class UploadUtil {
    private static final OssConfigProperties ossProperties;
    private static final Random rand;

    private UploadUtil() {
    }

    static {
        ossProperties = SpringUtil.getBean("ossConfigProperties");
        try {
            rand = SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 阿里云上传
     *
     * @param file 文件
     * @param cloudDir 上传至服务器目录
     * @return String 访问路径
     */
    public static String uploadFileByAli(@Nonnull MultipartFile file, String cloudDir) {
        try {
            OSS ossClient = new OSSClientBuilder().build(ossProperties.getHost(), ossProperties.getAccessKeyId(), ossProperties.getAccessKeySecret());
            // 上传内容到指定的存储空间（bucketName）并保存为指定的文件名称（objectName）。
            String suffix = Objects.requireNonNull(file.getOriginalFilename())
                    .substring(file.getOriginalFilename().lastIndexOf(".")).toLowerCase();
            String fileName = rand.nextInt(10000) + System.currentTimeMillis() + suffix;
            String fileKey = cloudDir + "/" + fileName;
            ossClient.putObject(ossProperties.getBucketName(), fileKey, new ByteArrayInputStream(file.getBytes()));
            // 设置URL过期时间为10年 3600l* 1000*24*365*10
            Date expiration = new Date(System.currentTimeMillis() + 3600L * 1000 * 24 * 365 * 10);
            // 生成URL
            URL url = ossClient.generatePresignedUrl(ossProperties.getBucketName(), fileKey, expiration);
            // 关闭OSSClient
            ossClient.shutdown();
            return url.toString().replace("music-story.oss-cn-hongkong-internal.aliyuncs.com", ossProperties.getEndpoint());
        } catch (IOException e) {
            throw new FileUploadException();
        }
    }

    /**
     * 阿里云删除文件
     *
     * @param url 图片地址
     */
    public static void deleteFileByAli(String url) {
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(ossProperties.getHost(), ossProperties.getAccessKeyId(), ossProperties.getAccessKeySecret());
        // 删除文件。
        String fileName = url.replace(ossProperties.getEndpoint(), "");
        ossClient.deleteObject(ossProperties.getBucketName(), fileName.substring(fileName.lastIndexOf(":/"), fileName.indexOf("?")));
        // 关闭OSSClient。
        ossClient.shutdown();
    }


}
