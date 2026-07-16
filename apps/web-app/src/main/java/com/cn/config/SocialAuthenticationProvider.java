package com.cn.config;
 
import com.cn.user.UserService;
import com.cn.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.util.Assert;
 
/**
 * @author chenning
 */
@RequiredArgsConstructor
public class SocialAuthenticationProvider implements AuthenticationProvider {
    private final UserService userService;
 
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Assert.isTrue(!authentication.isAuthenticated(), "Already authenticated");
        Assert.notNull(this.userService, "A UserService must be set");
        Assert.isInstanceOf(MyAuthenticationToken.class, authentication,
                () -> "Only MyAuthenticationToken is supported");
        //获取过滤器封装 of token信息
        MyAuthenticationToken token = (MyAuthenticationToken) authentication;
        User user = userService.socialLogin(token.getLoginType(), token.getPrincipal().toString(), token.getCredentials().toString());
 
        SecurityUser securityUser = new SecurityUser(user);
        return new MyAuthenticationToken(token.getLoginType(), securityUser, user.getPassword(), securityUser.getAuthorities());
    }
 
    @Override
    public boolean supports(Class<?> authentication) {
        return MyAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
