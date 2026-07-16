package com.cn.persistence.adapter;
 
import com.cn.dao.SocialInfoRepository;
import com.cn.persistence.mapper.UserMapper;
import com.cn.user.domain.SocialInfo;
import com.cn.user.domain.repository.SocialInfoRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.Optional;
 
/**
 * 社交绑定仓储端口适配器 (Repository Adapter)
 *
 * @author ngcly
 */
@Component
@RequiredArgsConstructor
public class SocialInfoRepositoryAdapter implements SocialInfoRepositoryPort {
    private final SocialInfoRepository socialInfoRepository;
 
    @Override
    public Optional<SocialInfo> findByOpenId(String openId) {
        return Optional.ofNullable(socialInfoRepository.findByOpenId(openId))
                .map(UserMapper::toDomain);
    }
 
    @Override
    public SocialInfo save(SocialInfo socialInfo) {
        com.cn.entity.SocialInfo entity = UserMapper.toEntity(socialInfo);
        return UserMapper.toDomain(socialInfoRepository.save(entity));
    }
 
    @Override
    public long deleteByOpenIdAndUserId(String openId, Long userId) {
        return socialInfoRepository.deleteByOpenIdAndUser_Id(openId, userId);
    }
}
