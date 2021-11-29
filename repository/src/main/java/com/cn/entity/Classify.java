package com.cn.entity;

import com.cn.config.AbstractUserDateAudit;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * 分类表实体
 *
 * @author chen
 * @date 2017-12-30 17:37
 */
@Getter
@Setter
@Entity
@Table(name = "classify")
@JsonIgnoreProperties(value = {"handler","hibernateLazyInitializer","fieldHandler"})
public class Classify extends AbstractUserDateAudit {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    /**分类名称*/
    @Column(nullable = false, length = 8)
    private String name;

    /**分类说明*/
    @Column(length = 100)
    private String introduction;

}
