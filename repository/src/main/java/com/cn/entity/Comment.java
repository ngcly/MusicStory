package com.cn.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 评论表实体
 *
 * @author chen
 * @date 2017-12-30 17:38
 */

@Entity
@Table(name = "comment")
public class Comment {
    @Id
    @GeneratedValue(generator = "id")
    @GenericGenerator(name = "id", strategy = "uuid")
    @Column(name="id")
    private String id;
    private String userId;   //用户ID
    private String essayId;  //文章ID
    private String content;  //内容
    private String replyUserId;    //回复用户ID
    private String replyCommentId; //回复评论ID

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEssayId() {
        return essayId;
    }

    public void setEssayId(String essayId) {
        this.essayId = essayId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getReplyUserId() {
        return replyUserId;
    }

    public void setReplyUserId(String replyUserId) {
        this.replyUserId = replyUserId;
    }

    public String getReplyCommentId() {
        return replyCommentId;
    }

    public void setReplyCommentId(String replyCommentId) {
        this.replyCommentId = replyCommentId;
    }
}
