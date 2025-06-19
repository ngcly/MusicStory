package com.cn;

import com.cn.dao.CustomizeRepository;
import com.cn.dao.UserFavesRepository;
import com.cn.dao.UserFollowRepository;
import com.cn.entity.News;
import com.cn.entity.UserFaves;
import com.cn.entity.UserFollow;
import com.cn.enums.FaveTypeEnum;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.constraints.NotNull;

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
    private final CustomizeRepository customizeRepository;
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
        String sql = "SELECT t.created_at, t2.id as userId, t2.username, t2.nick_name, t2.birthday, t2.gender, " +
                "t2.address, t2.real_name, t2.person_desc, t2.signature, t2.avatar, t2.phone, t2.email" +
                "FROM user_follow t,user t1 WHERE t.user_id=t2.id AND t.user_id=? " +
                "order by t.created_at desc";
        return customizeRepository.nativeQueryListMap(sql, pageable, userId);
    }

    /**
     * 获取关注我的用户列表
     *
     * @param userId   用户id
     * @param pageable 分页
     * @return List<User>
     */
    public List<Map<String, Object>> getFollowMeList(Long userId, Pageable pageable) {
        String sql = "SELECT t.created_at, t2.id as userId, t2.username, t2.nick_name, t2.birthday, t2.gender, " +
                "t2.address, t2.real_name, t2.person_desc, t2.signature, t2.avatar, t2.phone, t2.email" +
                "FROM user_follow t,user t1 WHERE t.follow_id=t2.id AND t.follow_id=? " +
                "order by t.created_at desc";
        return customizeRepository.nativeQueryListMap(sql, pageable, userId);
    }

    /**
     * websocket 通知
     */
    public void notifyUser(@NotNull News news) {
        //TODO 待完善,需要用表记录当前消息及发送状态
        if(Objects.nonNull(news.getUserId())){
            //对某个用户发
            messageTemplate.convertAndSendToUser(news.getUserId().toString(), "/queue/notify", news.getContent());
        }else{
            //群发
            messageTemplate.convertAndSend("/topic/notify", news.getContent());
        }
    }
}
