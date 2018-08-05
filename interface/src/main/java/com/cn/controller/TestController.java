package com.cn.controller;

import com.cn.UserService;
import com.cn.config.JwtTokenProvider;
import com.cn.dto.LogInDTO;
import com.cn.dto.SignUpDTO;
import com.cn.util.RestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * 描述:
 * 测试
 *
 * @author chen
 * @create 2018-08-05 14:48
 */
@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtTokenProvider tokenProvider;
    @Autowired
    UserService userService;

    /**
     * 登陆
     * @param loginDTO
     * @return
     */
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LogInDTO loginDTO) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDTO.getUsernameOrEmail(),
                        loginDTO.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);
        Map<String,String> map = new HashMap<>();
        map.put("tokenType","Bearer");
        map.put("accessToken",jwt);
        return ResponseEntity.ok(RestUtil.Success(map));
    }

    /**
     * 注册
     * @param signUpDTO
     * @return
     */
    @PostMapping("/signup")
    public ModelMap registerUser(@Valid @RequestBody SignUpDTO signUpDTO) {
        return userService.signUp(signUpDTO);
    }
}
