package com.cn.social.wechart;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.social.oauth2.AbstractOAuth2ApiBinding;
import org.springframework.social.oauth2.TokenStrategy;

import java.nio.charset.Charset;
import java.util.List;

/**
 * 获取微信用户信息
 *
 * @author chen
 * @date 2018-03-05 14:13
 */
public class WeChartImpl extends AbstractOAuth2ApiBinding implements WeChart{
    /**
     * 获取微信用户信息的URL
     */
    private static final String WEICHART_URL_GET_USER_INFO = "https://api.weixin.qq.com/sns/userinfo?openid=";

    private ObjectMapper objectMapper = new ObjectMapper();

    public WeChartImpl(String accessToken) {
        super(accessToken, TokenStrategy.ACCESS_TOKEN_PARAMETER);
    }

    /**
     * 获取微信用户信息
     * @param openId
     * @return
     */
    @Override
    public WeChartUserInfo getUserInfo(String openId) {
        String url = WEICHART_URL_GET_USER_INFO+openId;
        String result = getRestTemplate().getForObject(url,String.class);
        if(result.contains("errcode")){
            return null;
        }

        WeChartUserInfo userInfo = null;
        try {
            userInfo = objectMapper.readValue(result,WeChartUserInfo.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return userInfo;
    }

    @Override
    protected List<HttpMessageConverter<?>> getMessageConverters(){
        List<HttpMessageConverter<?>> messageConverters = super.getMessageConverters();
        messageConverters.remove(0);
        messageConverters.add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
        return messageConverters;
    }
}
