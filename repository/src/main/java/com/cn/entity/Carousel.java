package com.cn.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * 轮播图实体
 *
 * @author chen
 * @date 2017-12-30 17:41
 */
@Data
@Entity
@Table(name = "carousel")
public class Carousel extends UserDateAudit{
    @Id
    @GeneratedValue(generator = "id")
    @GenericGenerator(name = "id", strategy = "uuid")
    @Column(name="id")
    private String id;
    private String categoryId;   //分类ID
    private String imageUrl;     //图片地址
    private String imageDesc;    //图片描述
    private String imageTip;     //图片提示
    private String forwardUrl;   //跳转地址

}
