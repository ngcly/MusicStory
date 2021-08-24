package com.cn.entity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;


/**
 * 后台资源实体类
 * @author ngcly
 * @since 2017/6/23.
 */
@Getter
@Setter
@Entity
@Table(name="permission")
@JsonIgnoreProperties(value = {"handler","hibernateLazyInitializer","fieldHandler"})
public class Permission implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    /** 名称 */
    @Column(nullable = false, length = 12)
    private String name;

    /** 资源类型，[menu|button] */
    @Column(nullable = false, length = 10)
    private String resourceType;

    /** 资源路径 */
    @Column(length = 32)
    private String url;

    /** 权限字符串,menu例子：role:*，button例子：role:create,role:update,role:delete,role:view */
    @Column(length = 32)
    private String purview;

    /** 菜单图标 */
    @Column(length = 50)
    private String icon;

    /** 父id */
    @Column(nullable = false)
    private Long parentId;

    /** 父级id列表 */
    @Column(nullable = false,length = 100)
    private String parentIds;

    /** 排序号 */
    private Integer sort;

    /** 菜单与角色 多对多 */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name="role_permission",joinColumns={@JoinColumn(name="permissionId")},inverseJoinColumns={@JoinColumn(name="roleId")})
    private List<Role> roles;

    /**资源类型*/
    public static final String RESOURCE_MENU = "menu";
    public static final String RESOURCE_BUTTON = "button";

}
