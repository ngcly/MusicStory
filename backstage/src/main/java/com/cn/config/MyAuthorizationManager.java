package com.cn.config;

import com.cn.ManagerService;
import com.cn.entity.Manager;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

/**
 * @author ngcly
 */
@RequiredArgsConstructor
public class MyAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {
    private final ManagerService managerService;

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext context) {
        //当前用户名
        String username = authentication.get().getName();
        //admin账号 就没有过不去的坎
        if(Manager.ADMIN.equals(username)) {
            return new AuthorizationDecision(true);
        }

        //当前用户权限信息
        Collection<? extends GrantedAuthority> authorities = authentication.get().getAuthorities();

        //获取当前url
        String currentUrl = context.getRequest().getServletPath();
        String requestMethod = context.getRequest().getMethod();

        String currentUrlKey = String.join("_", requestMethod, currentUrl);

        //url权限 元数据
        List<String> permissionMetadata = managerService.getUrlPermissionMetadata();

        //是否为需要鉴权url
        boolean isAuthenticationUrl = permissionMetadata.contains(currentUrlKey);
        //非鉴权url 直接通过
        if(!isAuthenticationUrl) {
            return new AuthorizationDecision(true);
        }

        boolean isGranted = authorities.stream().anyMatch(grantedAuthority ->
        currentUrlKey.equals(grantedAuthority.getAuthority()));

        return new AuthorizationDecision(isGranted);
    }
}
