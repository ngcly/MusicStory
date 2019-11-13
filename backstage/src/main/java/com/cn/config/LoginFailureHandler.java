package com.cn.config;

import com.alibaba.fastjson.JSON;
import com.cn.pojo.RestCode;
import com.cn.util.RestUtil;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author chenning
 * @date 2018/3/24 下午7:43
 * 登录失败处理
 */
public class LoginFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        RestCode restCode;
        if (exception instanceof BadCredentialsException ||
                exception instanceof UsernameNotFoundException) {
            restCode = RestCode.USER_ERR;
        } else if(exception instanceof AuthenticationServiceException){
            restCode = RestCode.VERIFYCODE_ERR;
        } else if (exception instanceof LockedException) {
            restCode = RestCode.USER_LOCKED;
        } else if (exception instanceof AccountExpiredException) {
            restCode = RestCode.USER_EXPIRE;
        } else if (exception instanceof CredentialsExpiredException) {
            restCode = RestCode.PASSWORD_EXPIRE;
        } else if (exception instanceof DisabledException) {
            restCode = RestCode.USER_DISABLE;
        } else {
            restCode = RestCode.UNAUTHEN;
        }
        out.write(JSON.toJSON(RestUtil.failure(restCode)).toString());
        out.flush();
        out.close();
    }
}
