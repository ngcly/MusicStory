package com.cn.dao;

import com.cn.entity.SocialUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 第三方用户
 * @author ngcly
 * @version V1.0
 * @since 2021/8/24 21:26
 */
@Repository
public interface SocialUserRepository extends JpaRepository<SocialUser,Long>  {

    /**
     * 根据uuid 查询
     * @param uuid 三方唯一id
     * @return SocialUser
     */
    SocialUser findByUuid(String uuid);

}
