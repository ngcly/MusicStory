package com.cn;

import com.cn.dao.ManagerRepository;
import com.cn.entity.Manager;
import com.cn.util.DateUtil;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

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

    /**
     * 根据用户名获取用户信息
     * @param name
     * @return
     */
    @Transactional
    public Optional<Manager> findUserByName(String name) {
        return Optional.ofNullable(managerRepository.findUserByName(name));
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
    public String saveManager(Manager manager){
        Manager manager1 = managerRepository.findUserByName(manager.getUsername());
        if(StringUtil.isNullOrEmpty(manager.getId())){
            if(manager1!=null){
                return "该用户名已存在";
            }
            BCryptPasswordEncoder bc=new BCryptPasswordEncoder(4);
            manager.setPassword(bc.encode(manager.getPassword()));
        }else {
            if(manager1!=null&&!manager1.getId().equals(manager.getId())){
                return "该用户名已存在";
            }
        }
        managerRepository.save(manager);
        return "成功";
    }

    /**
     * 删除管理员
     */
    @Transactional
    public void delManager(String managerId){
        managerRepository.deleteById(managerId);
    }
}
