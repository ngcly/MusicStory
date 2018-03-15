package com.cn.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * 文章表实体
 *
 * @author chen
 * @date 2017-12-30 17:39
 */
@Data
@Entity
@Table(name = "essay")
public class Essay {
    @Id
    @GeneratedValue(generator = "id")
    @GenericGenerator(name = "id", strategy = "uuid")
    @Column(name="id")
    private String id;
    private String title;      //文章标题
    private String content;    //文章内容
    private String classifyId; //分类ID
    private Date createTime;   //创建时间
    private Date updateTime;   //修改时间
    private Integer readNum;   //阅览数

}
