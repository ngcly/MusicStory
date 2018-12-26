package com.cn.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 分类表实体
 *
 * @author chen
 * @date 2017-12-30 17:37
 */
@Data
@Entity
@Table(name = "classify")
public class Classify extends UserDateAudit implements Serializable{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="id")
    private Long id;
    private String name;     //分类名称
    private String introduction; //分类说明

}
