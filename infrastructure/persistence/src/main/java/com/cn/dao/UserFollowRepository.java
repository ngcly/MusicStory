package com.cn.dao;

import com.cn.entity.UserFollow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Map;

/**
 * @author ngcly
 */
public interface UserFollowRepository extends JpaRepository<UserFollow,Long> {

    /**
     * 根据用户id和被关注者id进行删除
     * @param userId 用户id
     * @param followId 被关注者id
     */
    void deleteUserFollowByUserIdAndFollowId(long userId, long followId);

    /**
     * 判断用户是否已经关注目标用户
     * @param userId 用户id
     * @param followId 被关注者id
     * @return boolean
     */
    boolean existsByUserIdAndFollowId(long userId, long followId);

    /**
     * 获取我的关注用户列表（代替 CustomizeRepository 的原生查询）
     */
    @Query(value = "SELECT t.created_at as created_at, t2.id as userId, t2.username as username, t2.nick_name as nick_name, t2.birthday as birthday, t2.gender as gender, " +
            "t2.address as address, t2.real_name as real_name, t2.person_desc as person_desc, t2.signature as signature, t2.avatar as avatar, t2.phone as phone, t2.email as email " +
            "FROM user_follow t JOIN user_info t2 ON t.follow_id=t2.id WHERE t.user_id = :userId " +
            "ORDER BY t.created_at DESC", 
            countQuery = "SELECT count(*) FROM user_follow t WHERE t.user_id = :userId",
            nativeQuery = true)
    Page<Map<String, Object>> findMyFollowList(@Param("userId") Long userId, Pageable pageable);

    /**
     * 获取关注我的用户列表（代替 CustomizeRepository 的原生查询）
     */
    @Query(value = "SELECT t.created_at as created_at, t2.id as userId, t2.username as username, t2.nick_name as nick_name, t2.birthday as birthday, t2.gender as gender, " +
            "t2.address as address, t2.real_name as real_name, t2.person_desc as person_desc, t2.signature as signature, t2.avatar as avatar, t2.phone as phone, t2.email as email " +
            "FROM user_follow t JOIN user_info t2 ON t.user_id=t2.id WHERE t.follow_id = :userId " +
            "ORDER BY t.created_at DESC", 
            countQuery = "SELECT count(*) FROM user_follow t WHERE t.follow_id = :userId",
            nativeQuery = true)
    Page<Map<String, Object>> findFollowMeList(@Param("userId") Long userId, Pageable pageable);

}
