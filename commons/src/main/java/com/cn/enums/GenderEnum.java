package com.cn.enums;

/**
 * 性别 枚举
 * @author ngcly
 * @version V1.0
 * @since 2021/8/24 0:23
 */
public enum GenderEnum {
    MAN((byte) 1,"男"),WOMAN((byte) 2,"女");

    private final byte code;
    private final String gender;

    GenderEnum(byte code, String gender) {
        this.code = code;
        this.gender = gender;
    }

    public byte getCode() {
        return code;
    }

    public String getGender() {
        return gender;
    }
}
