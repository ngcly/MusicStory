package com.cn.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 音乐表实体
 *
 * @author chen
 * @date 2017-12-30 17:40
 */
@Data
@Entity
@Table(name = "music")
public class Music {
    @Id
    @GeneratedValue(generator = "id")
    @GenericGenerator(name = "id", strategy = "uuid")
    @Column(name="id")
    private String id;
    private String name;   //歌名
    private String artist; //歌手
    private String album;  //专辑
    private String cover;  //封面
    private String lrc;    //歌词
    private String essayId;//文章ID
    private String linkUrl;//链接地址

}
