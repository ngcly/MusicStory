package com.cn.dao;

import com.cn.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author chen
 * @date 2018-03-21 18:11
 */
@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
}
