package com.cn.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * 轮播图实体
 *
 * @author chen
 * @date 2017-12-30 17:41
 */
@Setter
@Getter
@Entity
@Table(name = "carousel")
public class Carousel extends AbstractUserDateAudit {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    /**图片地址*/
    @Column(nullable = false)
    private String imageUrl;

    /**图片提示*/
    @Column(length = 20)
    private String imageTip;

    /**跳转地址*/
    @Column(nullable = false)
    private String forwardUrl;

    /**排序*/
    private Integer sort;

}
