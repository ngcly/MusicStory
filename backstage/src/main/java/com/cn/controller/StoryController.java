package com.cn.controller;

import com.cn.UserService;
import com.cn.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

/**
 * 音书管理 控制类
 */
@Controller
@RequestMapping("/ms")
public class StoryController {
    @Autowired
    UserService userService;

    /**
     * 用户列表页
     * @param pageable
     * @param model
     * @param user
     * @return
     */
    @PreAuthorize("hasAuthority('user:view')")
    @RequestMapping("/userList")
    public String managerList(@PageableDefault(sort = { "createdTime" }, direction = Sort.Direction.DESC)
                                      Pageable pageable, Model model, @Valid User user){
        Page<User> userList = userService.getUserList(pageable,user);
        model.addAttribute("users",userList);
        return "user/userList";
    }
}
