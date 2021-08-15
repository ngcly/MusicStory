package com.cn.config;

import cn.hutool.core.util.StrUtil;
import com.cn.pojo.ValidateCode;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 验证码过滤器
 * @author ngcly
 */
public class ValidateCodeFilter extends OncePerRequestFilter {

    private LoginFailureHandler loginFailureHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {

        if (StrUtil.equalsIgnoreCase("/login", httpServletRequest.getRequestURI())
                && StrUtil.equalsIgnoreCase(httpServletRequest.getMethod(), "post")) {
            try {
                validateCode(httpServletRequest);
            } catch (AuthenticationServiceException e) {
                loginFailureHandler.onAuthenticationFailure(httpServletRequest, httpServletResponse, e);
                return;
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private void validateCode(HttpServletRequest request) {
        ValidateCode codeInSession = (ValidateCode) request.getSession().getAttribute("validateCode");
        String codeInRequest = request.getParameter("randomcode");

        if (codeInSession.isExpire() || !StrUtil.equalsIgnoreCase(codeInSession.getCode(), codeInRequest)) {
            throw new AuthenticationServiceException("验证码错误！");
        }
        request.getSession().removeAttribute("validateCode");
    }

    public void setLoginFailureHandler(LoginFailureHandler loginFailureHandler) {
        this.loginFailureHandler = loginFailureHandler;
    }
}
