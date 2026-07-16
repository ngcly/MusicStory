package com.cn.user.domain;
 
import lombok.Getter;
import lombok.Setter;
 
/**
 * 社交绑定信息领域模型 (Domain Model)
 *
 * @author ngcly
 */
@Getter
@Setter
public class SocialInfo {
    private Long id;
    private String uuid;
    private String source;
    private String accessToken;
    private Integer expireIn;
    private String refreshToken;
    private String openId;
    private String uid;
    private String unionId;
    private String scope;
    private User user;
}
