//package com.cn.config;
//
//import com.amazonaws.ClientConfiguration;
//import com.amazonaws.auth.AWSCredentials;
//import com.amazonaws.auth.AWSCredentialsProvider;
//import com.amazonaws.auth.AWSStaticCredentialsProvider;
//import com.amazonaws.auth.BasicAWSCredentials;
//import com.amazonaws.client.builder.AwsClientBuilder;
//import com.amazonaws.services.s3.AmazonS3;
//import com.amazonaws.services.s3.AmazonS3Client;
//import lombok.RequiredArgsConstructor;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///**
// * @author chenning
// */
//@Configuration
//@RequiredArgsConstructor
//@EnableConfigurationProperties(OssProperties.class)
//public class OssAutoConfig {
//    @Bean
//    @ConditionalOnMissingBean
//    public AmazonS3 ossClient(OssProperties ossProperties) {
//        // 客户端配置，主要是全局的配置信息
//        ClientConfiguration clientConfiguration = new ClientConfiguration();
//        clientConfiguration.setMaxConnections(100);
//        // url以及region配置
//        AwsClientBuilder.EndpointConfiguration endpointConfiguration = new AwsClientBuilder.EndpointConfiguration(ossProperties.getEndpoint(), ossProperties.getRegion());
//        // 凭证配置
//        AWSCredentials awsCredentials = new BasicAWSCredentials(ossProperties.getAccessKeyId(), ossProperties.getAccessKeySecret());
//        AWSCredentialsProvider awsCredentialsProvider = new AWSStaticCredentialsProvider(awsCredentials);
//        // build amazonS3Client客户端
//        return AmazonS3Client.builder()
//                .withEndpointConfiguration(endpointConfiguration)
//                .withClientConfiguration(clientConfiguration)
//                .withCredentials(awsCredentialsProvider)
//                .disableChunkedEncoding()
//                .withPathStyleAccessEnabled(true)
//                .build();
//    }
//
//    @Bean
//    @ConditionalOnBean(AmazonS3.class)
//    public OssTemplate ossTemplate(AmazonS3 amazonS3) {
//        return new OssTemplate(amazonS3);
//    }
//}
