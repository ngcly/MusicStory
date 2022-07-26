package com.cn.dao;

import com.cn.entity.Role;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author ngcly
 * @since 2018-03-21 18:11
 */
@Repository
public interface RoleRepository extends JpaRepository<Role,Long>,JpaSpecificationExecutor<Role> {

    /**
     * 根据角色类型获取所有可用角色
     * @param type 角色类型
     * @return Set<Role>
     */
    List<Role> getAllByAvailableIsTrueAndRoleType(byte type);

    /**
     * 自定义条件查询
     * @param roleName 角色名
     * @param available 是否可用
     * @param roleType 角色类型
     * @return Specification<Role>
     */
    static Specification<Role> getRoleList(String roleName, Boolean available, Byte roleType) {
        return (Root<Role> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.hasLength(roleName)) {
                predicates.add(cb.like(root.get("roleName"), "%" + roleName + "%"));
            }

            if (Objects.nonNull(available)) {
                predicates.add(cb.equal(root.get("available"), available));
            }

            if (Objects.nonNull(roleType)) {
                predicates.add(cb.equal(root.get("roleType"), roleType));
            }

            return query.where(cb.and(predicates.toArray(new Predicate[0]))).getRestriction();
        };
    }
}
