package com.cn.config;

import com.cn.model.RestCode;
import com.cn.util.JacksonUtil;
import com.cn.util.Result;
import org.apache.http.entity.ContentType;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 自定义未认证 401 返回值
 * @author ngcly
 * @date 2018-03-01 11:03
 */
@Component
public class UnauthorizedEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.setContentType(ContentType.APPLICATION_JSON.toString());

        RestCode restCode;
        if (authException instanceof BadCredentialsException ||
                authException instanceof UsernameNotFoundException) {
            restCode = RestCode.USER_ERR;
        } else if(authException instanceof AuthenticationServiceException){
            restCode = RestCode.VERIFY_CODE_ERR;
        } else if (authException instanceof LockedException) {
            restCode = RestCode.USER_LOCKED;
        } else if (authException instanceof AccountExpiredException) {
            restCode = RestCode.USER_EXPIRE;
        } else if (authException instanceof CredentialsExpiredException) {
            restCode = RestCode.PASSWORD_EXPIRE;
        } else if (authException instanceof DisabledException) {
            restCode = RestCode.USER_DISABLE;
        } else if (authException instanceof InsufficientAuthenticationException){
            restCode = RestCode.TOKEN_EXPIRE;
        } else {
            restCode = RestCode.UNAUTHORIZED;
        }

        try (PrintWriter printWriter = response.getWriter()) {
            String jsonStr = JacksonUtil.stringify(Result.failure(restCode));
            printWriter.write(jsonStr);
        }
    }
}
