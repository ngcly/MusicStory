package com.cn.controller;

import com.cn.RoleService;
import com.cn.UserService;
import com.cn.dto.RestCode;
import com.cn.entity.Role;
import com.cn.entity.User;
import com.cn.util.RestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


/**
 * 音书管理 控制类
 */
@Controller
@RequestMapping("/ms")
public class MsController {
    @Autowired
    UserService userService;
    @Autowired
    RoleService roleService;

    /**
     * 用户列表页
     */
    @PreAuthorize("hasAuthority('user:view')")
    @RequestMapping("/user")
    public String managerList(){
        return "user/userList";
    }

    /**
     * 获取用户列表信息
     */
    @ResponseBody
    @RequestMapping("/userList")
    public ModelMap getUserList(@RequestParam(value="page",defaultValue="1") Integer page,
                                @RequestParam(value="limit",defaultValue="10") Integer size, User user){
        Page<User> userList = userService.getUserList(PageRequest.of(page - 1, size), user);
        return RestUtil.Success(userList.getTotalElements(),userList.getContent());
    }

    /**
     * 用户编辑页
     */
    @RequestMapping("/userEdit")
    public String userEdit(@RequestParam String userId, Model model){
        User user = userService.getUserDetail(userId);
        Set<Role> roles = roleService.getAvailableRoles((byte) 2);
        ArrayList<String> checkRoleIds = new ArrayList<>();
        for(Role role:user.getRoleList()){
            checkRoleIds.add(role.getId().toString());
            roles.add(role);
        }
        model.addAttribute(user);
        //待选角色列表
        model.addAttribute("roles",roles);
        //已勾选角色ID
        model.addAttribute("checkRoleId",String.join(",",checkRoleIds));
        return "user/userEdit";
    }

    /**
     * 用户详情页
     */
    @RequestMapping("/userDetail")
    public String userDetail(@RequestParam String userId, Model model){
        User user = userService.getUserDetail(userId);
        model.addAttribute(user);
        return "user/userDetail";
    }

    /**
     * 保存用户
     */
    @PreAuthorize("hasAuthority('user:alt')")
    @ResponseBody
    @RequestMapping("/userSave")
    public ModelMap saveUser(@Valid User user){
        try {
            userService.saveUser(user);
            return RestUtil.Success();
        }catch (Exception e){
            e.printStackTrace();
            return RestUtil.Error(RestCode.SERVER_ERROR);
        }
    }

    /**
     * 删除用户
     */
    @PreAuthorize("hasAuthority('user:del')")
    @ResponseBody
    @RequestMapping("/userDel")
    public ModelMap delUser(@RequestParam String userId){
        try {
            userService.delUser(userId);
            return RestUtil.Success();
        }catch (Exception e){
            e.printStackTrace();
            return RestUtil.Error(RestCode.SERVER_ERROR);
        }
    }
}
