package com.cn;

import com.cn.config.GlobalException;
import com.cn.dao.ManagerRepository;
import com.cn.dao.RoleRepository;
import com.cn.entity.Manager;
import com.cn.entity.Role;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * 后台客服管理员 service类
 * @author ngcly
 * @since 2018-01-02 17:50
 */
@Service
@AllArgsConstructor
public class ManagerService implements UserDetailsService {
    private final ManagerRepository managerRepository;
    private final RoleRepository roleRepository;

    /**
     * 根据用户名获取用户
     * @param username 用户名
     * @return UserDetails
     * @throws UsernameNotFoundException 用户名未找到
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return managerRepository.findManagerByUsername(username).orElseThrow(() -> new UsernameNotFoundException("用户不存在"));
    }

    /**
     * 根据Id获取管理员信息
     * @param managerId 管理员id
     * @return Manager
     */
    public Manager getManagerById(Long managerId){
        return managerRepository.getById(managerId);
    }

    /**
     * 获取管理员列表
     * @param pageable 分页
     * @return Page<Manager>
     */
    public Page<Manager> getManagersList(Pageable pageable, Manager manager){
        return managerRepository.findAll(ManagerRepository.getManagerList(manager.getUsername(),
                manager.getState(),manager.getGender(),manager.getBeginTime(),manager.getEndTime()),pageable);
    }

    /**
     * 新增或更新管理员信息
     * @param curManager 当前操作人
     * @param updateManager 被修改人
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveManager(Manager curManager,Manager updateManager){
        Set<Role> roleList = curManager.getRoleList();
        if(Manager.ADMIN.equals(curManager.getUsername())){
            roleList = roleRepository.getAllByAvailableIsTrueAndRoleType(Role.ROLE_TYPE_MANAGER);
        }
        //获取被修改人之前的角色-当前人的角色=必存角色 最后结果为必存角色+回传角色
        Set<Role> allRole = new HashSet<>(roleList);

        Set<Role> roles = new HashSet<>();
        if(Objects.nonNull(updateManager.getId())){
            Manager manager = managerRepository.getById(updateManager.getId());
            //判断是否当前人在改自己的信息
            if(!curManager.getId().equals(updateManager.getId())){
                updateManager.setUsername(manager.getUsername());
            }
            updateManager.setPassword(manager.getPassword());
            allRole.addAll(manager.getRoleList());
            manager.getRoleList().removeAll(roleList);
            //加上必存角色 防止越权篡改数据
            roles.addAll(manager.getRoleList());
        }else{
            BCryptPasswordEncoder bc=new BCryptPasswordEncoder(4);
            updateManager.setPassword(bc.encode("123456"));
            updateManager.setState(Manager.STATE_INITIALIZE);
        }
        if(!StringUtils.hasText(updateManager.getUsername())){
            throw new GlobalException(333,"用户名不可为空");
        }
        if(managerRepository.existsByUsernameAndIdIsNot(updateManager.getUsername(),updateManager.getId())){
            throw new GlobalException(333,"该用户名已存在");
        }

        if(updateManager.getRoleIds()!=null){
            List<Long> roleIds = Arrays.asList(updateManager.getRoleIds());
            allRole.stream().filter(role -> roleIds.contains(role.getId())).forEach(roles::add);
            updateManager.setRoleList(roles);
        }
        managerRepository.save(updateManager);
    }

    /**
     * 更新管理员信息
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateManager(Manager manager){
        managerRepository.save(manager);
    }

    /**
     * 删除管理员
     */
    @Transactional(rollbackFor = Exception.class)
    public void delManager(Long managerId){
        managerRepository.deleteById(managerId);
    }

    /**
     * 修改管理员密码
     * @param managerId 管理员id
     * @param password 密码
     */
    @Transactional(rollbackFor = Exception.class)
    public void updatePassword(Long managerId,String oldPassword,String password){
        Manager manager = managerRepository.getById(managerId);
        BCryptPasswordEncoder bc=new BCryptPasswordEncoder(4);
        if(!bc.matches(oldPassword,manager.getPassword())){
           throw new GlobalException(333,"原密码错误");
        }
        manager.setPassword(bc.encode(password));
    }

    /**
     * 重置密码
     * @param managerId 管理员id
     * @param password 修改密码
     */
    @Transactional(rollbackFor = Exception.class)
    public void updatePassword(Long managerId,String password){
        Manager manager = managerRepository.getById(managerId);
        BCryptPasswordEncoder bc=new BCryptPasswordEncoder(4);
        manager.setPassword(bc.encode(password));
    }

}
