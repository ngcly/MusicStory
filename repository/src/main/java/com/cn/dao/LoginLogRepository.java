package com.cn.dao;

import com.cn.entity.LoginLog;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 日志操作
 *
 * @author chen
 * @date 2018-01-02 18:55
 */
public interface LoginLogRepository extends JpaRepository<LoginLog,String>{

}
