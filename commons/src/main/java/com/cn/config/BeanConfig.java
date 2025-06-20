package com.cn.config;

import org.springframework.boot.web.embedded.tomcat.TomcatProtocolHandlerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * @author LinChen
 */
@Configuration
public class BeanConfig {

    @Bean
    public RestClient restClient(){
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(30000);
        requestFactory.setReadTimeout(30000);
        return RestClient.builder()
                .requestFactory(requestFactory)
                .build();
    }

    @Bean
    public TomcatProtocolHandlerCustomizer<?> protocolHandlerVirtualThreadExecutorCustomizer() {
        return protocolHandler -> {
            Thread.Builder.OfVirtual ofVirtual = Thread.ofVirtual().name("virtualthread#", 1);
            ThreadFactory factory = ofVirtual.factory();
            protocolHandler.setExecutor(Executors.newThreadPerTaskExecutor(factory));
        };
    }

}
