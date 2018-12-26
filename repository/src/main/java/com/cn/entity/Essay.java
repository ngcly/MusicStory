package com.cn.entity;

import lombok.AllArgsConstructor;
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
@AllArgsConstructor
@Entity
@Table(name = "essay")
public class Essay extends DateAudit{
    @Id
    @GeneratedValue(generator = "id")
    @GenericGenerator(name = "id", strategy = "uuid")
    @Column(name="id")
    private String id;
    private String userId;     //作者
    private String title;      //文章标题
    private String content;    //文章内容
    private String classifyId; //分类ID
    private Integer readNum;   //阅览数

}
