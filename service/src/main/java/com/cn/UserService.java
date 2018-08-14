package com.cn;

import com.cn.dao.UserRepository;
import com.cn.entity.User;
import com.cn.util.RestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

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
    @Autowired
    PasswordEncoder passwordEncoder;

    /**
     * 注册账户
     * @param signUpUser
     * @return
     */
    public ModelMap signUp(User signUpUser){
        if(userRepository.existsByUsername(signUpUser.getUsername())) {
            return RestUtil.Error(300,"用户名已存在");
        }

        if(userRepository.existsByEmail(signUpUser.getEmail())) {
            return RestUtil.Error(300,"该邮箱已注册");
        }

        User user = new User();
        user.setRealName(signUpUser.getRealName());
        user.setUsername(signUpUser.getUsername());
        user.setEmail(signUpUser.getEmail());
        user.setPassword(passwordEncoder.encode(signUpUser.getPassword()));
        User result = userRepository.save(user);
        //设置前台注册成功跳转页面
        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/users/{username}")
                .buildAndExpand(result.getUsername()).toUri();

        return RestUtil.Success();
    }

    public void saveUser(User user){
        userRepository.save(user);
    }
}
