package com.cn.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.social.UserIdSource;
import org.springframework.social.security.AuthenticationNameUserIdSource;
import org.springframework.social.security.SpringSocialConfigurer;

/**
 * Spring Security 配置
 *
 * @author chen
 * @date 2018-02-28 16:23
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)//开启security注解
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    SpringSocialConfigurer customSpringSocialConfig;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                // 跨域支持
                .cors()
                .and()
                //对请求URL进行权限配置
                .authorizeRequests()
                //antMatchers 匹配完整URL mvcMatcher 匹配mapping中的value
                //permitAll 允许所有情况，即相当于没做任何security限制
                .antMatchers("/", "/login**", "/webjars/**", "/github").permitAll()
                //authenticated 必须要进行身份验证
                .antMatchers("/user/**").authenticated()
                //anonymous 可以以匿名身份登录
                .anyRequest().anonymous()
                .and()
                .formLogin()
                //登陆成功后的处理，因为是API的形式所以不用跳转页面
                .successHandler(loginSuccessHandler())
                //登陆失败后的处理
                .failureHandler(new SimpleUrlAuthenticationFailureHandler())
                .and()
                .apply(customSpringSocialConfig)
                .and()
                //登出后的处理
                .logout().logoutSuccessHandler(logoutSuccessHandler())
                .and()
                //认证不通过后的处理
                .exceptionHandling()
                .authenticationEntryPoint(restAuthenticationEntryPoint())
                .and()
                //开启cookie保存用户数据
                .rememberMe()
                //设置cookie有效期
                .tokenValiditySeconds(60 * 60 * 24 * 7)
                //设置cookie的私钥
                .key("ms_token")
                .rememberMeCookieName("ms")
                .and()
                .logout()
                .invalidateHttpSession(true)
                .deleteCookies("ms_token")
                .permitAll()
                .and()
                .rememberMe()
                .tokenValiditySeconds(1209600);
//        // 在 UsernamePasswordAuthenticationFilter 前添加 QQAuthenticationFilter
//        http.addFilterAt(qqAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }


    @Bean
    public LoginSuccessHandler loginSuccessHandler(){
        return new LoginSuccessHandler();
    }

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler(){
        return new LogoutSuccessHandler();
    }

    @Bean
    public RestAuthenticationEntryPoint restAuthenticationEntryPoint(){
        return new RestAuthenticationEntryPoint();
    }

    @Bean
    public UserIdSource userIdSource() {
        return new AuthenticationNameUserIdSource();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public TextEncryptor textEncryptor() {
        return Encryptors.noOpText();
    }


    /**
     * 自定义 QQ登录 过滤器
     */
    private QQAuthenticationFilter qqAuthenticationFilter(){
        QQAuthenticationFilter authenticationFilter = new QQAuthenticationFilter("/login/qq");
        authenticationFilter.setAuthenticationManager(new QQAuthenticationManager());
        return authenticationFilter;
    }

}