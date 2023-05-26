package com.cn.config;

import com.cn.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.*;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security 配置
 *
 * @author ngcly
 * @date 2018-02-28 16:23
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class WebSecurityConfig {
    private final JwtTokenUtil jwtTokenUtil;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    /**
     * 放行接口
     */
    private final String[] pass = {"/webjars/**", "swagger-ui.html", "/swagger-ui/**", "/swagger-resources/**", "/v3/**", "/user/upload/**"};

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authManager) throws Exception {
        http
                .authorizeHttpRequests(authorizeHttpRequests ->
                        authorizeHttpRequests
                                .requestMatchers(pass).permitAll()
                                .requestMatchers(HttpMethod.OPTIONS).permitAll()
                                .requestMatchers("/user/**").authenticated())
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
//                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .addFilterBefore(new JwtLoginFilter(authManager, jwtTokenUtil), UsernamePasswordAuthenticationFilter.class)
                .addFilter(new JwtVerifyFilter(authManager, jwtTokenUtil))
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exceptionHandling -> exceptionHandling.authenticationEntryPoint(jwtAuthenticationEntryPoint));
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, UserService userService, PasswordEncoder passwordEncoder) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(new MyAuthenticationProvider(userService))
                .userDetailsService(userService)
                .passwordEncoder(passwordEncoder)
                .init()
                .build();
    }

}