package com.cn.config;

import com.cn.util.JacksonUtil;
import org.apache.hc.core5.http.ContentType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * 登录成功后事件
 *
 * @author ngcly
 * @since 2018-01-02 18:53
 */
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        response.setContentType(ContentType.APPLICATION_JSON.toString());
        try (PrintWriter printWriter = response.getWriter()) {
            String jsonStr = JacksonUtil.stringify(Map.of("status", "ok", "message", "登录成功"));
            printWriter.write(jsonStr);
        }
    }

}
