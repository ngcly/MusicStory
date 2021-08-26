package com.cn.dao;

import com.cn.entity.SocialInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 第三方用户
 * @author ngcly
 * @version V1.0
 * @since 2021/8/24 21:26
 */
@Repository
public interface SocialInfoRepository extends JpaRepository<SocialInfo,Long>  {

    /**
     * 根据uuid 查询
     * @param openId 三方唯一openId
     * @return SocialUser
     */
    SocialInfo findByOpenId(String openId);

    /**
     * 删除记录
     * @param openId 三方唯一openid
     */
    void deleteByOpenId(String openId);

}
