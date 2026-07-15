package com.cn.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;

import java.net.URI;

/**
 * OSS 自动配置类 (S3 兼容协议)
 *
 * @author ngcly
 */
@Configuration
public class OssAutoConfig {

    @Bean
    public S3Client s3Client(OssConfigProperties properties) {
        Region region = properties.getRegion() != null ? Region.of(properties.getRegion()) : Region.US_EAST_1;

        S3Configuration serviceConfiguration = S3Configuration.builder()
                .pathStyleAccessEnabled(true)
                .chunkedEncodingEnabled(false)
                .build();

        return S3Client.builder()
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(properties.getAccessKeyId(), properties.getAccessKeySecret())))
                .endpointOverride(URI.create(properties.getHost()))
                .region(region)
                .serviceConfiguration(serviceConfiguration)
                .build();
    }
}
