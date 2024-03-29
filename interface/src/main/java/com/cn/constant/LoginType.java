package com.cn.constant;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author chenning
 */
public enum LoginType {
    @JsonProperty("username")
    USERNAME,
    @JsonProperty("mobile")
    MOBILE,
    @JsonProperty("wechat")
    WECHAT,
    @JsonProperty("qq")
    QQ;
}
