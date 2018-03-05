package com.cn.social.wechart;


import org.springframework.social.oauth2.AbstractOAuth2ServiceProvider;

/**
 * 连接服务提供商
 *
 * @author chen
 * @date 2018-03-05 14:52
 */
public class WeChartServiceProvider extends AbstractOAuth2ServiceProvider<WeChart> {
    /**
     * 微信获取授权码的url
     */
    private static final String WEIXIN_URL_AUTHORIZE = "https://open.weixin.qq.com/connect/qrconnect";
    /**
     * 微信获取accessToken的url(微信在获取accessToken时也已经返回openId)
     */
    private static final String WEIXIN_URL_ACCESS_TOKEN = "https://api.weixin.qq.com/sns/oauth2/access_token";

    public WeChartServiceProvider(String appId, String appSecret) {
        super(new WeChartOAuth2Template(appId, appSecret, WEIXIN_URL_AUTHORIZE, WEIXIN_URL_ACCESS_TOKEN));
    }

    @Override
    public WeChart getApi(String accessToken) {
        return new WeChartImpl(accessToken);
    }
}
