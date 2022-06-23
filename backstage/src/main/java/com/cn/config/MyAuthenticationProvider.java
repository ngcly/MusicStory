package com.cn.config;

import cn.hutool.core.util.StrUtil;
import com.cn.pojo.AuthenticationDetails;
import com.cn.pojo.VerificationCode;
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

        String userCode = details.getUserCode();
        VerificationCode verificationCode = details.getVerificationCode();
        if (Objects.isNull(verificationCode) || verificationCode.isExpire()
                || !StrUtil.equalsIgnoreCase(verificationCode.code(), userCode)) {
            throw new AuthenticationServiceException("Verification code error！");
        }
        super.additionalAuthenticationChecks(userDetails, authentication);
    }
}
