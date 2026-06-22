package com.cn;

import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.cn.dao.LoginLogRepository;
import com.cn.entity.LoginLog;
import com.cn.enums.LoginStatusEnum;
import com.cn.enums.UserTypeEnum;
import com.cn.model.AuthenticationDetails;
import com.cn.util.IpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.event.AbstractAuthenticationEvent;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Objects;

/**
 * 系统日志 service
 *
 * @author ngcly
 * @since 2018-03-22 16:24
 */
@Service
@RequiredArgsConstructor
public class LogService {
    private final LoginLogRepository loginLogRepository;

    /**
     * 获取登录日志列表
     */
    public Page<LoginLog> getLoginLogList(Pageable pageable, LoginLog loginLog) {
        return loginLogRepository.findAll(LoginLogRepository.getLoginLogList(loginLog.getUserName(),
                loginLog.getUserType(), loginLog.getBeginTime(), loginLog.getEndTime()), pageable);
    }

    @Async
    @Transactional(rollbackFor = Exception.class)
    @EventListener(value = {AbstractAuthenticationFailureEvent.class, AuthenticationSuccessEvent.class})
    public void addLog(AbstractAuthenticationEvent event) {
        Authentication authentication = event.getAuthentication();
        AuthenticationDetails details = (AuthenticationDetails) event.getAuthentication().getDetails();
        UserTypeEnum userType = details.isAdministrator() ? UserTypeEnum.ADMIN : UserTypeEnum.USER;
        String ip = details.getRemoteAddress();
        UserAgent userAgent = UserAgentUtil.parse(details.getUserAgent());
        LoginStatusEnum loginStatus = event instanceof AuthenticationSuccessEvent ?
                LoginStatusEnum.SUCCESS : LoginStatusEnum.FAILURE;

        LoginLog loginLog = new LoginLog();
        loginLog.setUserName(authentication.getName());
        loginLog.setUserType(userType);
        loginLog.setLoginIp(ip);
        loginLog.setLoginAddress(IpUtil.getIpAddresses(ip));
        loginLog.setLoginBrowser(Objects.nonNull(userAgent) ? userAgent.getBrowser().toString() : "");
        loginLog.setLoginStatus(loginStatus);
        loginLog.setLoginTime(Instant.now());
        saveLog(loginLog);
    }

    /**
     * 保存日志
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveLog(LoginLog loginLog) {
        loginLogRepository.save(loginLog);
    }
}
