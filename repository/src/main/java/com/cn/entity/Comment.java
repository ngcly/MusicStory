package com.cn.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 评论表实体
 *
 * @author chen
 * @date 2017-12-30 17:38
 */
@Data
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

}
