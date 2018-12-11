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
    private String username;

    @NotBlank
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
