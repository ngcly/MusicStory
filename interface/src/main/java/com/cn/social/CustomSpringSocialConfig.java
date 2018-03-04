package com.cn.social;

import org.springframework.social.security.SocialAuthenticationFilter;
import org.springframework.social.security.SpringSocialConfigurer;

/**
 * 自定义登录和注册
 * @author chenning
 * @date 2018/3/4 下午2:35
 */
public class CustomSpringSocialConfig extends SpringSocialConfigurer {
    private String filterProcessesUrl;

    public CustomSpringSocialConfig(String filterProcessesUrl){
        this.filterProcessesUrl = filterProcessesUrl;
    }

    @Override
    protected <T> T postProcess(T object) {
        SocialAuthenticationFilter filter = (SocialAuthenticationFilter) super.postProcess(object);
        filter.setFilterProcessesUrl(filterProcessesUrl);
        filter.setSignupUrl("/register");
        return (T) filter;
    }
}
