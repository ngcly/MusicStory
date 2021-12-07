package com.cn.pojo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 登录数据封装类
 * @author ngcly
 * @since 2018-08-05 13:42
 */
@Data
@Schema(title="登录参数", description = "登录需要以下参数")
public class LogInDTO {

    @Schema(title="用户名", required = true)
    @NotBlank(message = "用户名不可为空")
    private String username;

    @Schema(title="密码", required = true)
    @NotBlank(message = "密码不可为空")
    private String password;

}
