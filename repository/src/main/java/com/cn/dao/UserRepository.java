package com.cn.dao;

import com.cn.entity.User;
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
import java.util.Optional;

/**
 * Created by chen on 2017/6/23.
 */
@Repository
//@Table(name="user")
//@Qualifier("userRepository")
//@CacheConfig(cacheNames = "users")
public interface UserRepository extends JpaRepository<User,String>, JpaSpecificationExecutor<User> {

    Optional<User> findById(String userId);

    Optional<User> findByUsernameOrEmail(String username, String email);

    Optional<User> findByUnionId(String unionId);

    List<User> findByIdIn(List<Long> userIds);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    /**
     * 动态查询管理员数据
     */
    static Specification<User> getUserList(String username, Byte state, Byte gender, Date beginTime, Date endTime){
        return (Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb)->{
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
