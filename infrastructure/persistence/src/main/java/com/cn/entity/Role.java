package com.cn.entity;

import com.cn.config.AbstractUserDateAudit;
import com.cn.enums.UserTypeEnum;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import java.util.Objects;
import java.util.Set;

/**
 * 角色实体
 *
 * @author ngcly
 * @date 2017/6/23
 */
@Setter
@Getter
@Entity
@Table(name = "role", uniqueConstraints = @UniqueConstraint(columnNames = {"roleCode", "roleType"}))
@JsonIgnoreProperties(value = {"userInfoList", "managers", "handler", "hibernateLazyInitializer", "fieldHandler"})
public class Role extends AbstractUserDateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 角色名称
     */
    @Column(nullable = false, length = 16)
    private String roleName;

    /**
     * 角色标识符
     */
    @Column(nullable = false, length = 32)
    private String roleCode;

    /**
     * 角色类型
     */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserTypeEnum roleType;

    /**
     * 角色描述
     */
    @Column(length = 50)
    private String description;
    /**
     * 是否可用,如果不可用将不会添加给用户
     */

    @Column(nullable = false)
    private Boolean available;

    /**
     * 用户 - 角色关系定义; 用户-角色 多对多
     */
    @ManyToMany(mappedBy = "roleList")
    private Set<User> userInfoList;

    /**
     * 管理员 - 角色关系定义; 管理员-角色 多对多
     */
    @ManyToMany(mappedBy = "roleList")
    private Set<Manager> managers;

    /**
     * 角色 -- 权限关系：多对多关系;
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "role_permission",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    @OrderBy("sort asc")
    private Set<Permission> permissions;


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Role role = (Role) o;
        return Objects.equals(id, role.id) &&
                Objects.equals(roleName, role.roleName) &&
                Objects.equals(roleType, role.roleType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, roleName, roleType);
    }

}
