package com.cn.config;

import com.cn.ManagerService;
import com.cn.entity.Manager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * security 用户登录 service 实现类
 *
 * @author chen
 * @date 2018-01-02 17:39
 */
@Component
public class ManagerDetailService implements UserDetailsService {
    @Autowired
    ManagerService managerService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Manager manager = managerService.findUserByName(username).orElseThrow(() -> new UsernameNotFoundException("用户不存在"));
//        Set<GrantedAuthority> authorities = new HashSet<>();
//        manager.getRoleList().stream().filter(role -> (role.getAvailable())).forEach(r -> authorities.add(new SimpleGrantedAuthority(r.getRoleName())));
        //这里使用securityManager 方便后面可以直接获取当前用户实体
        ManagerDetail user = new ManagerDetail(manager);
        return user;
    }
}
