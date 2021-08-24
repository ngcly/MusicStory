package com.cn.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * 文章表实体
 * @author ngcly
 * @since 2017-12-30 17:39
 */
@Setter
@Getter
@Entity
@Table(name = "essay")
@JsonIgnoreProperties(value = {"handler","hibernateLazyInitializer","fieldHandler"})
public class Essay extends AbstractDateAudit {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    /**文章标题*/
    @Column(nullable = false,length = 80)
    private String title;

    /**简介*/
    @Column(nullable = false)
    private String synopsis;

    /**文章内容*/
    @Lob @Basic(fetch = FetchType.LAZY)
    @Column(columnDefinition="mediumtext NOT NULL")
    private String content;

    /**阅览数*/
    @Column(columnDefinition = "int default 0",nullable = false)
    private Integer readNum;

    /**0-草稿 1-待审核 2-审核不通过 3-正常 4-推荐*/
    @Column(nullable = false)
    private Byte state;

    /**审核不通过原由*/
    private String remark;

    /**文章所属用户*/
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId",nullable = false)
    private User user;

    /**文章所属分类*/
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classifyId",nullable = false)
    private Classify classify;

    /**文章音乐列表*/
    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)
    @JoinColumn(name = "essayId")
    private List<Music> musicList;

}
