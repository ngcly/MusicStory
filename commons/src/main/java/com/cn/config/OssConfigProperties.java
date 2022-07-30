package com.cn.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * oss属性配置
 *
 * @author ngcly
 * @version V1.0
 * @since 2022/7/30 20:48
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "oss.config")
public class OssConfigProperties {
    /**
     * oss域名
     */
    private String host;
    /**
     * 端点
     */
    private String endpoint;
    /**
     * 访问秘钥id
     */
    private String accessKeyId;
    /**
     * 访问秘钥密码
     */
    private String accessKeySecret;
    /**
     * 存储桶名称
     */
    private String bucketName;
}
