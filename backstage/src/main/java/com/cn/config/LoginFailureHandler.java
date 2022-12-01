package com.cn.config;

import com.cn.pojo.RestCode;
import com.cn.util.JacksonUtil;
import com.cn.util.Result;
import org.apache.http.entity.ContentType;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author ngcly
 * @date 2018/3/24 下午7:43
 * 登录失败处理
 */
public class LoginFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        response.setContentType(ContentType.APPLICATION_JSON.toString());
        RestCode restCode;
        if (exception instanceof BadCredentialsException ||
                exception instanceof UsernameNotFoundException) {
            restCode = RestCode.USER_ERR;
        } else if (exception instanceof AuthenticationServiceException) {
            restCode = RestCode.VERIFY_CODE_ERR;
        } else if (exception instanceof LockedException) {
            restCode = RestCode.USER_LOCKED;
        } else if (exception instanceof AccountExpiredException) {
            restCode = RestCode.USER_EXPIRE;
        } else if (exception instanceof CredentialsExpiredException) {
            restCode = RestCode.PASSWORD_EXPIRE;
        } else if (exception instanceof DisabledException) {
            restCode = RestCode.USER_DISABLE;
        } else {
            restCode = RestCode.UNAUTHORIZED;
        }
        try (PrintWriter printWriter = response.getWriter()) {
            String jsonStr = JacksonUtil.stringify(Result.failure(restCode));
            printWriter.write(jsonStr);
        }
    }
}
