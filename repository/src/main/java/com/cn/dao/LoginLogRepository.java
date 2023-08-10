package com.cn.dao;

import com.cn.entity.LoginLog;
import com.cn.enums.UserTypeEnum;
import org.springframework.data.jpa.domain.Specification;
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

/**
 * 日志操作
 *
 * @author chen
 * @date 2018-01-02 18:55
 */
@Repository
public interface LoginLogRepository extends JpaRepository<LoginLog,Long>,JpaSpecificationExecutor<LoginLog> {

    /**
     * 动态查询登录日志数据
     * @param userName 用户名
     * @param userType 用户类型
     * @param beginTime 开始时间
     * @param endTime 截止时间
     * @return Specification<LoginLog>
     */
    static Specification<LoginLog> getLoginLogList(String userName, UserTypeEnum userType, LocalDateTime beginTime, LocalDateTime endTime){
        return (Root<LoginLog> root, CriteriaQuery<?> query, CriteriaBuilder cb)->{
            List<Predicate> predicates = new ArrayList<>();
            if(StringUtils.hasLength(userName)) {
                predicates.add(cb.like(root.get("userName"),"%"+userName+"%"));
            }

            if(Objects.nonNull(userType)) {
                predicates.add(cb.equal(root.get("userType"), userType));
            }

            if(Objects.nonNull(beginTime)){
                predicates.add(cb.greaterThanOrEqualTo(root.get("loginTime"), beginTime));
            }

            if(Objects.nonNull(endTime)){
                predicates.add(cb.lessThanOrEqualTo(root.get("loginTime"), endTime));
            }
            return query.where(cb.and(predicates.toArray(new Predicate[0]))).getRestriction();
        };
    }

}
