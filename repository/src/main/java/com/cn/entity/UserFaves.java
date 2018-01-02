package com.cn.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * 用户点赞收藏表
 *
 * @author chen
 * @date 2017-12-30 17:34
 */

@Entity
@Table(name = "user_faves")
public class UserFaves {
    private final static byte 点赞 = '1';
    private final static byte 收藏 = '2';

    @Id
    @GeneratedValue(generator = "id")
    @GenericGenerator(name = "id", strategy = "uuid")
    @Column(name="id")
    private String id;
    private String userId;   //会员ID
    private String essayId;  //文章ID
    private Date createTime; //创建时间
    private Byte faveType;   //类型 1-点赞 2-收藏

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

    public Byte getFaveType() {
        return faveType;
    }

    public void setFaveType(Byte faveType) {
        this.faveType = faveType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
