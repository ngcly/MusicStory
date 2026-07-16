package com.cn.user.domain.repository;
 
import com.cn.enums.UserTypeEnum;
import com.cn.user.domain.Role;
import java.util.List;
 
/**
 * 角色仓储端口接口 (Repository Port)
 *
 * @author ngcly
 */
public interface RoleRepositoryPort {
    List<Role> getActiveRolesByType(UserTypeEnum type);
}
