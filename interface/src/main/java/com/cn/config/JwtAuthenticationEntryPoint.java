package com.cn.config;

import com.cn.pojo.RestCode;
import com.cn.util.JacksonUtil;
import com.cn.util.Result;
import org.apache.http.entity.ContentType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 未授权配置
 * @author ngcly
 * @since 2020/5/14 19:12
 * @version V1.0
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        RestCode restCode = authException==null?RestCode.UNAUTHORIZED:RestCode.NOT_LOGIN;
        response.setContentType(ContentType.APPLICATION_JSON.toString());
        try (PrintWriter printWriter = response.getWriter()) {
            String jsonStr = JacksonUtil.stringify(Result.failure(restCode));
            printWriter.write(jsonStr);
        }
    }
}