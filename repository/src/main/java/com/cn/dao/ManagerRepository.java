package com.cn.dao;

import com.cn.entity.Manager;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author chen
 * @date 2018-01-02 17:11
 */
@Repository
public interface ManagerRepository extends JpaRepository<Manager,String>,JpaSpecificationExecutor<Manager> {

    @Query("select t from Manager t where t.username=:name")
    Manager findUserByName(@Param("name") String name);

    /**
     * 判断用户名是否存在
     */
    boolean existsByUsernameAndIdIsNot(String username,String userId);

    /**
     * 动态查询管理员数据
     */
    static Specification<Manager> getManagerList(String username,Byte state,Byte gender,Date beginTime,Date endTime){
        return (Root<Manager> root, CriteriaQuery<?> query, CriteriaBuilder cb)->{
                List<Predicate> predicates = new ArrayList<>();
                if(username!=null&&!"".equals(username)) {
                    predicates.add(cb.like(root.get("username"),"%"+username+"%"));
                }

                if(state!=null) {
                    predicates.add(cb.equal(root.get("state"), state));
                }

                if(gender!=null) {
                    predicates.add(cb.equal(root.get("gender"), gender));
                }

                if(beginTime!=null){
                    predicates.add(cb.greaterThanOrEqualTo(root.get("createdTime"), beginTime));
                }

                if(endTime!=null){
                    predicates.add(cb.lessThanOrEqualTo(root.get("createdTime"), endTime));
                }
                return query.where(cb.and(predicates.toArray(new Predicate[predicates.size()]))).getRestriction();
        };
    }
}
