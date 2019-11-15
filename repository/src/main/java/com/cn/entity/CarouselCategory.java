package com.cn.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * 轮播图分类实体
 */
@Data
@Entity
@Table(name = "carousel_category")
public class CarouselCategory extends AbstractUserDateAudit {
    @Id
    @Column(name="id")
    private String id;
    private String title;  //标题名称
    private String remark; //描述
    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "categoryId",nullable = false)
    private List<Carousel> carousels;
}
