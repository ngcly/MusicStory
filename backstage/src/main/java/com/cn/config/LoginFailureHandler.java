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
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * @author ngcly
 * @date 2018/3/24 下午7:43
 * 登录失败处理
 */
public class LoginFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        response.setContentType(ContentType.JSON.toString(UTF_8));
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
            JSON json = JSONUtil.parse(Result.failure(restCode), JSONConfig.create().setOrder(true)
                    .setIgnoreNullValue(false));
            JSONUtil.toJsonStr(json, printWriter);
        }
    }
}
