package com.cn.pojo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author ngcly
 */
@Getter
@Setter
public class UserVO {
    private String id ;
    private String username;       //用户名
    private String nickName;       //昵称
    private Date birthday;         //生日
    private Byte gender ;          //性别
    private String address;        //地址
    private String realName;       //真实姓名
    private String personDesc;     //个人简介
    private String signature;      //个性签名
    private String avatar;         //头像
    private String phone;          //手机号
    private String email;          //邮箱地址
    private BigDecimal balance;    //账户余额
    private Integer level;         //等级
    private Integer credit;        //积分
    private Byte state;//用户状态,0:不可用 1:正常状态,2:异常.

}
