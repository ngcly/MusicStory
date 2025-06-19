package com.cn.config;

import com.cn.ManagerService;
import com.cn.model.RestCode;
import com.cn.util.JacksonUtil;
import com.cn.util.Result;
import lombok.RequiredArgsConstructor;
import org.apache.http.entity.ContentType;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;
import java.io.PrintWriter;

/**
 * Spring Security 配置
 * EnableGlobalMethodSecurity(prePostEnabled = true) 开启security注解
 *
 * @author ngcly
 * @since 2018-01-02 17:32
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final DataSource dataSource;
    private final ManagerService managerService;
    private final MyAuthenticationDetailsSource myAuthenticationDetailsSource;
    private final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    private static final String[] IGNORING_URLS = new String[]{"/captcha", "/webjars/*", "/layui/*", "/js/*", "/css/*",
            "/img/*", "/media/*", "/*/favicon.ico", "/druid/*", "/h2-console/*"};

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //设置可以iframe访问
        http
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                .authorizeHttpRequests(authorizeHttpRequests ->
                        authorizeHttpRequests
                                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                                .requestMatchers(IGNORING_URLS).permitAll()
                                .anyRequest().access(new MyAuthorizationManager(managerService))
                )
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling.accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setContentType(ContentType.APPLICATION_JSON.toString());
                            try (PrintWriter printWriter = response.getWriter()) {
                                printWriter.write(JacksonUtil.stringify(Result.failure(RestCode.UNAUTHORIZED)));
                            }
                        })
                )
                .csrf(csrf -> csrf.ignoringRequestMatchers(IGNORING_URLS))
                .formLogin(formLogin -> formLogin
                        .authenticationDetailsSource(myAuthenticationDetailsSource)
                        .loginPage("/login")
                        .permitAll()
                        .successHandler(loginSuccessHandler())
                        .failureHandler(loginFailureHandler())
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login")
                        .invalidateHttpSession(true)
                        .deleteCookies("remember-me")
                )
                .rememberMe(remember -> remember
                        .userDetailsService(managerService)
                        .tokenRepository(persistentRepository(dataSource))
                        .tokenValiditySeconds(60 * 60 * 24 * 7));

        return http.build();
    }

    @Bean
    public AuthenticationManager providerManager(ApplicationEventPublisher applicationEventPublisher){
        MyAuthenticationProvider provider = new MyAuthenticationProvider(managerService);
        provider.setPasswordEncoder(passwordEncoder);
        ProviderManager providerManager = new ProviderManager(provider);
        providerManager.setAuthenticationEventPublisher(new DefaultAuthenticationEventPublisher(applicationEventPublisher));
        return providerManager;
    }

//    @Bean
//    public RoleHierarchy roleHierarchy() {
//        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
//        roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_STAFF \n ROLE_STAFF > ROLE_USER");
//        return roleHierarchy;
//    }


    @Bean
    public PersistentTokenRepository persistentRepository(DataSource dataSource) {
        var jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        //自动创建令牌桶，第一次启动需要，第二次启动时需要注释
        jdbcTokenRepository.setCreateTableOnStartup(true);
        return jdbcTokenRepository;
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