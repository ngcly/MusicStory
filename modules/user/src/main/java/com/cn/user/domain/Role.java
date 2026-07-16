package com.cn.user.domain;
 
import com.cn.enums.UserTypeEnum;
import lombok.Getter;
import lombok.Setter;
import java.util.Set;
 
/**
 * 角色领域模型 (Domain Model)
 *
 * @author ngcly
 */
@Getter
@Setter
public class Role {
    private Long id;
    private String roleName;
    private String roleCode;
    private UserTypeEnum roleType;
    private String description;
    private Boolean available;
    private Set<Permission> permissions;
}
