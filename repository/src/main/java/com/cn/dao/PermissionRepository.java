package com.cn.dao;

import com.cn.entity.Permission;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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
public interface PermissionRepository extends JpaRepository<Permission,Long> {

    @Query("select t from Permission t order by t.sort asc")
    List<Permission> findMenuList();

    List<Permission> findAllByResourceType(String resourceType);

//    @Modifying
//    @Query("delete from Permission t where t.parentIds like :parentId%")
//    void deleteMenus(@Param("parentId") String parentId);

    void deletePermissionByParentIdsStartingWith(String parentId);

}

