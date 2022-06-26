package com.cn.entity;

import com.cn.pojo.MenuDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;


/**
 * 后台资源实体类
 *
 * @author ngcly
 * @since 2017/6/23.
 */
@Setter
@Entity
@Table(name = "permission")
@JsonIgnoreProperties(value = {"handler", "hibernateLazyInitializer", "fieldHandler"})
public class Permission extends MenuDTO implements Serializable {

    /**
     * 资源类型，[menu|button]
     */
    private String resourceType;

    /**
     * 权限字符串,menu例子：role:*，button例子：role:create,role:update,role:delete,role:view
     */
    private String purview;

    /**
     * 父级id列表
     */
    private String parentIds;

    /**
     * 排序号
     */
    private Integer sort;

//    /**
//     * 菜单与角色 多对多
//     */
//    private List<Role> roles;

    /**
     * 资源类型
     */
    public static final String RESOURCE_MENU = "menu";
    public static final String RESOURCE_BUTTON = "button";

    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    @Override
    @Column(nullable = false, length = 12)
    public String getName() {
        return name;
    }

    @Override
    @Column(length = 32)
    public String getUrl() {
        return url;
    }

    @Override
    @Column(length = 50)
    public String getIcon() {
        return icon;
    }

    @Override
    @Column(nullable = false)
    public Long getParentId() {
        return parentId;
    }

    @Column(nullable = false, length = 10)
    public String getResourceType() {
        return resourceType;
    }

    @Column(length = 32)
    public String getPurview() {
        return purview;
    }

    @Column(nullable = false, length = 100)
    public String getParentIds() {
        return parentIds;
    }

    public Integer getSort() {
        return sort;
    }

//    @ManyToMany(mappedBy = "permissions")
//    public List<Role> getRoles() {
//        return roles;
//    }
}
