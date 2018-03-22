package com.cn;

import com.cn.dao.RoleRepository;
import com.cn.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * 角色权限service
 *
 * @author chen
 * @date 2018-03-21 18:07
 */
@Service
public class RoleService {
    @Autowired
    RoleRepository roleRepository;

    /**
     * 根据条件查询角色列表
     */
    public Page<Role> getRoleList(Pageable pageable,Role role){
        return roleRepository.findAll(RoleRepository.getRoleList(role.getRoleName(),role.getAvailable(),role.getRoleType()),pageable);
    }
}
