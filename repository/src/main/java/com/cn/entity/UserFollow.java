package com.cn.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 会员关注关系表
 *
 * @author chen
 * @date 2017-12-30 17:21
 */
@Data
@Entity
@Table(name = "user_follow")
public class UserFollow extends AbstractDateAudit {
    @Id
    @GeneratedValue(generator = "id")
    @GenericGenerator(name = "id", strategy = "uuid")
    @Column(name="id")
    private String id;
    private String userId;    //关注者ID
    private String followId;  //被关注的人ID

}
