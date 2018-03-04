package com.cn.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 分类表实体
 *
 * @author chen
 * @date 2017-12-30 17:37
 */

@Entity
@Table(name = "classify")
public class Classify implements Serializable{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="id")
    private Long id;
    private String name;     //分类名称
    private String introduction; //分类说明
    private Date createTime; //创建时间
    private Date updateTime; //修改时间


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
