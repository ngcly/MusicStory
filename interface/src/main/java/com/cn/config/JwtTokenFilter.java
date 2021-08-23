package com.cn.config;

import cn.hutool.core.date.DateUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.cn.pojo.UserDetail;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * 校验token过滤器
 * @author ngcly
 * @since 2020/5/13 16:35
 * @version V1.0
 */
public class JwtTokenFilter extends OncePerRequestFilter {
    private static final Log log = LogFactory.get();

    private final UserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;

    private static final String BEARER = "Bearer ";

    public JwtTokenFilter(UserDetailsService userDetailsService, JwtTokenUtil jwtTokenUtil) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwtToken = request.getHeader("authorization");
        if(StringUtils.hasLength(jwtToken) && jwtToken.startsWith(BEARER)){
            UserDetail user = null;
            LocalDateTime issuedAt = null;
            try {
                JWT jwt = JWTUtil.parseToken(jwtToken.substring(BEARER.length()));
                if(jwtTokenUtil.verify(jwt)){
                    user = jwt.getPayload().getClaimsJson().toBean(UserDetail.class);
                    issuedAt = DateUtil.toLocalDateTime(jwt.getPayloads().getDate(JWT.ISSUED_AT));
                }
            } catch (Exception e) {
                log.info("token 无效:{}", e.getMessage());
            }

            if (user != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                if(issuedAt!=null && issuedAt.isAfter(user.getPwdAlt())){
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                    log.info("authenticated user:{}", authenticationToken);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        }
        filterChain.doFilter(request,response);
    }

}