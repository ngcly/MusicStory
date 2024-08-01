package com.cn.config;

import cn.hutool.core.text.CharSequenceUtil;
import com.cn.model.AuthenticationDetails;
import com.cn.model.CaptchaInfo;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Objects;

/**
 * @author chenning
 */
public class MyAuthenticationProvider extends DaoAuthenticationProvider {

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        //获取详细信息
        AuthenticationDetails details = (AuthenticationDetails) authentication.getDetails();

        String userCaptcha = details.getUserCaptcha();
        CaptchaInfo captchaInfo = details.getCaptchaInfo();
        if (Objects.isNull(captchaInfo) || captchaInfo.isExpire()
                || !CharSequenceUtil.equalsIgnoreCase(captchaInfo.code(), userCaptcha)) {
            throw new AuthenticationServiceException("Captcha error！");
        }
        super.additionalAuthenticationChecks(userDetails, authentication);
    }
}
