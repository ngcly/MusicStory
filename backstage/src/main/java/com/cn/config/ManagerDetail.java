package com.cn.config;

import com.cn.entity.Manager;
import com.cn.entity.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

/**
 * security用户与数据库用户 合体信息封装类
 *
 * @author chen
 * @date 2018-01-02 17:36
 */
public class ManagerDetail extends Manager implements UserDetails{
    private static final long serialVersionUID = 1L;
    public ManagerDetail(Manager manager) {
        if(manager != null)
        {
            this.setId(manager.getId());
            this.setUsername(manager.getUsername());
            this.setRealName(manager.getRealName());
            this.setPassword(manager.getPassword());
            this.setGender(manager.getGender());
            this.setRoleList(manager.getRoleList());
            this.setAvatar(manager.getAvatar());
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        Set<Role> userRoles = this.getRoleList();

        if(userRoles != null)
        {
            for (Role role : userRoles) {
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.getRole());
                authorities.add(authority);
            }
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return super.getPassword();
    }

    @Override
    public String getUsername() {
        return super.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
