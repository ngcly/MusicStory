package com.cn.dao;

import com.cn.entity.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author chen
 * @date 2018-01-02 17:11
 */
public interface ManagerRepository extends JpaRepository<Manager,String> {

    @Query("select t from Manager t where t.username=:name")
    Manager findUserByName(@Param("name") String name);

    @Query("select t from Manager t where t.state='1'")
    List<Manager> findNormalUsers();
}
