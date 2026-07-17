package com.cn.config;

import com.cn.security.ManagerService;
import com.cn.model.RestCode;
import com.cn.util.JacksonUtil;
import lombok.RequiredArgsConstructor;
import org.apache.hc.core5.http.ContentType;
import org.springframework.boot.security.autoconfigure.web.servlet.PathRequest;
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
    private final ManagerService managerService;
    private final MyAuthenticationDetailsSource myAuthenticationDetailsSource;
    private final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    private static final String[] IGNORING_URLS = new String[]{"/captcha", "/webjars/*", "/layui/*", "/js/*", "/css/*",
            "/img/*", "/media/*", "/*/favicon.ico", "/druid/*", "/h2-console/*"};

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) {
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
                            com.cn.model.RestCode restCode = com.cn.model.RestCode.UNAUTHORIZED;
                            org.springframework.http.ProblemDetail problemDetail = org.springframework.http.ProblemDetail.forStatusAndDetail(restCode.status, restCode.msg);
                            problemDetail.setTitle(restCode.msg);
                            problemDetail.setInstance(java.net.URI.create(request.getRequestURI()));

                            response.setStatus(restCode.status.value());
                            response.setContentType("application/problem+json");
                            try (PrintWriter printWriter = response.getWriter()) {
                                printWriter.write(JacksonUtil.stringify(problemDetail));
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
                .rememberMe(remember -> remember.userDetailsService(managerService));

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
    public LoginSuccessHandler loginSuccessHandler(){
        return new LoginSuccessHandler();
    }

    @Bean
    public LoginFailureHandler loginFailureHandler(){
        return new LoginFailureHandler();
    }
}