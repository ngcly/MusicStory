package com.cn.entity;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import java.io.Serializable;

/**
 * 社会化信息实体
 * @author ngcly
 * @version V1.0
 * @since 2021/8/23 11:20
 */
@Getter
@Setter
@Entity
@Table(name = "social_info")
public class SocialInfo implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    /**第三方系统的唯一ID*/
    @Column(nullable = false,length = 32)
    private String uuid;

    /**第三方用户来源*/
    @Column(nullable = false,length = 15)
    private String source;

    /**用户的授权令牌*/
    @Column(nullable = false,length = 50)
    private String accessToken;

    /**第三方用户的授权令牌的有效期*/
    private Integer expireIn;

    /**刷新令牌*/
    @Column(length = 50)
    private String refreshToken;

    /**第三方用户的 open id*/
    @Column(length = 50)
    private String openId;

    /**第三方用户的 ID*/
    @Column(length = 50)
    private String uid;

    /**第三方用户的 union id*/
    @Column(length = 50)
    private String unionId;

    /**第三方用户授予的权限*/
    @Column(length = 50)
    private String scope;

    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.REMOVE)
    @JoinColumn(name = "userId")
    private User user;

}
