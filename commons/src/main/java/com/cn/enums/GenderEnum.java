package com.cn.enums;

/**
 * 性别 枚举
 * @author ngcly
 * @version V1.0
 * @since 2021/8/24 0:23
 */
public enum GenderEnum {
    MAN("男"),WOMAN("女");

    private final String gender;

    GenderEnum(String gender) {
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }
}
