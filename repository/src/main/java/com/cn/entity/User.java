package com.cn.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

/**
 * Created by chen on 2017/6/23.
 * 会员账户实体
 * 在单向关系中没有mappedBy,主控方相当于拥有指向另一方的外键的一方。
 * 1.一对一和多对一的@JoinColumn注解的都是在“主控方”，都是本表指向外表的外键名称。
 * 2.一对多的@JoinColumn注解在“被控方”，即一的一方，指的是外表中指向本表的外键名称。
 * 3.多对多中，joinColumns写的都是本表在中间表的外键名称，
 * inverseJoinColumns写的是另一个表在中间表的外键名称。
 */
@Data
@Entity
@Table(name="user",uniqueConstraints = {@UniqueConstraint(columnNames="username")})
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "id")
    @GenericGenerator(name = "id", strategy = "uuid")
    @Column(name="id")
    private String id ;
    @Column(name="username",unique = true)
    private String username;       //用户名
    private String password;       //密码
    private String nickName;       //昵称
    private Date birthday;         //生日
    private Byte gender ;          //性别
    private String address;        //地址
    private String realName;       //真实姓名
    private String personDesc;     //个人简介
    private String signature;      //个性签名
    private String avatar;         //头像
    private String phone;          //手机号
    private String email;          //邮箱地址
    private BigDecimal balance;    //账户余额
    private Integer level;         //等级
    private Integer credit;        //积分
    private Date createTime;       //创建时间
    private Date updateTime;       //最后更新时间
    private Date lastLogin;        //最后登录时间
    private Byte state;//用户状态,0:不可用 1:正常状态,2：异常.

    @ManyToMany(fetch= FetchType.EAGER)//立即从数据库中进行加载数据;
    @JoinTable(name = "user_role", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns ={@JoinColumn(name = "role_id") })
    private Set<Role> roleList;// 一个用户具有多个角色

}

