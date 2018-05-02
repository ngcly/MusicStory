package com.cn;

import com.cn.dao.PermissionRepository;
import com.cn.dao.RoleRepository;
import com.cn.entity.Permission;
import com.cn.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

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
    @Autowired
    PermissionRepository permissionRepository;

    /**
     * 根据ID获取角色
     */
    public Role findRole(long roleId){
        return roleRepository.getOne(roleId);
    }

    /**
     * 根据条件查询角色列表
     */
    public Page<Role> getRoleList(Pageable pageable,Role role){
        return roleRepository.findAll(RoleRepository.getRoleList(role.getRoleName(),role.getAvailable(),role.getRoleType()),pageable);
    }

    /**
     * 获取所有角色
     */
    public List<Role> getAllRole(){
        return roleRepository.findAll();
    }

    /**
     * 获取可用角色
     */
    public Set<Role> getAvailableRoles(){
        return roleRepository.getAllByAvailableIsTrue();
    }

    /**
     * 保存角色
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveRole(Role role){
        if(role.getId()!=null){
            Role role1 = roleRepository.getOne(role.getId());
            role1.setRoleName(role.getRoleName());
            role1.setRoleCode(role.getRoleCode());
            role1.setRoleType(role.getRoleType());
            role1.setDescription(role.getDescription());
            role1.setAvailable(role.getAvailable());
        }else{
            roleRepository.save(role);
        }
    }

    /**
     * 保存授权
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveGrant(long roleId,String menuIds){
        Role role = roleRepository.getOne(roleId);
        List<Permission> permissions = permissionRepository.findMenuList();
        List<Permission> permissionList = new ArrayList<>();
        String[] permissionIds = menuIds.split(",");
        for(String permissionId:permissionIds){
            for(Permission permission:permissions){
                if(permissionId.equals(permission.getId().toString())){
                    permissionList.add(permission);
                }
            }
        }
        role.setPermissions(permissionList);
    }

    /**
     * 修改角色是否可用
     */
    @Transactional(rollbackFor = Exception.class)
    public void altAvailable(long roleId){
        Role role = roleRepository.getOne(roleId);
        if(role.getAvailable()){
            role.setAvailable(false);
        }else{
            role.setAvailable(true);
        }
    }

    /**
     * 删除角色
     */
    @Transactional(rollbackFor = Exception.class)
    public void delRole(long roleId){
        roleRepository.deleteById(roleId);
    }

    /**
     * 根据ID获取菜单
     */
    public Permission getPermissionById(long menuId){
        return permissionRepository.getOne(menuId);
    }

    /**
     * 获取菜单列表
     */
    public List<Permission> getPermissionList(){
        return permissionRepository.findMenuList();
    }

    /**
     * 保存菜单
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveMenu(Permission permission){
        Permission pms = new Permission();
        if(permission.getId()!=null){
            pms = permissionRepository.getOne(permission.getId());
        }else{
            if(permission.getParentId()!=null&&permission.getParentId()!=0){
                Permission parentPermission = permissionRepository.getOne(permission.getParentId());
                pms.setParentId(parentPermission.getId());
                pms.setParentIds(parentPermission.getParentIds()+"/"+parentPermission.getId());
            }else{
                pms.setParentId((long) 0);
                pms.setParentIds("0");
            }
        }
        pms.setName(permission.getName());
        pms.setUrl(permission.getUrl());
        pms.setResourceType(permission.getResourceType());
        pms.setSort(permission.getSort());
        pms.setPurview(permission.getPurview());
        pms.setIcon(permission.getIcon());
        permissionRepository.save(pms);
    }

    /**
     * 删除菜单
     */
    @Transactional(rollbackFor = Exception.class)
    public void delMenu(long menuId){
        Permission permission = permissionRepository.getOne(menuId);
        permissionRepository.deletePermissionByParentIdsStartingWith(permission.getParentIds()+"/"+permission.getId());
        permissionRepository.delete(permission);
    }

}
