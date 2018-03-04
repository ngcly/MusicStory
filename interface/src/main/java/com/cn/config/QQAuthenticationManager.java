package com.cn.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cn.entity.User;
import org.jsoup.Jsoup;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.nodes.Document;
import org.springframework.security.core.AuthenticationException;

import static com.cn.config.QQAuthenticationFilter.clientId;


/**
 * QQ授权自定义
 *
 * @author chen
 * @date 2018-03-01 11:23
 */
public class QQAuthenticationManager implements AuthenticationManager {
    private static final List<GrantedAuthority> AUTHORITIES = new ArrayList<>();

    /**
     * 获取 QQ 登录信息的 API 地址
     */
    private final static String userInfoUri = "https://graph.qq.com/user/get_user_info";

    /**
     * 获取 QQ 用户信息的地址拼接
     */
    private final static String USER_INFO_API = "%s?access_token=%s&oauth_consumer_key=%s&openid=%s";

    static {
        AUTHORITIES.add(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException {
        if (auth.getName() != null && auth.getCredentials() != null) {
            User user = getUserInfo(auth.getName(), (String) (auth.getCredentials()));
            return new UsernamePasswordAuthenticationToken(user,
                    null, AUTHORITIES);
        }
        throw new BadCredentialsException("Bad Credentials");
    }

    private User getUserInfo(String accessToken, String openId) {
        String url = String.format(USER_INFO_API, userInfoUri, accessToken, clientId, openId);
        Document document;
        try {
            document = Jsoup.connect(url).get();
        } catch (IOException e) {
            throw new BadCredentialsException("Bad Credentials!");
        }
        String resultText = document.text();

        JSONObject json = JSON.parseObject(resultText);

        User user = new User();
        user.setUsername(openId);
        user.setNickName(json.getString("nickname"));
        user.setGender(Byte.decode(json.getString("gender")));
        user.setAddress(json.getString("province"));
//        user.setBirthday(json.getString("year"));
        user.setAvatar(json.getString("figureurl_qq_2"));

        return user;
    }
}
