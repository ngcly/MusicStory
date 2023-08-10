package com.cn.dao;

import com.cn.entity.Manager;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author ngcly
 * @since 2018-01-02 17:11
 */
@Repository
public interface ManagerRepository extends JpaRepository<Manager,Long>,JpaSpecificationExecutor<Manager> {
    @EntityGraph(value = "Role.Graph", type = EntityGraph.EntityGraphType.FETCH)
    Optional<Manager> findManagerById(Long id);

    /**
     * 根据用户名查询
     * @param username 用户名
     * @return Manager
     */
    @EntityGraph(value = "Role.Graph", type = EntityGraph.EntityGraphType.FETCH)
    Optional<Manager> findManagerByUsername(String username);

    /**
     * 判断用户名是否存在
     * @param username 用户名
     * @param userId 用户id
     * @return boolean
     */
    boolean existsByUsernameAndIdIsNot(String username,Long userId);

    /**
     * 动态查询管理员数据
     * @param username 用户名
     * @param state 状态
     * @param gender 性别
     * @param beginTime 开始时间
     * @param endTime 结束时间
     * @return Specification<Manager>
     */
    static Specification<Manager> getManagerList(String username, Byte state, Byte gender, LocalDateTime beginTime, LocalDateTime endTime){
        return (Root<Manager> root, CriteriaQuery<?> query, CriteriaBuilder cb)->{
                List<Predicate> predicates = new ArrayList<>();
                if(StringUtils.hasLength(username)) {
                    predicates.add(cb.like(root.get("username"),"%"+username+"%"));
                }

                if(Objects.nonNull(state)) {
                    predicates.add(cb.equal(root.get("state"), state));
                }

                if(Objects.nonNull(gender)) {
                    predicates.add(cb.equal(root.get("gender"), gender));
                }

                if(Objects.nonNull(beginTime)){
                    predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), beginTime));
                }

                if(Objects.nonNull(endTime)){
                    predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), endTime));
                }
                return query.where(cb.and(predicates.toArray(new Predicate[0]))).getRestriction();
        };
    }
}
