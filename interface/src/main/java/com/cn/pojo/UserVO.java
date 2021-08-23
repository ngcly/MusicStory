package com.cn.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 用户展示数据封装类
 * @author ngcly
 */
@Data
@ApiModel(value="注册", description = "注册参数")
public class UserVO {
    @ApiModelProperty("用户名")
    private String username;
    @ApiModelProperty("昵称")
    private String nickName;
    @ApiModelProperty("生日")
    private LocalDate birthday;
    @ApiModelProperty("性别")
    private Byte gender ;
    @ApiModelProperty("地址")
    private String address;
    @ApiModelProperty("真实姓名")
    private String realName;
    @ApiModelProperty("个人简介")
    private String personDesc;
    @ApiModelProperty("个性签名")
    private String signature;
    @ApiModelProperty("头像")
    private String avatar;
    @ApiModelProperty("手机号")
    private String phone;
    @ApiModelProperty("邮箱地址")
    private String email;
    @ApiModelProperty("账户余额")
    private BigDecimal balance;
    @ApiModelProperty("等级")
    private Integer level;
    @ApiModelProperty("积分")
    private Integer credit;
    @ApiModelProperty("用户状态")
    private Byte state;

}
