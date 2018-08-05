package com.cn.controller;

import com.cn.UserService;
import com.cn.config.CurrentUser;
import com.cn.config.CustomerDetail;
import com.cn.config.JwtTokenProvider;
import com.cn.dto.LogInDTO;
import com.cn.dto.SignUpDTO;
import com.cn.entity.Role;
import com.cn.entity.User;
import com.cn.util.RestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.nio.file.attribute.UserPrincipal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 会员控制层
 *
 * @author chen
 * @date 2018-01-05 12:08
 */

@RestController
@RequestMapping("/user")
public class UserController {
    //<a href="https://graph.qq.com/oauth2.0/authorize?response_type=code&client_id=101386962&redirect_uri=http://www.ictgu.cn/login/qq&state=test" class="btn btn-primary btn-block">QQ登录</a>

    /**
     * 获取 用户信息
     * @param customerDetail
     * @return
     */
    @GetMapping("/info")
    public ModelMap userInfo(@CurrentUser CustomerDetail customerDetail) {
        return RestUtil.Success(customerDetail);
    }

    @GetMapping("/test")
    public ModelMap test(){
        return RestUtil.Success("测试成功");
    }

}