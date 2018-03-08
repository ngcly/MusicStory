package com.cn.social.qq;

import org.springframework.social.connect.support.OAuth2ConnectionFactory;
/**
 * 连接服务提供商的工厂类
 * @author chenning
 * @date 2018/3/4 下午2:04
 */
public class QQConnectionFactory extends OAuth2ConnectionFactory<QQ>{

    public QQConnectionFactory(String appId, String appSecret) {
        super("qq",new QQServiceProvider(appId,appSecret),new QQAdapter());
    }
}
