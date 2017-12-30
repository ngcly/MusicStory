package com.cn.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * 公告表
 *
 * @author chen
 * @date 2017-12-30 17:36
 */

@Entity
@Table(name = "notice")
public class Notice {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="id")
    private Long id;
    private String content;   //公告内容
    private String managerId; //创建人ID
    private Date createTime;  //创建时间

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
