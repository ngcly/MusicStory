package com.cn.social.wechart;

import com.cn.social.StaticConstants;
import org.springframework.boot.autoconfigure.social.SocialAutoConfigurerAdapter;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.connect.ConnectionFactory;

import static com.cn.social.StaticConstants.DEFAULT_SOCIAL_WEIXIN_PROVIDER_ID;

/**
 * 创建工厂和设置数据源
 *
 * @author chen
 * @date 2018-03-05 15:07
 */
@Configuration
public class WeChartAuthConfig extends SocialAutoConfigurerAdapter {
    @Override
    protected ConnectionFactory<?> createConnectionFactory() {
        return new WeChartConnectionFactory(DEFAULT_SOCIAL_WEIXIN_PROVIDER_ID, StaticConstants.DEFAULT_SOCIAL_WEIXIN_APP_ID,
                StaticConstants.DEFAULT_SOCIAL_WEIXIN_APP_SECRET);
    }

}
