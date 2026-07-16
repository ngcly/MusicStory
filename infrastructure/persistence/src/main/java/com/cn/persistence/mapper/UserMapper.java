package com.cn.persistence.mapper;
 
import com.cn.user.domain.User;
import com.cn.user.domain.SocialInfo;
import com.cn.user.domain.Role;
import com.cn.user.domain.Permission;
import org.springframework.beans.BeanUtils;
import java.util.stream.Collectors;
 
/**
 * 领域模型与 JPA 实体之间的映射转换器 (Mapper)
 * 避免使用通配符导入导致类名冲突
 *
 * @author ngcly
 */
public class UserMapper {
 
    public static User toDomain(com.cn.entity.User entity) {
        if (entity == null) {
            return null;
        }
        User domain = new User();
        BeanUtils.copyProperties(entity, domain);
        if (entity.getRoleList() != null) {
            domain.setRoleList(entity.getRoleList().stream()
                    .map(UserMapper::toDomain)
                    .collect(Collectors.toSet()));
        }
        return domain;
    }
 
    public static com.cn.entity.User toEntity(User domain) {
        if (domain == null) {
            return null;
        }
        com.cn.entity.User entity = new com.cn.entity.User();
        BeanUtils.copyProperties(domain, entity);
        if (domain.getRoleList() != null) {
            entity.setRoleList(domain.getRoleList().stream()
                    .map(UserMapper::toEntity)
                    .collect(Collectors.toSet()));
        }
        return entity;
    }
 
    public static SocialInfo toDomain(com.cn.entity.SocialInfo entity) {
        if (entity == null) {
            return null;
        }
        SocialInfo domain = new SocialInfo();
        BeanUtils.copyProperties(entity, domain, "user");
        if (entity.getUser() != null) {
            domain.setUser(toDomain(entity.getUser()));
        }
        return domain;
    }
 
    public static com.cn.entity.SocialInfo toEntity(SocialInfo domain) {
        if (domain == null) {
            return null;
        }
        com.cn.entity.SocialInfo entity = new com.cn.entity.SocialInfo();
        BeanUtils.copyProperties(domain, entity, "user");
        if (domain.getUser() != null) {
            entity.setUser(toEntity(domain.getUser()));
        }
        return entity;
    }
 
    public static Role toDomain(com.cn.entity.Role entity) {
        if (entity == null) {
            return null;
        }
        Role domain = new Role();
        BeanUtils.copyProperties(entity, domain, "permissions");
        if (entity.getPermissions() != null) {
            domain.setPermissions(entity.getPermissions().stream()
                    .map(UserMapper::toDomain)
                    .collect(Collectors.toSet()));
        }
        return domain;
    }
 
    public static com.cn.entity.Role toEntity(Role domain) {
        if (domain == null) {
            return null;
        }
        com.cn.entity.Role entity = new com.cn.entity.Role();
        BeanUtils.copyProperties(domain, entity, "permissions");
        if (domain.getPermissions() != null) {
            entity.setPermissions(domain.getPermissions().stream()
                    .map(UserMapper::toEntity)
                    .collect(Collectors.toSet()));
        }
        return entity;
    }
 
    public static Permission toDomain(com.cn.entity.Permission entity) {
        if (entity == null) {
            return null;
        }
        Permission domain = new Permission();
        BeanUtils.copyProperties(entity, domain);
        return domain;
    }
 
    public static com.cn.entity.Permission toEntity(Permission domain) {
        if (domain == null) {
            return null;
        }
        com.cn.entity.Permission entity = new com.cn.entity.Permission();
        BeanUtils.copyProperties(domain, entity);
        return entity;
    }
}
