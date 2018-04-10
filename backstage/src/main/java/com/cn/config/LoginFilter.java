package com.cn.config;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录验证过滤器
 *
 * @author chen
 * @date 2018-01-02 17:28
 */
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    public LoginFilter(){
        AntPathRequestMatcher requestMatcher = new AntPathRequestMatcher("/login", "POST");
        this.setRequiresAuthenticationRequestMatcher(requestMatcher);
        this.setAuthenticationManager(getAuthenticationManager());
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        // 在这里进行验证码的校验
        // 取出验证码
        String validateCode = (String) request.getSession().getAttribute("validateCode");
        // 取出页面的验证码
        String randomcode = request.getParameter("randomcode");
        // 输入的验证码和session中的验证进行对比
        if (randomcode != null && validateCode != null && !randomcode.equals(validateCode)) {
            // 如果校验失败，将验证码错误失败信息设置到request中
            throw new AuthenticationServiceException("验证码错误");
        }
        request.getSession().removeAttribute("validateCode");
        return super.attemptAuthentication(request, response);
    }
}
