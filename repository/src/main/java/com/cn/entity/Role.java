package com.cn.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * 角色表
 * Created by chen on 2017/6/23.
 */
@Getter
@Setter
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class) //该注解是为了解决当前json序列化的bug 由于many to many会让json序列化产生无限循环 所以该注解能避免restful json产生的死循环
@Table(name="role",uniqueConstraints=@UniqueConstraint(columnNames={"roleCode","roleType"}))
public class Role implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="id")
    private Long id; // 编号
    private String roleName;    //角色名称
    private String roleCode;    //角色标识符 程序中使用
    private Byte roleType;      //角色类型  1-后台角色 2-前台角色
    private String description; //角色描述
    private Boolean available;  //是否可用,如果不可用将不会添加给用户

    // 用户 - 角色关系定义;
    @ManyToMany
    @JoinTable(name="user_role",joinColumns={@JoinColumn(name="role_id")},inverseJoinColumns={@JoinColumn(name="user_id")})
    private List<User> userInfoList;// 一个角色对应多个用户

    // 管理员 - 角色关系定义;
    @ManyToMany
    @JoinTable(name="manager_role",joinColumns={@JoinColumn(name="role_id")},inverseJoinColumns={@JoinColumn(name="manager_id")})
    // 一个角色对应多个用户
    private List<Manager> managers;

    //角色 -- 权限关系：多对多关系;
    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(name="role_permission",joinColumns={@JoinColumn(name="role_id")},inverseJoinColumns={@JoinColumn(name="permission_id")})
    @OrderBy("sort asc")
    private List<Permission> permissions;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
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
