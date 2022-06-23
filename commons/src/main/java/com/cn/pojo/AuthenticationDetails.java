package com.cn.pojo;

import cn.hutool.http.Header;
import com.cn.util.IpUtil;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.SpringSecurityCoreVersion;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.Serializable;

/**
 * @author chenning
 */
@Getter
@ToString
@EqualsAndHashCode
public class AuthenticationDetails implements Serializable {
    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;
    private static final String verificationCodeKey = "validateCode";
    private static final String userCodeKey = "randomcode";

    private final VerificationCode verificationCode;

    private final String remoteAddress;

    private final String userAgent;

    private final String userCode;

    public AuthenticationDetails(HttpServletRequest request) {
        this(extractVerificationCode(request), IpUtil.getIpAddress(request),
                request.getHeader(Header.USER_AGENT.name()), request.getParameter(userCodeKey));
    }

    public AuthenticationDetails(VerificationCode validateCode, String remoteAddress,
                                 String userAgent, String userCode) {
        this.verificationCode = validateCode;
        this.remoteAddress = remoteAddress;
        this.userAgent = userAgent;
        this.userCode = userCode;
    }

    private static VerificationCode extractVerificationCode(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        VerificationCode verificationCode = null;
        if (session != null) {
            verificationCode = (VerificationCode) session.getAttribute(verificationCodeKey);
            session.removeAttribute(verificationCodeKey);
        }
        return verificationCode;
    }
}
