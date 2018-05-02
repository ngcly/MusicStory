package com.cn.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * 消息表实体
 *
 * @author chen
 * @date 2017-12-30 17:42
 */
@Data
@Entity
@Table(name = "news")
public class News {
    @Id
    @GeneratedValue(generator = "id")
    @GenericGenerator(name = "id", strategy = "uuid")
    @Column(name="id")
    private String id;
    private String userId;   //用户ID
    private String content;  //消息内容
    private Boolean sended;  //是否已发送
    private Date createTime; //创建时间
    private Date sendTime;   //发送时间
    private String senderId; //发送人ID

}
