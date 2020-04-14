package com.cn;

import com.cn.dao.LoginLogRepository;
import com.cn.entity.LoginLog;
import com.cn.util.IpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 系统日志 service
 *
 * @author chen
 * @date 2018-03-22 16:24
 */
@Service
public class LogService {
    @Autowired
    private LoginLogRepository loginLogRepository;

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
    public void saveLog(String userId, String username, String ip){
        LoginLog loginLog = new LoginLog();
        loginLog.setUserId(userId);
        loginLog.setUserName(username);
        loginLog.setUserType((byte)1);
        loginLog.setLoginIp(ip);
        loginLog.setAddressIp(IpUtil.getIpAddresses(ip));
        loginLog.setLoginTime(new Date());
        loginLogRepository.save(loginLog);
    }
}
