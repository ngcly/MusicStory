package com.cn.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * 音乐表实体
 *
 * @author chen
 * @date 2017-12-30 17:40
 */
@Getter
@Setter
@Entity
@Table(name = "music")
public class Music {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private String id;

    /**歌名*/
    @Column(nullable = false, length = 32)
    private String name;

    /**歌手*/
    @Column(length = 20)
    private String artist;

    /**专辑*/
    @Column(length = 32)
    private String album;

    /**封面*/
    private String cover;

    /**歌词*/
    private String lrc;

    /**文章id*/
    @Column(nullable = false)
    private Long essayId;

    /**链接地址*/
    @Column(nullable = false)
    private String url;

}
