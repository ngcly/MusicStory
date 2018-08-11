package com.cn.dto;

import javax.validation.constraints.NotBlank;

/**
 * 描述:
 * 登录数据封装类
 *
 * @author chen
 * @create 2018-08-05 13:42
 */
public class LogInDTO {
    @NotBlank
    private String usernameOrEmail;

    @NotBlank
    private String password;

    public String getUsernameOrEmail() {
        return usernameOrEmail;
    }

    public void setUsernameOrEmail(String usernameOrEmail) {
        this.usernameOrEmail = usernameOrEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
