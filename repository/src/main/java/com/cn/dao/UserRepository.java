package com.cn.dao;

import com.cn.entity.User;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;
import java.util.List;

/**
 * Created by chen on 2017/6/23.
 */
@Repository
//@Table(name="user")
//@Qualifier("userRepository")
//@CacheConfig(cacheNames = "users")
public interface UserRepository extends JpaRepository<User,String> {
//    @Cacheable
    @Query("select t from User t where t.username=:name")
    User findUserByName(@Param("name") String name);

    @Query("select t from User t where t.state='1'")
    List<User> findNormalUsers();
}
