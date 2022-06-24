package com.cn.pojo;

import java.time.LocalDateTime;

/**
 * 验证码 数据
 *
 * @author ngcly
 */
public record CaptchaInfo(String code, LocalDateTime expireTime) {

    public CaptchaInfo(String code, int expireIn) {
        this(code, LocalDateTime.now().plusSeconds(expireIn));
    }

    public boolean isExpire() {
        return LocalDateTime.now().isAfter(expireTime);
    }
}
