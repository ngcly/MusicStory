package com.cn.config;

import com.cn.pojo.AuthenticationDetails;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;

/**
 * @author ngcly
 */
@Component
public class MyAuthenticationDetailsSource implements AuthenticationDetailsSource<HttpServletRequest, AuthenticationDetails> {
    @Override
    public AuthenticationDetails buildDetails(HttpServletRequest context) {
        return new AuthenticationDetails(context);
    }

}
