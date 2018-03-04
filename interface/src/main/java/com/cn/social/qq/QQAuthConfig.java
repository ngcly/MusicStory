package com.cn.social.qq;

import com.cn.social.StaticConstants;
import org.springframework.boot.autoconfigure.social.SocialAutoConfigurerAdapter;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.connect.ConnectionFactory;

/**
 * 针对QQ返回结果的一些操作
 * @author chenning
 * @date 2018/3/4 下午2:17
 */
@Configuration
public class QQAuthConfig extends SocialAutoConfigurerAdapter{

    @Override
    protected ConnectionFactory<?> createConnectionFactory() {
        return new QQConnectionFactory(StaticConstants.DEFAULT_SOCIAL_QQ_PROVIDER_ID,
                StaticConstants.DEFAULT_SOCIAL_QQ_APP_ID, StaticConstants.DEFAULT_SOCIAL_QQ_APP_SECRET);
    }

}
