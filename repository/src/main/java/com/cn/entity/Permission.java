package com.cn.entity;
import javax.persistence.*;
import java.io.Serializable;
import java.util.List;


/**
 * 后台资源实体类
 * Created by chen on 2017/6/23.
 */

@Entity
@Table(name="permission")
public class Permission implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="id")
    private Long id;//主键.
    private String name;//名称.
    @Column(columnDefinition="enum('menu','button')")
    private String resourceType;//资源类型，[menu|button]
    private String url;//资源路径.
    private String permission; //权限字符串,menu例子：role:*，button例子：role:create,role:update,role:delete,role:view
    private Long parentId; //父编号
    private String parentIds; //父编号列表
    private Boolean available = Boolean.FALSE;
    private Integer sort; //排序

    @ManyToMany
    @JoinTable(name="role_permission",joinColumns={@JoinColumn(name="permission_id")},inverseJoinColumns={@JoinColumn(name="role_id")})
    private List<Role> roles;


    public long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getResourceType() {
        return resourceType;
    }
    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }

    public String getPermission() {
        return permission;
    }
    public void setPermission(String permission) {
        this.permission = permission;
    }

    public Long getParentId() {
        return parentId;
    }
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getParentIds() {
        return parentIds;
    }
    public void setParentIds(String parentIds) {
        this.parentIds = parentIds;
    }

    public Boolean getAvailable() {
        return available;
    }
    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public List<Role> getRoles() {
        return roles;
    }
    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }


    @Override
    public String toString() {
        return "SysPermission [id=" + id + ", name=" + name + ", resourceType=" + resourceType + ", url=" + url
                + ", permission=" + permission + ", parentId=" + parentId + ", parentIds=" + parentIds + ", available="
                + available + ", roles=" + roles + "]";
    }

}
