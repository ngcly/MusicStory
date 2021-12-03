package com.cn.entity;

import com.cn.config.AbstractUserDateAudit;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 公告表
 * @author ngcly
 * @since 2017-12-30 17:36
 */
@Getter
@Setter
@Entity
@Table(name = "notice")
public class Notice extends AbstractUserDateAudit {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    /**公告标题*/
    @Column(nullable = false, length = 60)
    private String title;

    /**公告内容*/
    @Column(nullable = false)
    private String content;

    /**公告类型 暂不用*/
    @Column(length = 8)
    private String noticeType;

    /**展示开始时间*/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime beginTime;

    /**展示结束时间*/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    @Transient
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime showTime;

}
