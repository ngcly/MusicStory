package com.cn.pojo;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(title="注册", description = "注册参数")
public class SignUpDTO {

    @Schema(title="用户名", required = true)
    @NotBlank
    @Size(min = 3, max = 15)
    private String username;

    @Schema(title="邮箱", required = true)
    @NotBlank
    @Size(max = 40)
    @Email
    private String email;

    @Schema(title="手机号")
    private String phone;

    @Schema(title="密码", required = true)
    @NotBlank
    @Size(min = 6, max = 32)
    private String password;

}
