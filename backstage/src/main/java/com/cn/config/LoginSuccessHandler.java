package com.cn.config;

import com.cn.LogService;
import com.cn.entity.Manager;

import com.cn.util.IpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 登录成功后事件
 *
 * @author ngcly
 * @date 2018-01-02 18:53
 */
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    LogService logService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response, Authentication authentication) throws IOException,
            ServletException {

        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.write("{\"code\":0,\"msg\":\"登录成功\"}");
        out.flush();
        out.close();

        Manager userDetails = (Manager) authentication.getPrincipal();
        logService.saveLog(userDetails.getId(),userDetails.getUsername(), IpUtil.getIpAddress(request));
    }

}
