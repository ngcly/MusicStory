package com.cn.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author chenning
 */
public enum ResourceEnum {
    @JsonProperty("menu")
    MENU,
    @JsonProperty("button")
    BUTTON
}
