package com.cn.config;

import com.cn.ManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author ngcly
 */
@RequiredArgsConstructor
public class MyAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {
    private final ManagerService managerService;

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext context) {
        //当前用户权限信息
        Collection<? extends GrantedAuthority> authorities = authentication.get().getAuthorities();

        //获取当前url
        String currentUrl = context.getRequest().getServletPath();
        String method = context.getRequest().getMethod();

        //这种方式虽然简单 但是会导致 部分路由 等不需要具体授权的url也没有权限访问。 该方案比较适合前后分离模式
//        boolean isGranted = authorities.stream().anyMatch(grantedAuthority ->
//        currentUrl.equals(grantedAuthority.getAuthority()));


        //url和角色映射关系 元数据
        Map<String, Set<String>> urlRoleMap = managerService.getAuthorizationMetaMap();

        //是否为需要鉴权url
        boolean isAuthenticationUrl = urlRoleMap.containsKey(currentUrl);
        //非鉴权url 直接通过
        if(!isAuthenticationUrl) {
            return new AuthorizationDecision(true);
        }

        //从映射关系中 获取url对应的角色
        Set<String> urlRoles = urlRoleMap.get(currentUrl);

        //获取用户角色列表
        Set<String> userRoles = authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
        //判断用户角色列表中是否含有url需要的任意角色之一
        boolean isGranted = CollectionUtils.containsAny(urlRoles, userRoles);
        return new AuthorizationDecision(isGranted);
    }
}
