package com.cn.config;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.cn.model.RestCode;
import com.cn.util.JacksonUtil;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;

/**
 * @author ngcly
 * @date 2018/3/24 下午7:43
 * 登录失败处理 (RFC 7807 ProblemDetail 规范)
 */
public class LoginFailureHandler implements AuthenticationFailureHandler {
    private static final Log log = LogFactory.get();

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        log.error("Login failed with exception: ", exception);
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

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(restCode.status, restCode.msg);
        problemDetail.setTitle(restCode.msg);
        problemDetail.setInstance(URI.create(request.getRequestURI()));

        response.setStatus(restCode.status.value());
        response.setContentType("application/problem+json;charset=UTF-8");
        try (PrintWriter printWriter = response.getWriter()) {
            printWriter.write(JacksonUtil.stringify(problemDetail));
        }
    }
}
