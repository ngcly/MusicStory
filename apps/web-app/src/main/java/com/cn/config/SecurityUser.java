package com.cn.config;
 
import com.cn.enums.UserStatusEnum;
import com.cn.user.domain.User;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.stream.Collectors;
 
/**
 * Spring Security 适配器，将领域模型 User 包装为 UserDetails
 *
 * @author ngcly
 */
public class SecurityUser implements UserDetails, CredentialsContainer {
    private final User user;
 
    public SecurityUser(User user) {
        this.user = user;
    }
 
    public User getUser() {
        return user;
    }
 
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (user.getRoleList() == null) {
            return java.util.Collections.emptySet();
        }
        return user.getRoleList().stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleCode()))
                .collect(Collectors.toUnmodifiableSet());
    }
 
    @Override
    public String getPassword() {
        return user.getPassword();
    }
 
    @Override
    public String getUsername() {
        return user.getUsername();
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
        return user.getState() != UserStatusEnum.INITIALIZE;
    }
 
    @Override
    public void eraseCredentials() {
        user.setPassword(null);
    }
}
