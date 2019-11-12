package com.cn.config;

import com.cn.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * Spring Security 配置
 *
 * @author ngcly
 * @date 2018-02-28 16:23
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)//开启security注解
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    RestAuthenticationEntryPoint restAuthenticationEntryPoint;
    @Autowired
    UserService userService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                // 跨域支持
                .cors()
                .and()
                //对请求URL进行权限配置
                .authorizeRequests()
                .antMatchers("/", "/login","/signin","signup", "/github").permitAll()
                .antMatchers(HttpMethod.OPTIONS).permitAll()
                //authenticated 必须要进行身份验证
                .antMatchers("/user/**").authenticated()
                //anonymous 可以以匿名身份登录
                .anyRequest().anonymous()
                .and()
                //认证不通过后的处理
                .exceptionHandling()
                .authenticationEntryPoint(restAuthenticationEntryPoint)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(qqAuthenticationFilter(),UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        //指定密码加密所使用的加密器为passwordEncoder()
        //需要将密码加密后写入数据库
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
        auth.eraseCredentials(false);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        //指定允许跨域的请求(*所有)
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH"));
        // setAllowCredentials(true) is important, otherwise:
        // The value of the 'Access-Control-Allow-Origin' header in the response must not be the wildcard '*' when the request's credentials mode is 'include'.
        configuration.setAllowCredentials(true);
        // setAllowedHeaders is important! Without it, OPTIONS preflight request will fail with 403 Invalid CORS request
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "X-User-Agent", "Content-Type"));
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(4);
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(){
        return new JwtAuthenticationFilter();
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
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