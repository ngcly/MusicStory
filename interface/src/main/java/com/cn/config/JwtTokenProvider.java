package com.cn.config;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import java.util.Date;

/**
 * 描述:
 * JWT 工具类
 *
 * @author chen
 * @create 2018-08-05 12:30
 */
@Component
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    /**
     * 密钥
     */
    @Value("${app.jwtSecret}")
    private String jwtSecret;

    /**
     * 过期时间
     */
    @Value("${app.jwtExpirationInMs}")
    private int jwtExpirationInMs;

    /**
     * 生成 token
     */
    public String generateToken(Authentication authentication) {
        CustomerDetail customerDetail= (CustomerDetail) authentication.getPrincipal();

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        return Jwts.builder()
                .setSubject(customerDetail.getId())
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    /**
     * 从 token 中获取用户Id
     */
    public String getUserNameFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    /**
     * 验证 token
     */
    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            logger.error("JWT 签名不合法");
        } catch (MalformedJwtException ex) {
            logger.error("JWT token 不合法");
        } catch (ExpiredJwtException ex) {
            logger.error("JWT token 过期");
        } catch (UnsupportedJwtException ex) {
            logger.error("JWT token 不支持");
        } catch (IllegalArgumentException ex) {
            logger.error("JWT 主体为空.");
        }
        return false;
    }
}
