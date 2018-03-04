package com.cn.social.qq;

import org.springframework.social.oauth2.AbstractOAuth2ServiceProvider;

/**
 * 连接服务提供商
 * @author chenning
 * @date 2018/3/4 下午1:49
 */
public class QQServiceProvider extends AbstractOAuth2ServiceProvider<QQ>{
    /**
     * 获取code地址
     */
    private static final String QQ_RUL_AUTHORIZE = "https://graph.qq.com/oauth2.0/authorize";
    /**
     * 获取access_token 令牌地址的
     */
    private static final String QQ_URL_ACCESS_TOKEN = "https://graph.qq.com/oauth2.0/token";
    private String appId;

    public QQServiceProvider(String appId,String appSecret){
        super(new QQOAuth2Template(appId,appSecret,QQ_RUL_AUTHORIZE,QQ_URL_ACCESS_TOKEN));
    }

    @Override
    public QQ getApi(String accessToken) {
        return new QQImpl(accessToken, appId);
    }
}
