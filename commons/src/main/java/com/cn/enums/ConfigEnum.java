package com.cn.enums;

/**
 * 配置相关 枚举
 * @author ngcly
 */
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

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
