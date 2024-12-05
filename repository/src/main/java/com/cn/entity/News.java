package com.cn.entity;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;

import java.time.Instant;
import java.time.LocalDateTime;

/**
 * 消息表实体
 *
 * @author chen
 * @date 2017-12-30 17:42
 */
@Getter
@Setter
@Entity
@Table(name = "news")
public class News {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    /** 用户id */
    @Column(nullable = false)
    private Long userId;

    /** 发送人id */
    @Column(nullable = false)
    private Long senderId;

    /** 消息内容 */
    @Column(nullable = false)
    private String content;

    /** 是否已发送 */
    @Column(nullable = false)
    private Boolean sent;

    /** 创建时间 */
    @Column(nullable = false)
    private Instant createTime;

    /** 发送时间 */
    @Column(nullable = false)
    private Instant sendTime;

}
