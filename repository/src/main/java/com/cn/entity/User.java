package com.cn.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

/**
 *
 * @author ngcly
 * @date 2017/6/23
 * 会员账户实体
 * 在单向关系中没有mappedBy,主控方相当于拥有指向另一方的外键的一方。
 * 1.一对一和多对一的@JoinColumn注解的都是在“主控方”，都是本表指向外表的外键名称。
 * 2.一对多的@JoinColumn注解在“被控方”，即一的一方，指的是外表中指向本表的外键名称。
 * 3.多对多中，joinColumns写的都是本表在中间表的外键名称，
 * inverseJoinColumns写的是另一个表在中间表的外键名称。
 */
@Getter
@Setter
@Entity
@JsonIgnoreProperties(value = {"handler","hibernateLazyInitializer","fieldHandler"})
@Table(name="user", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "username"
        }),
        @UniqueConstraint(columnNames = {
                "email"
        }),
        @UniqueConstraint(columnNames = {
                "unionId"
        })
})
public class User extends AbstractDateAudit implements UserDetails {
    @Id
    @GeneratedValue(generator = "id")
    @GenericGenerator(name = "id", strategy = "uuid")
    @Column(name="id")
    private String id ;
    @Column(name="username",unique = true)
    /**用户名*/
    private String username;
    /**密码*/
    @JsonIgnore
    private String password;
    /**昵称*/
    private String nickName;
    /**生日*/
    private Date birthday;
    /**性别*/
    private Byte gender ;
    /**地址*/
    private String address;
    /**真实姓名*/
    private String realName;
    /**个人简介*/
    private String personDesc;
    /**个性签名*/
    private String signature;
    /**头像*/
    private String avatar;
    /**手机号*/
    private String phone;
    /**邮箱地址*/
    private String email;
    /**账户余额*/
    private BigDecimal balance;
    /**等级*/
    private Integer level;
    /**积分*/
    private Integer credit;
    /**用户状态,0:未激活 1:正常状态,2:异常.*/
    private Byte state;
    /**
     * 重置密码时间
     */
    /**重置密码时间*/
    private LocalDateTime pwdReset;

    /**立即从数据库中进行加载数据;*/
    @ManyToMany(fetch= FetchType.EAGER)
    @JoinTable(name = "user_role", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns ={@JoinColumn(name = "role_id") })
    @Where(clause = "available=true")
    /**一个用户具有多个角色*/
    private Set<Role> roleList;

    @Transient
    private String[] roleIds;

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }
}

