package com.cn.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 社会化用户实体
 * @author ngcly
 * @version V1.0
 * @since 2021/8/23 11:20
 */
@Getter
@Setter
@Entity
@Table(name = "social_user")
public class SocialUser implements Serializable {
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
    private Long expireIn;

    /**刷新令牌*/
    @Column(length = 50)
    private String refreshToken;

    /**第三方用户的 open id*/
    @Column(length = 50)
    private String openId;

    /**第三方用户的 ID*/
    @Column(length = 50)
    private String uid;

    /**个别平台的授权信息*/
    @Column(length = 50)
    private String accessCode;

    /**第三方用户的 union id*/
    @Column(length = 50)
    private String unionId;

    /**第三方用户授予的权限*/
    @Column(length = 50)
    private String scope;

    /**个别平台的授权信息*/
    @Column(length = 50)
    private String tokenType;

    /**id_token*/
    @Column(length = 50)
    private String idToken;

    /**小米平台用户的附带属性*/
    @Column(length = 50)
    private String macAlgorithm;

    /**小米平台用户的附带属性*/
    @Column(length = 50)
    private String macKey;

    /**用户的授权code*/
    @Column(length = 50)
    private String code;

    /**Twitter平台用户的附带属性*/
    @Column(length = 50)
    private String oauthToken;

    /**Twitter平台用户的附带属性*/
    @Column(length = 50)
    private String oauthTokenSecret;

}
