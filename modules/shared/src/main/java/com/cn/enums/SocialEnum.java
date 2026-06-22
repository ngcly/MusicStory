package com.cn.enums;

import lombok.Getter;

/**
 * 三方枚举
 * @author ngcly
 * @version V1.0
 * @since 2021/8/26 17:39
 */
@Getter
public enum SocialEnum {
    /** 腾讯QQ */
    QQ("qq","qq","101447968","46474c655bd4f21ddc55cf827e2f04be"),
    /** 腾讯微信 */
    WECHAT("wechat","微信","xxxxxxxxxx","xxxxxxxxxxxxxxxxx");

    private final String source;
    private final String name;
    private final String appId;
    private final String appSecret;

    SocialEnum(String source, String name, String appId, String appSecret) {
        this.source = source;
        this.name = name;
        this.appId = appId;
        this.appSecret = appSecret;
    }

    /**回调地址填写前端地址  不然后面拿到用户信息后没办法通知前端登录成功*/
    public static final String APP_REDIRECT = "https://localhost/oauth/callback";

    /**为了简单起见，这里直接用固定值，该值是为了防止 csrf 攻击*/
    public static final String STATE = "cly";
}
