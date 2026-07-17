package com.cn.config;

import com.cn.model.RestCode;
import com.cn.util.JacksonUtil;
import org.springframework.http.ProblemDetail;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;

/**
 * 未授权/未登录统一处理 (RFC 7807 ProblemDetail 规范)
 *
 * @author ngcly
 * @version V1.0
 * @since 2020/5/14 19:12
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        RestCode restCode = authException == null ? RestCode.UNAUTHORIZED : RestCode.NOT_LOGIN;
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(restCode.status, restCode.msg);
        problemDetail.setTitle(restCode.msg);
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        
        response.setStatus(restCode.status.value());
        response.setContentType("application/problem+json");
        try (PrintWriter printWriter = response.getWriter()) {
            printWriter.write(JacksonUtil.stringify(problemDetail));
        }
    }
}