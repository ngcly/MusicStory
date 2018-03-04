package com.cn.config;

import com.cn.UserService;
import com.cn.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * 会员登录实现类
 *
 * @author chen
 * @date 2018-02-28 16:49
 */
@Component
public class CustomerDetailService implements UserDetailsService,SocialUserDetailsService{
    @Autowired
    UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findUserByName(username).orElseThrow(() -> new UsernameNotFoundException("用户不存在"));
        Set<GrantedAuthority> authorities = new HashSet<>();
        user.getRoleList().forEach(r -> authorities.add(new SimpleGrantedAuthority(r.getRole())));
        CustomerDetail customer = new CustomerDetail(user);
        return customer;
    }

    @Override
    public SocialUserDetails loadUserByUserId(String userId) throws UsernameNotFoundException {
        User user = userService.findUserByName(userId).orElseThrow(() -> new UsernameNotFoundException("用户不存在"));
        Set<GrantedAuthority> authorities = new HashSet<>();
        user.getRoleList().forEach(r -> authorities.add(new SimpleGrantedAuthority(r.getRole())));
        CustomerDetail customer = new CustomerDetail(user);
        return customer;
    }
}
