package com.cn.enums;

import lombok.Getter;

/**
 * 配置相关 枚举
 * @author ngcly
 */
@Getter
public enum ConfigEnum {
    /**
     * 密钥属性
     */
    JASYPT_ENCRYPTOR("jasypt.encryptor.password","ngCly");

    private final String key;
    private final String value;

    ConfigEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

}
