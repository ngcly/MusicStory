package com.cn.config;

import com.cn.user.domain.User;
import com.cn.model.AuthenticationDetails;
import com.cn.model.LogInDTO;
import com.cn.model.RestCode;
import com.cn.util.JacksonUtil;
import org.apache.hc.core5.http.ContentType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;

/**
 * 当前方式是采用框架自带的过滤器实现登录 (使用 ProblemDetail 处理登录失败)
 *
 * @author chenning
 */
public class JwtLoginFilter extends AbstractAuthenticationProcessingFilter {
    private final JwtTokenUtil jwtTokenUtil;

    private static final PathPatternRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER = PathPatternRequestMatcher
            .withDefaults().matcher(HttpMethod.POST, "/login");

    public JwtLoginFilter(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil) {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER, authenticationManager);
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException {
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        LogInDTO logInDTO = JacksonUtil.toObject(request.getInputStream(), LogInDTO.class);
        if(logInDTO instanceof LogInDTO.SocialLoginDTO socialLoginDTO){
            MyAuthenticationToken authRequest = new MyAuthenticationToken(socialLoginDTO.getLoginType().name(), socialLoginDTO.getCode(), socialLoginDTO.getState());
            authRequest.setDetails(new AuthenticationDetails(request));
            return this.getAuthenticationManager().authenticate(authRequest);
        }else{
            LogInDTO.UserNameLoginDTO userNameLoginDTO = (LogInDTO.UserNameLoginDTO) logInDTO;
            UsernamePasswordAuthenticationToken authRequest = UsernamePasswordAuthenticationToken.unauthenticated(userNameLoginDTO.getUsername(),
                    userNameLoginDTO.getPassword());
            authRequest.setDetails(new AuthenticationDetails(request));
            return this.getAuthenticationManager().authenticate(authRequest);
        }
    }

    /**
     * 一旦springSecurity认证登录成功,立即执行该方法
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException {
        SecurityUser securityUser = (SecurityUser) authResult.getPrincipal();
        User user = securityUser.getUser();
        String token = jwtTokenUtil.generateToken(user);
        response.setContentType(ContentType.APPLICATION_JSON.toString());
        response.setHeader(HttpHeaders.AUTHORIZATION, token);
        try (PrintWriter printWriter = response.getWriter()) {
            String jsonStr = JacksonUtil.stringify(user.getUsername());
            printWriter.write(jsonStr);
        }
    }

    /**
     * 一旦springSecurity认证失败,立即执行该方法
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException ex) throws IOException {
        RestCode restCode;
        if (ex instanceof BadCredentialsException ||
                ex instanceof UsernameNotFoundException) {
            restCode = RestCode.USER_ERR;
        } else if (ex instanceof AuthenticationServiceException) {
            restCode = RestCode.VERIFY_CODE_ERR;
        } else if (ex instanceof LockedException) {
            restCode = RestCode.USER_LOCKED;
        } else if (ex instanceof AccountExpiredException) {
            restCode = RestCode.USER_EXPIRE;
        } else if (ex instanceof CredentialsExpiredException) {
            restCode = RestCode.PASSWORD_EXPIRE;
        } else if (ex instanceof DisabledException) {
            restCode = RestCode.USER_DISABLE;
        } else {
            restCode = RestCode.UNAUTHORIZED;
        }

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(restCode.status, restCode.msg);
        problemDetail.setTitle(restCode.msg);
        problemDetail.setInstance(URI.create(request.getRequestURI()));

        response.setStatus(restCode.status.value());
        response.setContentType("application/problem+json");
        try (PrintWriter printWriter = response.getWriter()) {
            String jsonStr = JacksonUtil.stringify(problemDetail);
            printWriter.write(jsonStr);
        }
    }
}
