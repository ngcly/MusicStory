package com.cn;

import cn.hutool.http.Header;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.cn.dao.LoginLogRepository;
import com.cn.entity.LoginLog;
import com.cn.util.IpUtil;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 系统日志 service
 * @author ngcly
 * @since 2018-03-22 16:24
 */
@Service
@AllArgsConstructor
public class LogService {
    private final LoginLogRepository loginLogRepository;

    /**
     * 获取登录日志列表
     */
    public Page<LoginLog> getLoginLogList(Pageable pageable,LoginLog loginLog){
        return loginLogRepository.findAll(LoginLogRepository.getLoginLogList(loginLog.getUserName(),loginLog.getUserType(),loginLog.getBeginTime(),loginLog.getEndTime()),pageable);
    }

    /**
     * 保存日志
     */
    @Async
    public void saveLog(Long userId, String username, Byte userType, HttpServletRequest request){
        String ip = IpUtil.getIpAddress(request);
        String ua = request.getHeader(Header.USER_AGENT.toString());
        UserAgent userAgent = UserAgentUtil.parse(ua);

        LoginLog loginLog = new LoginLog();
        loginLog.setUserId(userId);
        loginLog.setUserName(username);
        loginLog.setUserType(userType);
        loginLog.setLoginIp(ip);
        loginLog.setLoginAddress(IpUtil.getIpAddresses(ip));
        loginLog.setLoginBrowser(Objects.nonNull(userAgent)?userAgent.getBrowser().toString():"");
        loginLog.setLoginTime(LocalDateTime.now());
        loginLogRepository.save(loginLog);
    }

}
