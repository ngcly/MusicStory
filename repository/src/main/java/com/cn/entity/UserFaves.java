package com.cn.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 用户点赞收藏表
 *
 * @author chen
 * @date 2017-12-30 17:34
 */
@Data
@Entity
@Table(name = "user_faves")
public class UserFaves extends AbstractDateAudit {
    public final static byte 点赞 = '1';
    public final static byte 收藏 = '2';

    @Id
    @GeneratedValue(generator = "id")
    @GenericGenerator(name = "id", strategy = "uuid")
    @Column(name="id")
    private String id;
    private String userId;   //会员ID
    private String essayId;  //文章ID
    private Byte faveType;   //类型 1-点赞 2-收藏

}
