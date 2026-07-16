package com.cn.user.domain.repository;
 
import com.cn.user.domain.SocialInfo;
import java.util.Optional;
 
/**
 * 社交绑定信息仓储端口接口 (Repository Port)
 *
 * @author ngcly
 */
public interface SocialInfoRepositoryPort {
    Optional<SocialInfo> findByOpenId(String openId);
    SocialInfo save(SocialInfo socialInfo);
    long deleteByOpenIdAndUserId(String openId, Long userId);
}
