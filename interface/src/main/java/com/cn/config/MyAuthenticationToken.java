package com.cn.config;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

import java.util.Collection;

/**
 * @author chenning
 */
public class MyAuthenticationToken extends AbstractAuthenticationToken {
    private final String loginType;
    private final Object principal;
    private Object credentials;


    /**
     * 认证时过滤器通过这个方法创建Token，传入前端的参数
     * This constructor can be safely used by any code that wishes to create a
     * <code>UsernamePasswordAuthenticationToken</code>, as the {@link #isAuthenticated()}
     * will return <code>false</code>.
     *
     */
    public MyAuthenticationToken(String loginType, Object principal, Object credentials) {
        super(null);
        this.loginType = loginType;
        this.principal = principal;
        this.credentials = credentials;
        setAuthenticated(false);
    }

    /**
     * 认证通过后Provider通过这个方法创建Token，传入自定义信息以及授权信息
     * This constructor should only be used by <code>AuthenticationManager</code> or
     * <code>AuthenticationProvider</code> implementations that are satisfied with
     * producing a trusted (i.e. {@link #isAuthenticated()} = <code>true</code>)
     * authentication token.
     * @param principal
     * @param credentials
     * @param authorities
     */
    public MyAuthenticationToken(String loginType, Object principal, Object credentials,
                                 Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.loginType = loginType;
        this.principal = principal;
        this.credentials = credentials;
        super.setAuthenticated(true); // must use super, as we override
    }

    public String getLoginType() {
        return loginType;
    }

    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        Assert.isTrue(!isAuthenticated,
                "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        super.setAuthenticated(false);
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        this.credentials = null;
    }
}
