package com.cn.config;

import com.cn.ManagerService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

/**
 * Spring Security 配置
 * EnableGlobalMethodSecurity(prePostEnabled = true) 开启security注解
 * @author ngcly
 * @date 2018-01-02 17:32
 */
@Configuration
@EnableWebSecurity
@AllArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {
    private final ManagerService managerService;
    private final DataSource dataSource;

    @Bean
    public WebSecurityCustomizer ignoringCustomizer() {
        //忽略静态文件 也可以在下面忽略
        return (web) -> web.ignoring().antMatchers("/webjars/**","/layui/**", "/js/**", "/css/**", "/img/**", "/media/**",
                "/**/favicon.ico","/druid/**");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //验证码过滤器
        ValidateCodeFilter validateCodeFilter = new ValidateCodeFilter();
        validateCodeFilter.setLoginFailureHandler(loginFailureHandler());
        http
                .addFilterBefore(validateCodeFilter,UsernamePasswordAuthenticationFilter.class)
                .formLogin()
                //表单登录的 登录页
                .loginPage("/login")
                .successHandler(loginSuccessHandler())
                .failureHandler(loginFailureHandler())
                .and()
                //开启cookie保存用户数据
                .rememberMe()
                //设置cookie有效期
                .userDetailsService(managerService)
                .tokenRepository(persistentTokenRepository())
                .tokenValiditySeconds(60 * 60 * 24 * 7)
                //设置cookie的私钥
                .key("token")
                .and()
                .logout()
                //默认注销行为为logout，可以通过下面的方式来修改
//                .logoutUrl("/logout")
                //设置注销成功后跳转页面，默认是跳转到登录页面
                .logoutSuccessUrl("/login")
                .invalidateHttpSession(true)
                .deleteCookies("token")
                .and()
                .authorizeRequests()
                .antMatchers("/kaptcha","/login").permitAll()
//                .antMatchers("/admin/**").hasRole("ADMIN")  //该URL只有ADMIN权限才可访问
//                .antMatchers("/db/**").access("hasRole('ADMIN') and hasRole('DBA')") //该URL需具备设定的两个权限才可访问
                .anyRequest().authenticated()
                .and()
                //设置可以iframe访问
                .headers().frameOptions().sameOrigin();

        return http.build();
    }

//    @Override
//    public void configure(AuthenticationManagerBuilder auth) throws Exception {
//        //指定密码加密所使用的加密器为passwordEncoder()
//        //需要将密码加密后写入数据库
//        auth.userDetailsService(managerService).passwordEncoder(passwordEncoder());
//        auth.eraseCredentials(false);
//    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
//        tokenRepository.setCreateTableOnStartup(true);
        return tokenRepository;
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

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}