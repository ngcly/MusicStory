package com.cn;

import com.cn.dao.ManagerRepository;
import com.cn.entity.Manager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public Optional<Manager> findUserByName(String name) {
        return Optional.ofNullable(managerRepository.findUserByName(name));
    }

    public void saveManager(Manager manager){
        managerRepository.save(manager);
    }
}
