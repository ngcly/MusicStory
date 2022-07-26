package com.cn.util;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.cn.exception.FileUploadException;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

/**
 * 文件上传工具类
 *
 * @author ngcly
 */
@Component
@ConfigurationProperties(prefix = "oss.config")
public class UploadUtil {

    /**
     * oss域名
     */
    private static String host;
    /**
     * 端点
     */
    private static String endpoint;
    /**
     * 访问秘钥id
     */
    private static String accessKeyId;
    /**
     * 访问秘钥密码
     */
    private static String accessKeySecret;
    /**
     * 存储桶名称
     */
    private static String bucketName;

    public static void setHost(String host) {
        UploadUtil.host = host;
    }

    public static void setEndpoint(String endpoint) {
        UploadUtil.endpoint = endpoint;
    }

    public static void setAccessKeyId(String accessKeyId) {
        UploadUtil.accessKeyId = accessKeyId;
    }

    public static void setAccessKeySecret(String accessKeySecret) {
        UploadUtil.accessKeySecret = accessKeySecret;
    }

    public static void setBucketName(String bucketName) {
        UploadUtil.bucketName = bucketName;
    }

    /**
     * 阿里云上传
     *
     * @param file 文件
     * @param cloudDir 上传至服务器目录
     * @return String 访问路径
     */
    public static String uploadFileByAli(MultipartFile file, String cloudDir) {
        try {
            OSS ossClient = new OSSClientBuilder().build(host, accessKeyId, accessKeySecret);
            // 上传内容到指定的存储空间（bucketName）并保存为指定的文件名称（objectName）。
            String suffix = Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename()
                    .lastIndexOf(".")).toLowerCase();
            Random random = new Random();
            String fileName = random.nextInt(10000) + System.currentTimeMillis() + suffix;
            String fileKey = cloudDir + "/" + fileName;
            ossClient.putObject(bucketName, fileKey, new ByteArrayInputStream(file.getBytes()));
            // 设置URL过期时间为10年 3600l* 1000*24*365*10
            Date expiration = new Date(System.currentTimeMillis() + 3600L * 1000 * 24 * 365 * 10);
            // 生成URL
            URL url = ossClient.generatePresignedUrl(bucketName, fileKey, expiration);
            // 关闭OSSClient
            ossClient.shutdown();
            return url.toString().replace("music-story.oss-cn-hongkong-internal.aliyuncs.com", endpoint);
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
        OSS ossClient = new OSSClientBuilder().build(host, accessKeyId, accessKeySecret);
        // 删除文件。
        String fileName = url.replace(endpoint, "");
        ossClient.deleteObject(bucketName, fileName.substring(fileName.lastIndexOf(":/"), fileName.indexOf("?")));
        // 关闭OSSClient。
        ossClient.shutdown();
    }


}
