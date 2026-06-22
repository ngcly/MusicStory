package com.cn.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author chenning
 */
public enum LoginStatusEnum {
    @JsonProperty("success")
    SUCCESS,
    @JsonProperty("failure")
    FAILURE
}
