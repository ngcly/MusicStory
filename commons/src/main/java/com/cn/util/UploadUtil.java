package com.cn.util;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.cn.config.GlobalException;
import com.cn.pojo.RestCode;
import org.springframework.beans.factory.annotation.Value;
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
public class UploadUtil {

    /**
     * 云基本信息
     */
    private static final String ENDPOINT = "https://oss-cn-hongkong-internal.aliyuncs.com";
    public static String accessKeyId;
    public static String accessKeySecret;
    private static final String BUCKET_NAME = "music-story";
    private static final String RETURN_PATH = "oss.ngcly.cn";

    /**
     * 使用这种方法是因为@Value注入对static无效，注意需要 @Component
     */
    @Value("${upload.accessKey}")
    public void setAccessKey(String accessKey) {
        UploadUtil.accessKeyId = accessKey;
    }

    @Value("${upload.secretKey}")
    public void setSecretKey(String secretKey) {
        UploadUtil.accessKeySecret = secretKey;
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
            OSS ossClient = new OSSClientBuilder().build(ENDPOINT, accessKeyId, accessKeySecret);
            // 上传内容到指定的存储空间（bucketName）并保存为指定的文件名称（objectName）。
            String suffix = Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename()
                    .lastIndexOf(".")).toLowerCase();
            Random random = new Random();
            String fileName = random.nextInt(10000) + System.currentTimeMillis() + suffix;
            String fileKey = cloudDir + "/" + fileName;
            ossClient.putObject(BUCKET_NAME, fileKey, new ByteArrayInputStream(file.getBytes()));
            // 设置URL过期时间为10年 3600l* 1000*24*365*10
            Date expiration = new Date(System.currentTimeMillis() + 3600L * 1000 * 24 * 365 * 10);
            // 生成URL
            URL url = ossClient.generatePresignedUrl(BUCKET_NAME, fileKey, expiration);
            // 关闭OSSClient
            ossClient.shutdown();
            return url.toString().replace("music-story.oss-cn-hongkong-internal.aliyuncs.com", RETURN_PATH);
        } catch (IOException e) {
            throw new GlobalException(RestCode.FILE_UPLOAD_ERR);
        }
    }

    /**
     * 阿里云删除文件
     *
     * @param url 图片地址
     */
    public static void deleteFileByAli(String url) {
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(ENDPOINT, accessKeyId, accessKeySecret);
        // 删除文件。
        String fileName = url.replace(RETURN_PATH, "");
        ossClient.deleteObject(BUCKET_NAME, fileName.substring(fileName.lastIndexOf(":/"), fileName.indexOf("?")));
        // 关闭OSSClient。
        ossClient.shutdown();
    }
}
