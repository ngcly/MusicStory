package com.cn.entity;

import com.cn.config.AbstractDateAudit;
import com.cn.enums.FaveTypeEnum;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;

/**
 * 用户点赞收藏表
 * @author chen
 * @date 2017-12-30 17:34
 */
@Setter
@Getter
@Entity
@Table(name = "user_faves")
public class UserFaves extends AbstractDateAudit {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    /**会员ID*/
    @Column(nullable = false)
    private Long userId;

    /**文章ID*/
    @Column(nullable = false)
    private Long essayId;

    /**类型*/
    @Column(nullable = false)
    private FaveTypeEnum faveType;
}
