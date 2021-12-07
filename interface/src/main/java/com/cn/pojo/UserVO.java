package com.cn.pojo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 用户展示数据封装类
 * @author ngcly
 */
@Data
@Schema(title="用户", description = "用户信息")
public class UserVO {
    @Schema(title = "用户名")
    private String username;
    @Schema(title = "昵称")
    private String nickName;
    @Schema(title = "生日")
    private LocalDate birthday;
    @Schema(title = "性别")
    private Byte gender ;
    @Schema(title = "地址")
    private String address;
    @Schema(title = "真实姓名")
    private String realName;
    @Schema(title = "个人简介")
    private String personDesc;
    @Schema(title = "个性签名")
    private String signature;
    @Schema(title = "头像")
    private String avatar;
    @Schema(title = "手机号")
    private String phone;
    @Schema(title = "邮箱地址")
    private String email;
    @Schema(title = "账户余额")
    private BigDecimal balance;
    @Schema(title = "等级")
    private Integer level;
    @Schema(title = "积分")
    private Integer credit;
    @Schema(title = "用户状态")
    private Byte state;

}
