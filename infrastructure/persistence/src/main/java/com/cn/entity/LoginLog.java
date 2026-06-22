package com.cn.entity;

import com.cn.enums.LoginStatusEnum;
import com.cn.enums.UserTypeEnum;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.*;

import java.time.Instant;
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
    /**登录用户名*/
    @Column(nullable = false, length = 32)
    private String userName;

    /**用户类型*/
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserTypeEnum userType;

    /**登录IP*/
    @Column(length = 20)
    private String loginIp;

    /**登录地址*/
    @Column(length = 50)
    private String loginAddress;

    /**登录浏览器*/
    @Column(length = 32)
    private String loginBrowser;

    /**登录时间*/
    @Column(nullable = false)
    private Instant loginTime;

    /**登录状态*/
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private LoginStatusEnum loginStatus;

    @Transient
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Instant beginTime;
    @Transient
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Instant endTime;

}
