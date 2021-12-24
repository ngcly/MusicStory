package com.cn.config;

import cn.hutool.http.ContentType;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONUtil;
import com.cn.LogService;
import com.cn.entity.LoginLog;
import com.cn.entity.Manager;

import com.cn.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * 登录成功后事件
 *
 * @author ngcly
 * @since 2018-01-02 18:53
 */
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    LogService logService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        response.setContentType(ContentType.JSON.toString(UTF_8));
        try (PrintWriter printWriter = response.getWriter()) {
            JSON json = JSONUtil.parse(Result.success(), JSONConfig.create().setOrder(true)
                    .setIgnoreNullValue(false));

            JSONUtil.toJsonStr(json, printWriter);
        }

        Manager userDetails = (Manager) authentication.getPrincipal();
        logService.saveLog(userDetails.getId(), userDetails.getUsername(), LoginLog.USER_TYPE_MANAGER, request);
    }

}
