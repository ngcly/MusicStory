package com.cn.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * webSocket 配置
 * @author ngcly
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * 配置消息代理(中介)
     * enableSimpleBroker 服务端推送给客户端的路径前缀
     * setApplicationDestinationPrefixes  客户端发送数据给服务器端的一个前缀
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/queue","/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    /**
     * 注册端点，发布或者订阅消息的时候需要连接此端点
     * setAllowedOrigins 非必须，*表示允许其他域进行连接
     * withSockJS  表示开始sockejs支持
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat").withSockJS();
    }

}
