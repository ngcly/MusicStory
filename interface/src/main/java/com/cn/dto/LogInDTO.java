package com.cn.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;

/**
 * 描述:
 * 登录数据封装类
 *
 * @author chen
 * @create 2018-08-05 13:42
 */

@ApiModel(value="登录参数", description = "登录需要以下参数")
public class LogInDTO {
    @ApiModelProperty(value="用户名", required = true)
    @NotBlank(message = "用户名不可为空")
    private String username;

    @ApiModelProperty(value="密码", required = true)
    @NotBlank(message = "密码不可为空")
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
