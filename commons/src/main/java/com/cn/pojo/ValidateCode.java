package com.cn.pojo;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 验证码数据封装类
 * @author ngcly
 */
@Getter
@Setter
public class ValidateCode {
    private String code;

    private LocalDateTime expireTime;

    public ValidateCode(String code, int expireIn) {
        this.code = code;
        this.expireTime = LocalDateTime.now().plusSeconds(expireIn);
    }

    public ValidateCode(String code, LocalDateTime expireTime) {
        this.code = code;
        this.expireTime = expireTime;
    }

    public boolean isExpire() {
        return LocalDateTime.now().isAfter(expireTime);
    }
}
