package com.cn.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * 客服管理员实体
 *
 * @author chen
 * @date 2017-12-30 16:48
 */

@Entity
@Table(name="manager")
public class Manager implements Serializable {
    @Id
    @GeneratedValue(generator = "id")
    @GenericGenerator(name = "id", strategy = "uuid")
    @Column(name="id")
    private String id;
    private String username;  //用户名
    private String password;  //密码
    private Byte gender;      //性别
    private String realName;  //真实姓名
    private Date birthday;    //生日
    private Byte state;       //状态,0:创建未认证 1:正常状态,2：用户被锁定.
    @ManyToMany(fetch= FetchType.EAGER)//立即从数据库中进行加载数据;
    @JoinTable(name = "manager_role", joinColumns = { @JoinColumn(name = "manager_id") }, inverseJoinColumns ={@JoinColumn(name = "role_id") })
    private Set<Role> roleList;// 一个用户具有多个角色

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Byte getGender() {
        return gender;
    }

    public void setGender(Byte gender) {
        this.gender = gender;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Byte getState() {
        return state;
    }

    public void setState(Byte state) {
        this.state = state;
    }

    public Set<Role> getRoleList() {
        return roleList;
    }

    public void setRoleList(Set<Role> roleList) {
        this.roleList = roleList;
    }
}
