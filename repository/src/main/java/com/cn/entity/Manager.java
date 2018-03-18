package com.cn.entity;

import lombok.Data;
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
@Data
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
    private String avatar;    //头像
    private Date birthday;    //生日
    private Byte state;       //状态,0:创建未认证 1:正常状态,2：用户被锁定.
    @ManyToMany(fetch= FetchType.EAGER)//立即从数据库中进行加载数据;
    @JoinTable(name = "manager_role", joinColumns = { @JoinColumn(name = "manager_id") }, inverseJoinColumns ={@JoinColumn(name = "role_id") })
    private Set<Role> roleList;// 一个用户具有多个角色

}
