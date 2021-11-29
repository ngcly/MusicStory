package com.cn.entity;

import com.cn.config.AbstractDateAudit;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author ngcly
 */
@Getter
@Setter
@Entity
@Table(name = "daily")
public class Daily extends AbstractDateAudit {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

}
