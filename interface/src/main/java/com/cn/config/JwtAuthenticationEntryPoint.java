package com.cn.config;

import cn.hutool.http.ContentType;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONUtil;
import com.cn.pojo.RestCode;
import com.cn.util.RestUtil;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static java.nio.charset.StandardCharsets.UTF_8;

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
        response.setContentType(ContentType.JSON.toString(UTF_8));
        try (PrintWriter printWriter = response.getWriter()) {
            JSON json = JSONUtil.parse(RestUtil.failure(restCode), JSONConfig.create().setOrder(true).setIgnoreNullValue(false));
            JSONUtil.toJsonStr(json,printWriter);
        }
    }
}