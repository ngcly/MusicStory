package com.cn;

import com.cn.dao.LoginLogRepository;
import com.cn.entity.LoginLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 系统日志 service
 *
 * @author chen
 * @date 2018-03-22 16:24
 */
@Service
public class LogService {
    @Autowired
    LoginLogRepository loginLogRepository;

    /**
     * 获取登录日志列表
     */
    public Page<LoginLog> getLoginLogList(Pageable pageable,LoginLog loginLog){
        return loginLogRepository.findAll(LoginLogRepository.getLoginLogList(loginLog.getUserName(),loginLog.getUserType(),loginLog.getBeginTime(),loginLog.getEndTime()),pageable);
    }

    /**
     * 保存日志
     * @param loginLog
     */
    @Async
    @Transactional(rollbackFor = Exception.class)
    public void saveLog(LoginLog loginLog){
        loginLogRepository.save(loginLog);
    }
}
