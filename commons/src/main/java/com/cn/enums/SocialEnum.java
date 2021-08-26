package com.cn.enums;

/**
 * 三方枚举
 * @author ngcly
 * @version V1.0
 * @since 2021/8/26 17:39
 */
public enum SocialEnum {
    /** 腾讯QQ */
    QQ("qq","qq"),
    /** 腾讯微信 */
    WECHAT("wechat","微信");

    private final String source;
    private final String name;

    SocialEnum(String source, String name) {
        this.source = source;
        this.name = name;
    }

    public String getSource() {
        return source;
    }

    public String getName() {
        return name;
    }
}
