package com.cn.social.wechart;

import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.support.OAuth2Connection;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2ServiceProvider;

/**
 * 连接服务商工厂类
 *
 * @author chen
 * @date 2018-03-05 14:59
 */
public class WeChartConnectionFactory extends OAuth2ConnectionFactory<WeChart> {

    public WeChartConnectionFactory(String appId, String appSecret) {
        super("wechart", new WeChartServiceProvider(appId, appSecret), new WeChartAdapter());
    }

    /**
     * 由于微信的openId是和accessToken一起返回的，所以在这里直接根据accessToken设置providerUserId即可，不用像QQ那样通过QQAdapter来获取
     */
    @Override
    protected String extractProviderUserId(AccessGrant accessGrant) {
        if(accessGrant instanceof WeChartAccessGrant) {
            return ((WeChartAccessGrant)accessGrant).getOpenId();
        }
        return null;
    }

    public Connection<WeChart> createConnection(AccessGrant accessGrant) {
        return new OAuth2Connection<WeChart>(getProviderId(), extractProviderUserId(accessGrant), accessGrant.getAccessToken(),
                accessGrant.getRefreshToken(), accessGrant.getExpireTime(), getOAuth2ServiceProvider(), getApiAdapter(extractProviderUserId(accessGrant)));
    }

    public Connection<WeChart> createConnection(ConnectionData data) {
        return new OAuth2Connection<WeChart>(data, getOAuth2ServiceProvider(), getApiAdapter(data.getProviderUserId()));
    }

    private ApiAdapter<WeChart> getApiAdapter(String providerUserId) {
        return new WeChartAdapter(providerUserId);
    }

    private OAuth2ServiceProvider<WeChart> getOAuth2ServiceProvider() {
        return (OAuth2ServiceProvider<WeChart>) getServiceProvider();
    }

}
