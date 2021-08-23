package com.cn.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 注册参数数据封装类
 * @author nglcy
 * @since 2018-08-05 13:50
 */
@Data
@ApiModel(value="注册", description = "注册参数")
public class SignUpDTO {

    @ApiModelProperty(value="用户名", required = true)
    @NotBlank
    @Size(min = 3, max = 15)
    private String username;

    @ApiModelProperty(value="邮箱", required = true)
    @NotBlank
    @Size(max = 40)
    @Email
    private String email;

    @ApiModelProperty(value="手机号")
    private String phone;

    @ApiModelProperty(value="密码", required = true)
    @NotBlank
    @Size(min = 6, max = 32)
    private String password;

}
