package com.cn.entity;

import com.cn.enums.ResourceEnum;
import com.cn.pojo.MenuDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Setter;

import jakarta.persistence.*;
import java.util.Set;

/**
 * 后台资源实体类
 *
 * @author ngcly
 */
@Setter
@Entity
@Table(name = "permission")
@JsonIgnoreProperties(value = {"roles", "handler", "hibernateLazyInitializer", "fieldHandler"})
public class Permission extends MenuDTO {

    /**
     * 资源类型
     */
    private ResourceEnum resourceType;

    /**
     * 请求方式
     */
    private String httpMethod;

    /**
     * 父级id列表
     */
    private String parentIds;

    /**
     * 排序号
     */
    private Integer sort;

    /**
     * 菜单与角色 多对多
     */
    private Set<Role> roles;

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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    public ResourceEnum getResourceType() {
        return resourceType;
    }

    @Column(length = 32)
    public String getHttpMethod() {
        return httpMethod;
    }

    @Column(nullable = false, length = 100)
    public String getParentIds() {
        return parentIds;
    }

    public Integer getSort() {
        return sort;
    }

    @ManyToMany(mappedBy = "permissions")
    public Set<Role> getRoles() {
        return roles;
    }

}
