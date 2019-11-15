package com.cn.entity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;


/**
 * 后台资源实体类
 * Created by chen on 2017/6/23.
 */
@Getter
@Setter
@Entity
@Table(name="permission")
public class Permission implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="id")
    private Long id;//主键.
    private String name;//名称.
    @Column(columnDefinition="enum('menu','button')")
    private String resourceType;//资源类型，[menu|button]
    private String url;//资源路径.
    private String purview; //权限字符串,menu例子：role:*，button例子：role:create,role:update,role:delete,role:view
    private String icon;
    private Long parentId; //父编号
    private String parentIds; //父编号列表
    private Integer sort; //排序

    @ManyToMany
    @JoinTable(name="role_permission",joinColumns={@JoinColumn(name="permission_id")},inverseJoinColumns={@JoinColumn(name="role_id")})
    private List<Role> roles;

}
