package com.cn.dao;

import com.cn.entity.UserFollow;
import org.springframework.data.jpa.repository.JpaRepository;

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

}
