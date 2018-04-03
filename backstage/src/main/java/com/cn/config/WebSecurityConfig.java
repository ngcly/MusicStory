package com.cn.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security 配置
 *
 * @author chen
 * @date 2018-01-02 17:32
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)//开启security注解
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private ManagerDetailService managerDetailService;

    @Override
    public void configure(WebSecurity web) throws Exception {
        //忽略静态文件 也可以在下面忽略
        web.ignoring().antMatchers("/layui/**", "/js/**", "/css/**", "/img/**", "/media/**", "/**/favicon.ico","/druid/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //设置验证码过滤器
        LoginFilter loginFilter = new LoginFilter();
        loginFilter.setAuthenticationManager(authenticationManager());
        loginFilter.setAuthenticationSuccessHandler(loginSuccessHandler());
        loginFilter.setAuthenticationFailureHandler(loginFailureHandler());

        http.authorizeRequests()
                .antMatchers("/kaptcha","/login").permitAll()
//                .antMatchers("/admin/**").hasRole("ADMIN")  //该URL只有ADMIN权限才可访问
//                .antMatchers("/db/**").access("hasRole('ADMIN') and hasRole('DBA')") //该URL需具备设定的两个权限才可访问
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(loginFilter,UsernamePasswordAuthenticationFilter.class)
                .formLogin()
                //表单登录的 登录页
                .loginPage("/login")
//                .successHandler(loginSuccessHandler())
//                .defaultSuccessUrl("/")
                //登录失败跳转页面
//                .failureUrl("/login?error=true")
//                .failureHandler(loginFailureHandler())
                .permitAll()
                .and()
                //开启cookie保存用户数据
                .rememberMe()
                //设置cookie有效期
                .tokenValiditySeconds(60 * 60 * 24 * 7)
                //设置cookie的私钥
                .key("token")
                .rememberMeCookieName("cly")
                .and()
                .logout()
                //默认注销行为为logout，可以通过下面的方式来修改
//                .logoutUrl("/logout")
                //设置注销成功后跳转页面，默认是跳转到登录页面
                .logoutSuccessUrl("/login")
                .invalidateHttpSession(true)
                .deleteCookies("token")
                .permitAll()
                .and()
                .rememberMe()
                .tokenValiditySeconds(1209600);
        //设置可以iframe访问
        http.headers().frameOptions().sameOrigin();
    }


    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        //指定密码加密所使用的加密器为passwordEncoder()
        //需要将密码加密后写入数据库
        auth.userDetailsService(managerDetailService).passwordEncoder(passwordEncoder());
        auth.eraseCredentials(false);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(4);
    }

    @Bean
    public LoginSuccessHandler loginSuccessHandler(){
        return new LoginSuccessHandler();
    }

    @Bean
    public LoginFailureHandler loginFailureHandler(){
        return new LoginFailureHandler();
    }

}