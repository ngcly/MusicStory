package com.cn;

import com.cn.dao.UserRepository;
import com.cn.dto.SignUpDTO;
import com.cn.entity.Role;
import com.cn.entity.User;
import com.cn.util.RestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collections;
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
    @Autowired
    PasswordEncoder passwordEncoder;

    /**
     * 根据用户或邮箱获取用户
     * @param nameOrEmail
     * @return
     */
    public Optional<User> findByUsernameOrEmail(String nameOrEmail) {
        return userRepository.findByUsernameOrEmail(nameOrEmail,nameOrEmail);
    }

    /**
     * 注册账户
     * @param signUpDTO
     * @return
     */
    public ModelMap signUp(SignUpDTO signUpDTO){
        if(userRepository.existsByUsername(signUpDTO.getUsername())) {
            return RestUtil.Error(300,"用户名已存在");
        }

        if(userRepository.existsByEmail(signUpDTO.getEmail())) {
            return RestUtil.Error(300,"该邮箱已注册");
        }

        User user = new User();
        user.setRealName(signUpDTO.getRealName());
        user.setUsername(signUpDTO.getUsername());
        user.setEmail(signUpDTO.getEmail());
        user.setPassword(passwordEncoder.encode(signUpDTO.getPassword()));
        User result = userRepository.save(user);
        //设置前台注册成功跳转页面
        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/users/{username}")
                .buildAndExpand(result.getUsername()).toUri();

        return RestUtil.Success();
    }
}
