package com.cn.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 登录日志表实体
 *
 * @author chen
 * @date 2018-01-02 16:52
 */
@Getter
@Setter
@Entity
@Table(name = "login_log")
public class LoginLog {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    /**登录用户ID*/
    @Column(nullable = false)
    private Long userId;

    /**登录用户名*/
    @Column(nullable = false, length = 32)
    private String userName;

    /**用户类型 1-后台管理员 2-前台会员*/
    @Column(nullable = false)
    private Byte userType;

    /**登录IP*/
    @Column(length = 20)
    private String loginIp;

    /**登录地址*/
    @Column(length = 32)
    private String loginAddress;

    /**登录浏览器*/
    @Column(length = 32)
    private String loginBrowser;

    /**登录时间*/
    @Column(nullable = false)
    private LocalDateTime loginTime;

    @Transient
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime beginTime;
    @Transient
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    /**用户类型*/
    public static final byte USER_TYPE_MANAGER = 1;
    public static final byte USER_TYPE_CUSTOMER = 2;

}
