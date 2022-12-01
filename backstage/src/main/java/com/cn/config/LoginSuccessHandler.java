package com.cn.config;

import com.cn.util.JacksonUtil;
import com.cn.util.Result;
import org.apache.http.entity.ContentType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

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
            String jsonStr = JacksonUtil.stringify(Result.success());
            printWriter.write(jsonStr);
        }
    }

}
