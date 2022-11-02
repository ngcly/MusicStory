package com.cn.pojo;

import com.cn.constant.LoginType;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 登录数据封装类
 * @author ngcly
 * @since 2018-08-05 13:42
 */
@Getter
@Schema(title="登录参数", description = "登录需要以下参数",
        discriminatorProperty = "loginType", subTypes = {
        LogInDTO.UserNameLoginDTO.class,
        LogInDTO.SocialLoginDTO.class})
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "loginType", include = JsonTypeInfo.As.EXISTING_PROPERTY, visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = LogInDTO.UserNameLoginDTO.class, name = "username"),
        @JsonSubTypes.Type(value = LogInDTO.SocialLoginDTO.class, names = {"qq", "wechat"})
})
public abstract class LogInDTO {
    @Schema(title="登录类型", required = true)
    @NotNull(message = "登录类型不可为空")
    @Setter(AccessLevel.PROTECTED)
    private LoginType loginType;

    @Schema
    @Getter
    @Setter
    public static class UserNameLoginDTO extends LogInDTO {
        @Schema(title="用户名", required = true)
        @NotBlank(message = "用户名不可为空")
        private String username;

        @Schema(title="密码", required = true)
        @NotBlank(message = "密码不可为空")
        private String password;
    }

    @Schema
    @Getter
    @Setter
    public static class SocialLoginDTO extends LogInDTO {
        @Schema(title="授权码", required = true)
        @NotBlank(message = "授权码不可为空")
        private String code;
        @Schema(title="csrf值", required = true)
        @NotBlank(message = "csrf值不可为空")
        private String state;
    }
}
