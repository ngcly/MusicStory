package com.cn.config;

import com.cn.dao.LoginLogRepository;
import com.cn.entity.LoginLog;
import com.cn.entity.Manager;
import com.cn.util.IpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * 登录成功后事件
 *
 * @author chen
 * @date 2018-01-02 18:53
 */
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    @Autowired
    LoginLogRepository loginLogRepository;

    @Async
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response, Authentication authentication) throws IOException,
            ServletException {
        //获得授权后可得到用户信息   可使用SUserService进行数据库操作
        Manager userDetails = (Manager) authentication.getPrincipal();
        LoginLog loginLog = new LoginLog();
        loginLog.setUserId(userDetails.getId());
        loginLog.setUserName(userDetails.getUsername());
        loginLog.setUserType((byte)1);
        String ip = IpUtil.getIp(request);
        loginLog.setLoginIp(ip);
        loginLog.setAddressIp(IpUtil.getIpAddresses(ip));
        loginLog.setLoginTime(new Date());
        loginLogRepository.save(loginLog);
        super.onAuthenticationSuccess(request, response, authentication);
    }

}
