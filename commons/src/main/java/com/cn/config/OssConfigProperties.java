package com.cn.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

/**
 * oss属性配置
 *
 * @author ngcly
 * @version V1.0
 * @since 2022/7/30 20:48
 */
@Data
@Validated
@Configuration
@ConfigurationProperties(prefix = "oss.config")
public class OssConfigProperties {
    /**
     * oss域名
     */
    @NotBlank
    private String host;
    /**
     * 端点
     */
    private String endpoint;
    /**
     * 访问秘钥id
     */
    @NotBlank
    private String accessKeyId;
    /**
     * 访问秘钥密码
     */
    @NotBlank
    private String accessKeySecret;
    /**
     * 存储桶名称
     */
    private String bucketName;
}
