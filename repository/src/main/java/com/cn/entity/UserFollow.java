package com.cn.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * 用户关注关系表
 * @author ngcly
 * @since 2017-12-30 17:21
 */
@Getter
@Setter
@Entity
@Table(name = "user_follow")
public class UserFollow extends AbstractDateAudit {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    /**关注者ID*/
    @Column(nullable = false)
    private Long userId;

    /**被关注的人ID*/
    @Column(nullable = false)
    private Long followId;

}
