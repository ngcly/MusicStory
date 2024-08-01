package com.cn.model;

import com.cn.util.IpUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.apache.http.HttpHeaders;
import org.springframework.security.core.SpringSecurityCoreVersion;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author chenning
 */
@Getter
@ToString
@EqualsAndHashCode
public class AuthenticationDetails implements Serializable {
    @Serial
    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;
    private static final String CAPTCHA_KEY = "captcha";

    private final CaptchaInfo captchaInfo;

    private final String remoteAddress;

    private final String userAgent;

    private final String userCaptcha;

    public AuthenticationDetails(HttpServletRequest request) {
        this(extractCaptchaInfo(request), IpUtil.getIpAddress(request),
                request.getHeader(HttpHeaders.USER_AGENT), request.getParameter(CAPTCHA_KEY));
    }

    public AuthenticationDetails(CaptchaInfo captchaInfo, String remoteAddress,
                                 String userAgent, String userCaptcha) {
        this.captchaInfo = captchaInfo;
        this.remoteAddress = remoteAddress;
        this.userAgent = userAgent;
        this.userCaptcha = userCaptcha;
    }

    private static CaptchaInfo extractCaptchaInfo(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        CaptchaInfo verificationCode = null;
        if (session != null) {
            verificationCode = (CaptchaInfo) session.getAttribute(CAPTCHA_KEY);
            session.removeAttribute(CAPTCHA_KEY);
        }
        return verificationCode;
    }

    public boolean isAdministrator() {
        return Objects.nonNull(captchaInfo);
    }
}
