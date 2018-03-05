package com.cn.social;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.stereotype.Component;

/**
 * 隐式注册
 * @author chenning
 * @date 2018/3/4 下午3:18
 */
@Component
public class MyConnectionSignUp implements ConnectionSignUp{
    @Override
    public String execute(Connection<?> connection) {
        //根据社交用户信息，默认创建用户并返回用户唯一标识
        return connection.getDisplayName();
    }
}
