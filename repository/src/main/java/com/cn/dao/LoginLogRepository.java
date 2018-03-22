package com.cn.dao;

import com.cn.entity.LoginLog;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 日志操作
 *
 * @author chen
 * @date 2018-01-02 18:55
 */
@Repository
public interface LoginLogRepository extends JpaRepository<LoginLog,String>,JpaSpecificationExecutor<LoginLog> {
    /**
     * 动态查询登录日志数据
     */
    static Specification<LoginLog> getLoginLogList(String userName, Byte userType, Date beginTime, Date endTime){
        return (Root<LoginLog> root, CriteriaQuery<?> query, CriteriaBuilder cb)->{
            List<Predicate> predicates = new ArrayList<>();
            if(userName!=null&&!"".equals(userName)) {
                predicates.add(cb.like(root.get("userName"),"%"+userName+"%"));
            }

            if(userType!=null) {
                predicates.add(cb.equal(root.get("userType"), userType));
            }

            if(beginTime!=null&&!"".equals(beginTime)){
                predicates.add(cb.greaterThanOrEqualTo(root.get("loginTime"), beginTime));
            }

            if(endTime!=null&&!"".equals(endTime)){
                predicates.add(cb.lessThanOrEqualTo(root.get("loginTime"), endTime));
            }
            return query.where(cb.and(predicates.toArray(new Predicate[predicates.size()]))).getRestriction();
        };
    }
}
