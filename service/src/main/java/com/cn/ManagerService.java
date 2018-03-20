package com.cn;

import com.cn.dao.ManagerRepository;
import com.cn.entity.Manager;
import com.cn.util.DateUtil;
import io.netty.util.internal.StringUtil;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
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
    public Optional<Manager> findUserByName(String name) {
        return Optional.ofNullable(managerRepository.findUserByName(name));
    }

    /**
     * 获取管理员列表
     * @param pageable
     * @return
     */
    public Page<Manager> getManagersList(Pageable pageable, Manager manager){
        Date beginTime=null,endTime=null;
        if(manager.getBeginTime()!=null&&!"".equals(manager.getBeginTime())){
            beginTime = DateUtil.parse(manager.getBeginTime());
        }
        if(manager.getEndTime()!=null&&!"".equals(manager.getEndTime())){
            endTime = DateUtil.parse(manager.getEndTime());
        }
        return managerRepository.findAll(ManagerRepository.getManagerList(manager.getUsername(),
                manager.getState(),manager.getGender(),beginTime,endTime),pageable);
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
}
