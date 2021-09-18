package com.cn.config;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.Header;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import com.cn.pojo.UserDetail;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * JWT工具类
 * @author ngcly
 * @since 2020/5/13 17:30
 * @version V1.0
 */
@Component
public class JwtTokenUtil {
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private Integer expiration;

    private static final String BEARER = "Bearer ";

    /**
     * 生成 user token
     * @param user 用户信息
     * @return jwt字符串
     */
    public String generateToken(UserDetail user) {
        final Date now = new Date();
        //创建jwt
        return JWT.create()
                .setSubject(user.getUsername())
                .addPayloads(BeanUtil.beanToMap(user))
                .setIssuedAt(now)
                .setExpiresAt(DateUtil.offsetSecond(now,expiration))
                .setKey(SecureUtil.md5(secret).getBytes())
                .sign();
    }

    public String generateToken(String str) {
        return generateToken(str,expiration);
    }

    public String generateToken(String str, Integer expTime) {
        return generateToken(str,expTime,DateField.SECOND);
    }

    public String generateToken(String str, Integer expTime, DateField dateField) {
        final Date now =new Date();
        return JWT.create()
                .setSubject(str)
                .setIssuedAt(now)
                .setExpiresAt(DateUtil.offset(now,dateField,expTime))
                .setKey(SecureUtil.md5(secret).getBytes())
                .sign();
    }

    /**
     * 刷新token
     * @param token token字符串
     * @return jwt字符串
     */
    public String refreshToken(String token) {
        final Date now = new Date();
        return JWTUtil.parseToken(token)
                .setIssuedAt(now)
                .setExpiresAt(DateUtil.offsetSecond(now,expiration))
                .sign();
    }

    /**
     * 验证jwt
     * @param jwt jwt对象
     * @return boolean
     */
    public boolean verify(JWT jwt){
        return jwt.setKey(SecureUtil.md5(secret).getBytes()).validate(0L);
    }

    /**
     * 从Http请求里获取token
     * @param request 请求
     * @return String
     */
    public static String getToken(HttpServletRequest request){
        String jwtToken = request.getHeader(Header.AUTHORIZATION.toString());
        if(StringUtils.hasLength(jwtToken) && jwtToken.startsWith(BEARER)){
            return jwtToken.substring(BEARER.length());
        }
        return null;
    }

}
