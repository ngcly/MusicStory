package com.cn.controller;

import com.cn.ManagerService;
import com.cn.entity.Manager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

/**
 * 后台客服管理员 控制类
 *
 * @author chen
 * @date 2018-01-02 18:25
 */
public class ManagerController {
    @Autowired
    ManagerService managerService;

    /***
     * 下面两个底层实现一样  唯一区别就是hasRole默认添加 ROLE_ 前缀
     * @PreAuthorize("hasAuthority('ROLE_admin')")
     * @PreAuthorize("hasRole('admin')") 方法调用前判断是否有权限
     * @PreAuthorize("hasPermission('','sys:user')") 判断自定义权限标识符
     * @PostAuthorize("returnObject.id%2==0") 方法调用完后判断 若为false则无权限  基本用不上
     * @PostFilter("filterObject.id%2==0") 对返回结果进行过滤  filterObject内置为返回对象
     * @PreFilter(filterTarget="ids", value="filterObject%2==0") 对方法参数进行过滤 如有多参则指定参数 ids为其中一个参数
     */



    /**
     * 注册
     * @param manager
     * @return
     */
    @RequestMapping("/register")
    public String register(@Valid Manager manager){
        Manager manager1 = managerService.findUserByName(manager.getUsername()).get();
        if(manager1!=null){
            //将密码加密
            BCryptPasswordEncoder bc=new BCryptPasswordEncoder(4);
            manager.setPassword(bc.encode(manager.getPassword()));
            managerService.saveManager(manager);
        }
        return "login";
    }
}
