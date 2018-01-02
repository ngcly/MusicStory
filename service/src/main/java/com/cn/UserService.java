package com.cn;

import com.cn.dao.UserRepository;
import com.cn.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 会员 service 类
 *
 * @author chen
 * @date 2018-01-02 18:20
 */

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public Optional<User> findUserByName(String name) {
        return Optional.ofNullable(userRepository.findUserByName(name));
    }
}
