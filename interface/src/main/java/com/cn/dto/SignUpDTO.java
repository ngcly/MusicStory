package com.cn.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 描述:
 *
 * @author chen
 * @create 2018-08-05 13:50
 */
@ApiModel(value="注册", description = "注册参数")
public class SignUpDTO {
    @ApiModelProperty(value="真实姓名", required = true)
    @NotBlank
    @Size(min = 4, max = 40)
    private String realName;

    @ApiModelProperty(value="用户名", required = true)
    @NotBlank
    @Size(min = 3, max = 15)
    private String username;

    @ApiModelProperty(value="邮箱", required = true)
    @NotBlank
    @Size(max = 40)
    @Email
    private String email;

    @ApiModelProperty(value="密码", required = true)
    @NotBlank
    @Size(min = 6, max = 20)
    private String password;

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
