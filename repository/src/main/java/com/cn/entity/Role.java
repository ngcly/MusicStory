package com.cn.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * 角色表
 * Created by chen on 2017/6/23.
 */

@Data
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class,property = "@id") //该注解是为了解决当前json序列化的bug 由于many to many会让json序列化产生无限循环 所以该注解能避免restful json产生的死循环
@Table(name="role")
public class Role implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="id")
    private Long id; // 编号
    private String role; // 角色标识程序中判断使用,如"admin",这个是唯一的:
    private Byte roleType;     //角色类型  1-后台角色 2-前台角色
    private String description; // 角色描述,UI界面显示使用
    private Boolean available = Boolean.FALSE; // 是否可用,如果不可用将不会添加给用户

    // 用户 - 角色关系定义;
    @ManyToMany
    @JoinTable(name="user_role",joinColumns={@JoinColumn(name="role_id")},inverseJoinColumns={@JoinColumn(name="user_id")})
    private List<User> userInfos;// 一个角色对应多个用户

    // 管理员 - 角色关系定义;
    @ManyToMany
    @JoinTable(name="manager_role",joinColumns={@JoinColumn(name="role_id")},inverseJoinColumns={@JoinColumn(name="manager_id")})
    // 一个角色对应多个用户
    private List<Manager> managers;

    //角色 -- 权限关系：多对多关系;
    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(name="role_permission",joinColumns={@JoinColumn(name="role_id")},inverseJoinColumns={@JoinColumn(name="permission_id")})
    private List<Permission> permissions;

}
