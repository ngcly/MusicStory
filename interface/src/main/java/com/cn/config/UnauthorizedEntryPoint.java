package com.cn.config;

import cn.hutool.http.ContentType;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONUtil;
import com.cn.pojo.RestCode;
import com.cn.util.Result;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * 自定义未认证 401 返回值
 * @author chen
 * @date 2018-03-01 11:03
 */
@Component
public class UnauthorizedEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.setContentType(ContentType.JSON.toString(UTF_8));

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
            JSON json = JSONUtil.parse(Result.failure(restCode), JSONConfig.create().setOrder(true).setIgnoreNullValue(false));
            JSONUtil.toJsonStr(json,printWriter);
        }
    }
}
