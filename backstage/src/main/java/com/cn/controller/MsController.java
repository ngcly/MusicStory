package com.cn.controller;

import com.cn.ClassifyService;
import com.cn.RoleService;
import com.cn.UserService;
import com.cn.dto.RestCode;
import com.cn.entity.Classify;
import com.cn.entity.Role;
import com.cn.entity.User;
import com.cn.util.RestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
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
    @Autowired
    ClassifyService classifyService;

    /**
     * 用户列表页
     */
    @PreAuthorize("hasAuthority('user:view')")
    @RequestMapping("/user")
    public String userList(){
        return "user/userList";
    }

    /**
     * 获取用户列表信息
     */
    @ResponseBody
    @RequestMapping("/userList")
    public ModelMap getUserList(@RequestParam(value="page",defaultValue="1") Integer page,
                                @RequestParam(value="size",defaultValue="10") Integer size, User user){
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
     * 修改用户
     */
    @PreAuthorize("hasAuthority('user:alt')")
    @ResponseBody
    @RequestMapping("/userSave")
    public ModelMap saveUser(@Valid User user){
        try {
            userService.altUser(user);
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

    /**
     * 分类列表页
     */
    @PreAuthorize("hasAuthority('clazz:view')")
    @RequestMapping("/classify")
    public String classifyList(){
        return "classify/classifyList";
    }

    /**
     * 获取分类列表
     */
    @ResponseBody
    @RequestMapping("/classifyList")
    public ModelMap getClassifyList(@RequestParam(value="page",defaultValue="1") Integer page,
                                    @RequestParam(value="size",defaultValue="10") Integer size, Classify classify){
        Page<Classify> classifyList = classifyService.getClassifyList(PageRequest.of(page - 1, size), classify);
        return RestUtil.Success(classifyList.getTotalElements(),classifyList.getContent());
    }

    /**
     * 修改分类页
     */
    @RequestMapping("/classifyAlt")
    public String altClassify(@RequestParam(required = false)Long id,Model model){
        Classify classify = new Classify();
        if(id!=null){
            classify = classifyService.getClassifyDetail(id);
        }
        model.addAttribute(classify);
        return "classify/classifyEdit";
    }

    /**
     * 保存分类
     */
    @PreAuthorize("hasAnyAuthority('clazz:add','clazz:alt')")
    @ResponseBody
    @PostMapping("/saveClassify")
    public ModelMap saveClassify(@Valid Classify classify){
        try {
            classifyService.saveClassify(classify);
            return RestUtil.Success();
        }catch (Exception e){
            e.printStackTrace();
            return RestUtil.Error(RestCode.SERVER_ERROR);
        }
    }

    /**
     * 删除分类
     */
    @PreAuthorize("hasAuthority('clazz:del')")
    @ResponseBody
    @GetMapping("/classifyDel/{id}")
    public ModelMap delClassify(@PathVariable("id")Long id){
        try {
            classifyService.deleteClassify(id);
            return RestUtil.Success();
        }catch (Exception e){
            e.printStackTrace();
            return RestUtil.Error(RestCode.SERVER_ERROR);
        }
    }
}
