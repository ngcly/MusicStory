package com.cn.controller;
import cn.hutool.json.JSONUtil;
import com.cn.UserService;
import com.cn.pojo.RestCode;
import com.cn.util.RestUtil;
import com.xkcoding.http.config.HttpConfig;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.AuthRequestBuilder;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.enums.scope.*;
import me.zhyd.oauth.exception.AuthException;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthToken;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.*;
import me.zhyd.oauth.utils.AuthScopeUtils;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ngcly
 * @version 1.0
 * @since 2021/08/09 9:28
 */
@Slf4j
@Controller
@RequestMapping("/oauth")
public class RestAuthController {

    @Autowired
    private UserService userService;

    @RequestMapping("/render/{code}")
    @ResponseBody
    public void renderAuth(@PathVariable("code") String code, HttpServletResponse response) throws IOException {
        log.info("进入render：" + code);
        AuthRequest authRequest = AuthRequestBuilder.builder()
                .source(code)
                .authConfig((source) -> {
                    // 通过 source 动态获取 AuthConfig
                    // 此处可以灵活的从 sql 中取配置也可以从配置文件中取配置
                    return AuthConfig.builder()
                            .clientId("clientId")
                            .clientSecret("clientSecret")
                            .redirectUri("redirectUri")
                            .build();
                })
                .build();
        String authorizeUrl = authRequest.authorize(AuthStateUtils.createState());
        log.info(authorizeUrl);
        response.sendRedirect(authorizeUrl);
    }

    /**
     * oauth平台中配置的授权回调地址，以本项目为例，在创建github授权应用时的回调地址应为：http://127.0.0.1:8443/oauth/callback/github
     */
    @RequestMapping("/callback/{code}")
    public ModelAndView login(@PathVariable("code") String code, AuthCallback callback, HttpServletRequest request) {
        log.info("进入callback：" + code + " callback params：" + JSONUtil.toJsonStr(callback));
        AuthRequest authRequest = AuthRequestBuilder.builder()
                .source(code)
                .authConfig((source) -> {
                    // 通过 source 动态获取 AuthConfig
                    // 此处可以灵活的从 sql 中取配置也可以从配置文件中取配置
                    return AuthConfig.builder()
                            .clientId("clientId")
                            .clientSecret("clientSecret")
                            .redirectUri("redirectUri")
                            .build();
                })
                .build();
        AuthResponse<AuthUser> response = authRequest.login(callback);
        log.info(JSONUtil.toJsonStr(response));

        if (response.ok()) {
//            userService.save(response.getData());
            return new ModelAndView("redirect:/users");
        }

        Map<String, Object> map = new HashMap<>(1);
        map.put("errorMsg", response.getMsg());

        return new ModelAndView("error", map);
    }

    @RequestMapping("/revoke/{source}/{uuid}")
    @ResponseBody
    public ModelMap revokeAuth(@PathVariable("code") String code, @PathVariable("uuid") String uuid) throws IOException {
        AuthRequest authRequest = AuthRequestBuilder.builder()
                .source(code)
                .authConfig((source) -> {
                    // 通过 source 动态获取 AuthConfig
                    // 此处可以灵活的从 sql 中取配置也可以从配置文件中取配置
                    return AuthConfig.builder()
                            .clientId("clientId")
                            .clientSecret("clientSecret")
                            .redirectUri("redirectUri")
                            .build();
                })
                .build();
//        AuthUser user = userService.getByUuid(uuid);
//        if (null == user) {
//            return RestUtil.failure(RestCode.USER_ERR);
//        }
//        AuthResponse<AuthToken> response;
//        try {
//            response = authRequest.revoke(user.getToken());
//            if (response.ok()) {
//                userService.remove(user.getUuid());
//                return RestUtil.success("用户 [" + user.getUsername() + "] 的 授权状态 已收回！");
//            }
//            return RestUtil.failure(500,"用户 [" + user.getUsername() + "] 的 授权状态 收回失败！" + response.getMsg());
//        } catch (AuthException e) {
//            return RestUtil.failure(500,e.getErrorMsg());
//        }
        return null;
    }

    @RequestMapping("/refresh/{source}/{uuid}")
    @ResponseBody
    public ModelMap refreshAuth(@PathVariable("code") String code, @PathVariable("uuid") String uuid) {
        AuthRequest authRequest = AuthRequestBuilder.builder()
                .source(code)
                .authConfig((source) -> {
                    // 通过 source 动态获取 AuthConfig
                    // 此处可以灵活的从 sql 中取配置也可以从配置文件中取配置
                    return AuthConfig.builder()
                            .clientId("clientId")
                            .clientSecret("clientSecret")
                            .redirectUri("redirectUri")
                            .build();
                })
                .build();
//        AuthUser user = userService.getByUuid(uuid);
//        if (null == user) {
//            return RestUtil.failure(RestCode.USER_ERR);
//        }
//        AuthResponse<AuthToken> response;
//        try {
//            response = authRequest.refresh(user.getToken());
//            if (response.ok()) {
//                user.setToken(response.getData());
//                userService.save(user);
//                return RestUtil.success("用户 [" + user.getUsername() + "] 的 access token 已刷新！新的 accessToken: " + response.getData().getAccessToken());
//            }
//            return RestUtil.failure(500,"用户 [" + user.getUsername() + "] 的 access token 刷新失败！" + response.getMsg());
//        } catch (AuthException e) {
//            return RestUtil.failure(500,e.getErrorMsg());
//        }
        return null;
    }

}