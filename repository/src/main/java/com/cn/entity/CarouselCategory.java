package com.cn.entity;

import com.cn.config.AbstractUserDateAudit;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.util.List;

/**
 * 轮播图分类实体
 * @author ngcly
 */
@Getter
@Setter
@Entity
@Table(name = "carousel_category")
@JsonIgnoreProperties(value = {"handler","hibernateLazyInitializer","fieldHandler"})
public class CarouselCategory extends AbstractUserDateAudit {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    /**标题名称*/
    @Column(nullable = false,length = 10)
    private String title;

    /**描述*/
    @Column(length = 32)
    private String remark;

    /**轮播图列表*/
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "categoryId")
    private List<Carousel> carousels;

}
