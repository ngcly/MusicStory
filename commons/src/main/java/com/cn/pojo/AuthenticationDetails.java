package com.cn.pojo;

import cn.hutool.http.Header;
import com.cn.util.IpUtil;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.SpringSecurityCoreVersion;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.Serial;
import java.io.Serializable;

/**
 * @author chenning
 */
@Getter
@ToString
@EqualsAndHashCode
public class AuthenticationDetails implements Serializable {
    @Serial
    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;
    private static final String captchaKey = "captcha";

    private final CaptchaInfo captchaInfo;

    private final String remoteAddress;

    private final String userAgent;

    private final String userCaptcha;

    public AuthenticationDetails(HttpServletRequest request) {
        this(extractCaptchaInfo(request), IpUtil.getIpAddress(request),
                request.getHeader(Header.USER_AGENT.getValue()), request.getParameter(captchaKey));
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
            verificationCode = (CaptchaInfo) session.getAttribute(captchaKey);
            session.removeAttribute(captchaKey);
        }
        return verificationCode;
    }
}
