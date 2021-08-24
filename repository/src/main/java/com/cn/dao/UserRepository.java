package com.cn.dao;

import com.cn.entity.User;
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
import java.util.Optional;

/**
 *
 * @author ngcly
 * @since 2017/6/23
 */
@Repository
//@Table(name="user")
//@Qualifier("userRepository")
//@CacheConfig(cacheNames = "users")
public interface UserRepository extends JpaRepository<User,Long>, JpaSpecificationExecutor<User> {

    /**
     * 根据用户名或邮箱查找
     * @param username 用户名
     * @param email 邮箱
     * @return User
     */
    Optional<User> findByUsernameOrEmail(String username, String email);

    /**
     * 根据用户名判断用户是否存在
     * @param username 用户名
     * @return boolean
     */
    boolean existsByUsername(String username);

    /**
     * 根据邮箱判断用户是否存在
     * @param email 邮箱
     * @return boolean
     */
    boolean existsByEmail(String email);

    /**
     * 动态查询管理员数据
     * @param username 用户名
     * @param nickName 昵称
     * @param phone 手机号
     * @param email 邮箱
     * @param state 状态
     * @return Specification<User
     */
    static Specification<User> getUserList(String username, String nickName, String phone, String email, Byte state){
        return (Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb)->{
            List<Predicate> predicates = new ArrayList<>();
            if(StringUtils.hasLength(username)) {
                predicates.add(cb.like(root.get("username"),"%"+username+"%"));
            }

            if(StringUtils.hasLength(nickName)) {
                predicates.add(cb.like(root.get("nickName"),"%"+nickName+"%"));
            }

            if(StringUtils.hasLength(phone)) {
                predicates.add(cb.like(root.get("phone"),"%"+phone+"%"));
            }

            if(StringUtils.hasLength(email)) {
                predicates.add(cb.like(root.get("email"),"%"+email+"%"));
            }

            if(Objects.nonNull(state)) {
                predicates.add(cb.equal(root.get("state"), state));
            }

            return query.where(cb.and(predicates.toArray(new Predicate[predicates.size()]))).getRestriction();
        };
    }
}
