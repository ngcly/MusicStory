package com.cn.relation;

import com.cn.dao.NewsRepository;
import com.cn.dao.UserFavesRepository;
import com.cn.dao.UserFollowRepository;
import com.cn.entity.News;
import com.cn.entity.UserFaves;
import com.cn.entity.UserFollow;
import com.cn.enums.FaveTypeEnum;
import com.cn.exception.GlobalException;
import com.cn.model.RestCode;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 用户相关 service
 *
 * @author ngcly
 * @version V1.0
 * @since 2021/8/26 15:16
 */
@Service
@AllArgsConstructor
public class UserRelatedService {
    private final UserFavesRepository userFavesRepository;
    private final UserFollowRepository userFollowRepository;
    private final NewsRepository newsRepository;
    private final SimpMessageSendingOperations messageTemplate;

    /**
     * 点赞/收藏
     *
     * @param userId  用户ID
     * @param essayId 文章ID
     * @param type    类型
     */
    @Transactional(rollbackFor = Exception.class)
    public void addUserFaves(Long userId, Long essayId, FaveTypeEnum type) {
        if (userFavesRepository.existsByUserIdAndEssayIdAndFaveType(userId, essayId, type)) {
            return;
        }
        UserFaves userFaves = new UserFaves();
        userFaves.setUserId(userId);
        userFaves.setEssayId(essayId);
        userFaves.setFaveType(type);
        userFavesRepository.save(userFaves);
    }

    /**
     * 取消点赞/收藏
     *
     * @param userId  用户ID
     * @param essayId 文章ID
     * @param type    类型
     */
    @Transactional(rollbackFor = Exception.class)
    public void delUserFaves(Long userId, Long essayId, FaveTypeEnum type) {
        userFavesRepository.deleteUserFavesByUserIdAndEssayIdAndFaveType(userId, essayId, type);
    }

    /**
     * 关注
     *
     * @param userId   用户ID
     * @param followId 被关注ID
     */
    @Transactional(rollbackFor = Exception.class)
    public void addUserFollow(Long userId, Long followId) {
        if (userId.equals(followId)) {
            throw new GlobalException(RestCode.PARAM_ERROR.code, "不能关注自己");
        }
        if (userFollowRepository.existsByUserIdAndFollowId(userId, followId)) {
            return;
        }
        UserFollow userFollow = new UserFollow();
        userFollow.setUserId(userId);
        userFollow.setFollowId(followId);
        userFollowRepository.save(userFollow);
    }

    /**
     * 取消关注
     *
     * @param userId   用户ID
     * @param followId 被关注ID
     */
    @Transactional(rollbackFor = Exception.class)
    public void delUserFollow(Long userId, Long followId) {
        userFollowRepository.deleteUserFollowByUserIdAndFollowId(userId, followId);
    }

    /**
     * 获取我的关注用户列表
     *
     * @param userId   用户id
     * @param pageable 分页
     * @return List<Map>
     */
    public List<Map<String, Object>> getMyFollowList(Long userId, Pageable pageable) {
        return userFollowRepository.findMyFollowList(userId, pageable).getContent();
    }

    /**
     * 获取关注我的用户列表
     *
     * @param userId   用户id
     * @param pageable 分页
     * @return List<User>
     */
    public List<Map<String, Object>> getFollowMeList(Long userId, Pageable pageable) {
        return userFollowRepository.findFollowMeList(userId, pageable).getContent();
    }

    /**
     * 获取当前用户的消息列表
     *
     * @param userId 用户ID
     * @return 消息列表
     */
    public List<News> getMyNews(Long userId) {
        return newsRepository.findByUserIdOrderByCreateTimeDesc(userId);
    }

    /**
     * 发送用户消息
     *
     * @param senderId 发送人用户ID
     * @param dto      消息发送 DTO
     * @return 保存后的消息实体
     */
    @Transactional(rollbackFor = Exception.class)
    public News sendNews(Long senderId, com.cn.model.NewsDTO dto) {
        News news = new News();
        news.setUserId(dto.getUserId());
        news.setSenderId(senderId);
        news.setContent(dto.getContent());
        news.setCreateTime(Instant.now());
        news.setSent(false);
        News saved = newsRepository.save(news);
        notifyUser(saved);
        return saved;
    }

    /**
     * websocket 通知并更新消息状态
     */
    @Transactional(rollbackFor = Exception.class)
    public void notifyUser(@NotNull News news) {
        news.setSent(true);
        news.setSendTime(Instant.now());
        newsRepository.save(news);

        if (Objects.nonNull(news.getUserId())) {
            // 对某个用户发送
            messageTemplate.convertAndSendToUser(news.getUserId().toString(), "/queue/notify", news.getContent());
        } else {
            // 群发
            messageTemplate.convertAndSend("/topic/notify", news.getContent());
        }
    }
}
