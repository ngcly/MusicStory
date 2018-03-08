package com.cn.social.qq;

import org.springframework.social.ApiBinding;

public interface QQ extends ApiBinding {
    /**
     * 获取用户信息
     * @return
     */
    QQUserInfo getUserInfo();
}
