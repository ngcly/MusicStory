package com.cn.config;

import cn.hutool.core.date.DateUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import cn.hutool.jwt.RegisteredPayload;
import com.cn.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 请求校验
 *
 * @author chenning
 */
@Slf4j
public class JwtVerifyFilter extends BasicAuthenticationFilter {
    private final JwtTokenUtil jwtTokenUtil;

    public JwtVerifyFilter(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil) {
        super(authenticationManager);
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = JwtTokenUtil.getToken(request);
        if (StringUtils.hasLength(token)) {
            User user = null;
            LocalDateTime issuedAt = null;
            try {
                JWT jwt = JWTUtil.parseToken(token);
                if (jwtTokenUtil.verify(jwt)) {
                    user = jwt.getPayload().getClaimsJson().toBean(User.class);
                    issuedAt = DateUtil.toLocalDateTime(jwt.getPayloads().getDate(RegisteredPayload.ISSUED_AT));
                }
            } catch (Exception e) {
                log.info("token 无效:{}", e.getMessage());
            }

            if (validUserAuthenticated(user, issuedAt)) {
                UsernamePasswordAuthenticationToken authenticationToken =
                        UsernamePasswordAuthenticationToken.authenticated(user, null, user.getAuthorities());
                log.info("authenticated user:{}", authenticationToken);
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        chain.doFilter(request, response);
    }

    /**
     * 判断是否已授权
     *
     * @param userDetail 用户信息
     * @param issuedAt   jwt有效期
     * @return boolean
     */
    public boolean validUserAuthenticated(User userDetail, LocalDateTime issuedAt) {
        return Objects.nonNull(userDetail)
                && Objects.isNull(SecurityContextHolder.getContext().getAuthentication())
                && Objects.nonNull(issuedAt)
                && (Objects.isNull(userDetail.getPwdAlt()) || issuedAt.isAfter(userDetail.getPwdAlt()));
    }
}
