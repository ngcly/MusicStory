package com.cn.config;

import com.alibaba.fastjson.JSON;
import com.cn.pojo.RestCode;
import com.cn.util.RestUtil;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 自定义未认证 401 返回值
 * @author chen
 * @date 2018-03-01 11:03
 */
@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        RestCode restCode;
        if (authException instanceof BadCredentialsException ||
                authException instanceof UsernameNotFoundException) {
            restCode = RestCode.USER_ERR;
        } else if(authException instanceof AuthenticationServiceException){
            restCode = RestCode.VERIFYCODE_ERR;
        } else if (authException instanceof LockedException) {
            restCode = RestCode.USER_LOCKED;
        } else if (authException instanceof AccountExpiredException) {
            restCode = RestCode.USER_EXPIRE;
        } else if (authException instanceof CredentialsExpiredException) {
            restCode = RestCode.PASSWORD_EXPIRE;
        } else if (authException instanceof DisabledException) {
            restCode = RestCode.USER_DISABLE;
        } else {
            restCode = RestCode.UNAUTHEN;
        }
        out.write(JSON.toJSON(RestUtil.failure(restCode)).toString());
        out.flush();
        out.close();
    }
}
