package com.cn.dao;

import com.cn.entity.Role;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author chen
 * @date 2018-03-21 18:11
 */
@Repository
public interface RoleRepository extends JpaRepository<Role,Long>,JpaSpecificationExecutor<Role> {

    /**
     * 获取所有可用角色
     */
    Set<Role> getAllByAvailableIsTrue();

    /**
     * 自定义条件查询
     */
    static Specification<Role> getRoleList(String roleName, Boolean available, Byte roleType) {
        return (Root<Role> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (roleName != null && !"".equals(roleName)) {
                predicates.add(cb.like(root.get("roleName"), "%" + roleName + "%"));
            }

            if (available != null) {
                predicates.add(cb.equal(root.get("available"), available));
            }

            if (roleType != null) {
                predicates.add(cb.equal(root.get("roleType"), roleType));
            }

            return query.where(cb.and(predicates.toArray(new Predicate[predicates.size()]))).getRestriction();
        };
    }
}
