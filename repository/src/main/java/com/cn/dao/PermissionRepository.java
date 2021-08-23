package com.cn.dao;

import com.cn.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author ngcly
 * @date 2017-12-20 16:34
 */
@Repository
public interface PermissionRepository extends JpaRepository<Permission,Long> {

    /**
     * 获取菜单列表
     * @return List<Permission>
     */
    @Query("select t from Permission t order by t.sort asc")
    List<Permission> findMenuList();

    /**
     * 根据资源类型获取资源列表
     * @param resourceType 资源类型
     * @return List<Permission>
     */
    List<Permission> findAllByResourceType(String resourceType);

//    @Modifying
//    @Query("delete from Permission t where t.parentIds like :parentId%")
//    void deleteMenus(@Param("parentId") String parentId);

    /**
     * 删除资源
     * @param parentId 父级id
     */
    void deletePermissionByParentIdsStartingWith(String parentId);

}

