package com.cn.config;

import com.cn.constant.LoginType;
import com.cn.entity.User;
import com.cn.pojo.AuthenticationDetails;
import com.cn.pojo.RestCode;
import com.cn.util.JacksonUtil;
import com.cn.util.Result;
import org.apache.http.HttpHeaders;
import org.apache.http.entity.ContentType;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 当前方式是采用框架自带的过滤器实现登录
 * 另一种方式 就是自己去Controller实现登录接口 但是认证还是一样的
 *
 * @author chenning
 */
public class JwtLoginFilter extends AbstractAuthenticationProcessingFilter {
    private final JwtTokenUtil jwtTokenUtil;

    public static final String SPRING_SECURITY_FORM_USERNAME_KEY = "username";

    public static final String SPRING_SECURITY_FORM_PASSWORD_KEY = "password";

    public static final String LOGIN_TYPE_KEY = "loginType";

    private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER = new AntPathRequestMatcher("/login",
            "POST");

    private String usernameParameter = SPRING_SECURITY_FORM_USERNAME_KEY;

    private String passwordParameter = SPRING_SECURITY_FORM_PASSWORD_KEY;

    private String loginTypeParameter = LOGIN_TYPE_KEY;

    private boolean postOnly = true;

    public JwtLoginFilter(JwtTokenUtil jwtTokenUtil) {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER);
        this.jwtTokenUtil = jwtTokenUtil;
    }

    public JwtLoginFilter(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil) {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER, authenticationManager);
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        if (this.postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        String loginType = request.getParameter(this.loginTypeParameter);
        LoginType lt = LoginType.valueOf(loginType);
        if(lt.isSocialLoginType()){
            MyAuthenticationToken authRequest = new MyAuthenticationToken(loginType,request.getParameter("code"), request.getParameter("state"));
            authRequest.setDetails(new AuthenticationDetails(request));
            return this.getAuthenticationManager().authenticate(authRequest);
        }else{
            String username = request.getParameter(this.usernameParameter);
            username = (username != null) ? username.trim() : "";
            String password = request.getParameter(this.passwordParameter);
            password = (password != null) ? password : "";
            UsernamePasswordAuthenticationToken authRequest = UsernamePasswordAuthenticationToken.unauthenticated(username,
                    password);
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
        User user = (User) authResult.getPrincipal();
        String token = jwtTokenUtil.generateToken(user);
        response.setContentType(ContentType.APPLICATION_JSON.toString());
        response.setHeader(HttpHeaders.AUTHORIZATION, token);
        try (PrintWriter printWriter = response.getWriter()) {
            String jsonStr = JacksonUtil.stringify(Result.success(user.getUsername()));
            printWriter.write(jsonStr);
        }
    }

    /**
     * 一旦springSecurity认证失败,立即执行该方法
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException ex) throws IOException {
        response.setContentType(ContentType.APPLICATION_JSON.toString());
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
        try (PrintWriter printWriter = response.getWriter()) {
            String jsonStr = JacksonUtil.stringify(Result.failure(restCode));
            printWriter.write(jsonStr);
        }
    }
}
