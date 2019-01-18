package com.cn.dao;

import com.cn.entity.UserFollow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserFollowRepository extends JpaRepository<UserFollow,String> {

    void deleteUserFollowByUserIdAndAndFollowId(String userId,String followId);
}
