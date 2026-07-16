package com.cn.user.domain;
 
import com.cn.enums.GenderEnum;
import com.cn.enums.UserStatusEnum;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Set;
 
/**
 * 用户领域模型 (Domain Model)
 *
 * @author ngcly
 */
@Getter
@Setter
public class User {
    private Long id;
    private String username;
    private String password;
    private String nickName;
    private LocalDate birthday;
    private GenderEnum gender;
    private String address;
    private String realName;
    private String personDesc;
    private String signature;
    private String avatar;
    private String phone;
    private String email;
    private BigDecimal balance;
    private Integer level;
    private Integer credit;
    private UserStatusEnum state;
    private Instant pwdAlt;
    private Set<Role> roleList;
    private Long[] roleIds;
    private Instant createdAt;
    private Instant updatedAt;
}
