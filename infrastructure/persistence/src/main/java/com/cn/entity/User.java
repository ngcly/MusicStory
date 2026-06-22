package com.cn.entity;

import com.cn.config.AbstractDateAudit;
import com.cn.enums.GenderEnum;
import com.cn.enums.UserStatusEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 用户实体
 * 在单向关系中没有mappedBy,主控方相当于拥有指向另一方的外键的一方。
 * 1.一对一和多对一的@JoinColumn注解的都是在“主控方”，都是本表指向外表的外键名称。
 * 2.一对多的@JoinColumn注解在“被控方”，即一的一方，指的是外表中指向本表的外键名称。
 * 3.多对多中，joinColumns写的都是本表在中间表的外键名称，
 * inverseJoinColumns写的是另一个表在中间表的外键名称。
 *
 * @author ngcly
 * @version V1.0
 * @since 2017/6/23 11:01
 */
@Getter
@Setter
@Entity
@Table(name = "user_info", uniqueConstraints = @UniqueConstraint(columnNames = {"username", "email"}))
@JsonIgnoreProperties(value = {"handler", "hibernateLazyInitializer", "fieldHandler"})
@NamedEntityGraph(name = "UserRole.Graph", attributeNodes = {
        @NamedAttributeNode(value = "roleList", subgraph = "Permission.Graph")
},
        subgraphs = {@NamedSubgraph(name = "Permission.Graph", attributeNodes = {
                @NamedAttributeNode("permissions")
        })}
)
public class User extends AbstractDateAudit implements UserDetails, CredentialsContainer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户名
     */
    @Column(nullable = false, length = 32)
    private String username;

    /**
     * 密码
     */
    @JsonIgnore
    @Column(length = 120)
    private String password;

    /**
     * 昵称
     */
    @Column(nullable = false, length = 32)
    private String nickName;

    /**
     * 生日
     */
    private LocalDate birthday;

    /**
     * 性别
     */
    @Enumerated(EnumType.STRING)
    private GenderEnum gender;

    /**
     * 地址
     */
    @Column(length = 100)
    private String address;

    /**
     * 真实姓名
     */
    @Column(length = 32)
    private String realName;

    /**
     * 个人简介
     */
    private String personDesc;

    /**
     * 个性签名
     */
    private String signature;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 手机号
     */
    @Column(length = 13)
    private String phone;

    /**
     * 邮箱地址
     */
    @Column(length = 32)
    private String email;

    /**
     * 账户余额
     */
    private BigDecimal balance;

    /**
     * 等级
     */
    private Integer level;

    /**
     * 积分
     */
    private Integer credit;

    /**
     * 用户状态
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatusEnum state;

    /**
     * 密码修改时间
     */
    private Instant pwdAlt;

    /**
     * 一个用户具有多个角色
     * 立即从数据库中进行加载数据;
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @SQLRestriction("available=true")
    private Set<Role> roleList;

    @Transient
    private Long[] roleIds;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.getRoleList().stream().map(role ->
                new SimpleGrantedAuthority(role.getRoleCode())).collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return getState() != UserStatusEnum.INITIALIZE;
    }

    @Override
    public void eraseCredentials() {
        this.password = null;
    }
}
