package com.cn.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * 登录日志表实体
 *
 * @author chen
 * @date 2018-01-02 16:52
 */
@Data
@Entity
@Table(name = "login_log")
public class LoginLog {
    @Id
    @GeneratedValue(generator = "id")
    @GenericGenerator(name = "id", strategy = "uuid")
    @Column(name="id")
    private String id;
    private String userId;    //登录用户IP
    private Byte userType;    //用户类型 1-后台管理员 2-前台会员
    private String loginIp;   //登录IP
    private String addressIp; //IP地址
    private Date loginTime;   //登录时间

}
