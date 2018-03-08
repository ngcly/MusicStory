package com.cn.social;

import com.cn.dao.UserRepository;
import com.cn.entity.User;
import com.cn.social.qq.QQUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UserProfile;
import org.springframework.stereotype.Component;

/**
 * 隐式注册
 * @author chenning
 * @date 2018/3/4 下午3:18
 */
@Component
public class MyConnectionSignUp implements ConnectionSignUp{
    @Autowired
    UserRepository userRepository;

    @Override
    public String execute(Connection<?> connection) {
        //根据社交用户信息，默认创建用户并返回用户唯一标识
        UserProfile profile = connection.fetchUserProfile();
        User user = new User();
        user.setId(profile.getId());
        user.setUsername(profile.getUsername());
        user.setEmail(profile.getEmail());
        userRepository.save(user);
        return user.getUsername();
    }
}
