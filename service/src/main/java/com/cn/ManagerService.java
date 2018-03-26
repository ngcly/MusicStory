package com.cn;

import com.cn.dao.ManagerRepository;
import com.cn.dao.RoleRepository;
import com.cn.entity.Manager;
import com.cn.entity.Role;
import com.cn.util.DateUtil;
import com.cn.util.RestUtil;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;

import java.util.*;

/**
 * 后台客服管理员 service类
 *
 * @author chen
 * @date 2018-01-02 17:50
 */
@Service
public class ManagerService {
    @Autowired
    ManagerRepository managerRepository;
    @Autowired
    RoleRepository roleRepository;

    /**
     * 根据用户名获取用户信息
     * @param name
     * @return
     */
    public Optional<Manager> findUserByName(String name) {
        return Optional.ofNullable(managerRepository.findUserByName(name));
    }

    /**
     * 根据Id获取管理员信息
     * @param managerId
     * @return
     */
    public Manager getManagerById(String managerId){
        return managerRepository.getOne(managerId);
    }

    /**
     * 获取管理员列表
     * @param pageable
     * @return
     */
    public Page<Manager> getManagersList(Pageable pageable, Manager manager){
        return managerRepository.findAll(ManagerRepository.getManagerList(manager.getUsername(),
                manager.getState(),manager.getGender(),manager.getBeginTime(),manager.getEndTime()),pageable);
    }

    /**
     * 新增或更新管理员信息
     * @param manager
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ModelMap saveManager(Set<Role> roleList,Manager manager){
        //获取被修改人之前的角色-当前人的角色=必存角色 最后结果为必存角色+回传角色
        Set<Role> allRole = new HashSet<>();
        allRole.addAll(roleList);
        Manager manager1 = managerRepository.findUserByName(manager.getUsername());
        if(StringUtil.isNullOrEmpty(manager.getId())){
            if(manager1!=null){
                return RestUtil.Error(333,"该用户名已存在");
            }
            BCryptPasswordEncoder bc=new BCryptPasswordEncoder(4);
            manager.setPassword(bc.encode("123456"));
            manager.setState((byte) 0);
        }else {
            if(manager1!=null&&!manager1.getId().equals(manager.getId())){
                return RestUtil.Error(333,"该用户名已存在");
            }
            manager.setPassword(manager1.getPassword());
            allRole.addAll(manager1.getRoleList());
            manager1.getRoleList().removeAll(roleList);
        }
        if(manager.getRoleIds()!=null){
            Set<Role> roles = new HashSet<>();
            for(String roleId:manager.getRoleIds()){
                allRole.stream().filter(role -> roleId.equals(role.getId().toString())).forEach(roles::add);
            }
            //加上必存角色
            roles.addAll(manager1.getRoleList());
            manager.setRoleList(roles);
        }
        managerRepository.save(manager);
        return RestUtil.Success();
    }

    /**
     * 删除管理员
     */
    @Transactional(rollbackFor = Exception.class)
    public void delManager(String managerId){
        managerRepository.deleteById(managerId);
    }

    /**
     * 修改管理员密码
     * @param managerId
     * @param password
     */
    @Transactional(rollbackFor = Exception.class)
    public void updatePassword(String managerId,String password){
        Manager manager = managerRepository.getOne(managerId);
        BCryptPasswordEncoder bc=new BCryptPasswordEncoder(4);
        manager.setPassword(bc.encode(password));
    }
}
