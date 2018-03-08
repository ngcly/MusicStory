package com.cn.social;

import com.cn.social.qq.QQConnectionFactory;
import com.cn.social.wechart.WeChartConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurer;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.social.security.AuthenticationNameUserIdSource;
import org.springframework.social.security.SpringSocialConfigurer;

import javax.sql.DataSource;

/**
 * 社交登录配置类
 * @author chen
 * @date 2018-03-04
 */
@Configuration
@EnableSocial
public class SocialConfig implements SocialConfigurer {
    @Autowired
    private DataSource dataSource;
    @Autowired
    private ConnectionSignUp myConnectionSignUp;

    /**
     * 社交登录配类
     *
     * @return
     */
    @Bean
    public SpringSocialConfigurer customSpringSocialConfig() {
        String filterProcessesUrl = StaticConstants.DEFAULT_SOCIAL_PROCESS_URL;
        CustomSpringSocialConfig configurer = new CustomSpringSocialConfig(filterProcessesUrl);
        return configurer;
    }

    /**
     * 处理注册流程的工具类
     * @param factoryLocator
     * @return
     */
    @Bean
    public ProviderSignInUtils providerSignInUtils(ConnectionFactoryLocator factoryLocator) {
        return new ProviderSignInUtils(factoryLocator, getUsersConnectionRepository(factoryLocator));
    }

    /**
     * 添加ConnectionFactory实例
     * @param cfConfig
     * @param env
     */
    @Override
    public void addConnectionFactories(ConnectionFactoryConfigurer cfConfig, Environment env) {
        //从配置文件获取相应 appId和appSecret
        cfConfig.addConnectionFactory(new QQConnectionFactory(env.getProperty("qq.appId"), env.getProperty("qq.appSecret")));
        cfConfig.addConnectionFactory(new WeChartConnectionFactory(env.getProperty("wechart.appId"), env.getProperty("wechart.appSecret")));
    }

    @Override
    public UserIdSource getUserIdSource() {
        return new AuthenticationNameUserIdSource();
    }

    @Override
    public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
        JdbcUsersConnectionRepository repository = new JdbcUsersConnectionRepository(dataSource,
                connectionFactoryLocator, Encryptors.noOpText());
        if (myConnectionSignUp != null) {
            repository.setConnectionSignUp(myConnectionSignUp);
        }
        return repository;
    }


}
