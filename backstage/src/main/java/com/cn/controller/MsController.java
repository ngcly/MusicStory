package com.cn.controller;

import com.cn.UserService;
import com.cn.entity.User;
import com.cn.util.RestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * 音书管理 控制类
 */
@Controller
@RequestMapping("/ms")
public class MsController {
    @Autowired
    UserService userService;

    /**
     * 用户列表页
     */
    @PreAuthorize("hasAuthority('user:view')")
    @RequestMapping("/user")
    public String managerList(){
        return "user/userList";
    }

    @ResponseBody
    @RequestMapping("/userList")
    public ModelMap getUserList(@RequestParam(value="page",defaultValue="1") Integer page,
                                @RequestParam(value="limit",defaultValue="10") Integer size, User user){
        Page<User> userList = userService.getUserList(PageRequest.of(page - 1, size), user);
        return RestUtil.Success(userList.getTotalElements(),userList.getContent());
    }
}
