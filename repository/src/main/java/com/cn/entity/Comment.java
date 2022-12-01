package com.cn.entity;

import com.cn.config.AbstractDateAudit;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;

/**
 * 评论表实体
 *
 * @author chen
 * @date 2017-12-30 17:38
 */
@Setter
@Getter
@Entity
@Table(name = "comment")
public class Comment extends AbstractDateAudit {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    /**用户id*/
    @Column(nullable = false)
    private Long userId;

    /**文章id*/
    @Column(nullable = false)
    private Long essayId;

    /**内容*/
    @Column(nullable = false)
    private String content;

    /**回复用户ID*/
    private Long replyUserId;

    /**回复评论ID*/
    private Long replyCommentId;

}
