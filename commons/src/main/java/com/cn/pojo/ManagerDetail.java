package com.cn.pojo;

import com.cn.entity.Manager;
import com.cn.entity.Permission;
import com.cn.entity.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;

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
            this.setAvatar(manager.getAvatar());
            this.setState(manager.getState());
            this.setRoleList(manager.getRoleList());
        }
    }

    /**
     * 加载权限
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new HashSet<>();
        SimpleGrantedAuthority authority;
        for (Role role : this.getRoleList()) {
            authority = new SimpleGrantedAuthority(role.getRoleCode());
            authorities.add(authority);
            for(Permission permission:role.getPermissions()){
                authority = new SimpleGrantedAuthority(permission.getPurview());
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
        if(3==super.getState()){
            return false;
        }
        return true;
    }
}
