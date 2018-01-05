package com.cn.dao;

import com.cn.entity.Permission;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;
import java.util.List;

/**
 * @author chen
 * @date 2017-12-20 16:34
 */

@Repository
//@Table(name="permission")
//@Qualifier("permissionRepository")
public interface PermissionRepository extends JpaRepository<Permission,Long> {

    @Query("select t from Permission t where t.available= true order by t.sort")
    List<Permission> findMenuList();
}

