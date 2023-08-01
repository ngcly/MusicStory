package com.cn.config;

import com.cn.ManagerService;
import com.cn.pojo.RestCode;
import com.cn.util.JacksonUtil;
import com.cn.util.Result;
import lombok.RequiredArgsConstructor;
import org.apache.http.entity.ContentType;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import java.io.PrintWriter;
import java.util.List;

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
    private final PersistentTokenRepository persistentRepository;
    private final LoginSuccessHandler loginSuccessHandler;
    private final LoginFailureHandler loginFailureHandler;
    private final MyAuthenticationDetailsSource myAuthenticationDetailsSource;

    private static final String[] IGNORING_URLS = new String[]{"/captcha", "/webjars/*", "/layui/*", "/js/*", "/css/*",
            "/img/*", "/media/*", "/*/favicon.ico", "/druid/*"};

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
                        .successHandler(loginSuccessHandler)
                        .failureHandler(loginFailureHandler)
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login")
                        .invalidateHttpSession(true)
                        .deleteCookies("remember-me")
                )
                .rememberMe(remember -> remember
                        .userDetailsService(managerService)
                        .tokenRepository(persistentRepository)
                        .tokenValiditySeconds(60 * 60 * 24 * 7));

        return http.build();
    }

    @Bean
    public ProviderManager providerManager(PasswordEncoder passwordEncoder){
        MyAuthenticationProvider provider = new MyAuthenticationProvider();
        provider.setUserDetailsService(managerService);
        provider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(List.of(provider));
    }

//    @Bean
//    public RoleHierarchy roleHierarchy() {
//        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
//        roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_STAFF \n ROLE_STAFF > ROLE_USER");
//        return roleHierarchy;
//    }

}