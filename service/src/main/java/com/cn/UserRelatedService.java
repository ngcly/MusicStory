package com.cn;

import com.cn.dao.UserFavesRepository;
import com.cn.dao.UserFollowRepository;
import com.cn.entity.News;
import com.cn.entity.UserFaves;
import com.cn.entity.UserFollow;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

/**
 * 用户相关 service
 * @author ngcly
 * @version V1.0
 * @since 2021/8/26 15:16
 */
@Service
public class UserRelatedService {
    @Resource
    private UserFavesRepository userFavesRepository;
    @Resource
    private UserFollowRepository userFollowRepository;
    @Resource
    private SimpMessageSendingOperations messageTemplate;

    /**
     * 点赞/收藏
     * @param userId   用户ID
     * @param essayId  文章ID
     * @param type     类型
     */
    @Transactional(rollbackFor = Exception.class)
    public void addUserFaves(Long userId,Long essayId,byte type){
        UserFaves userFaves = new UserFaves();
        userFaves.setUserId(userId);
        userFaves.setEssayId(essayId);
        userFaves.setFaveType(type);
        userFavesRepository.save(userFaves);
    }

    /**
     * 取消点赞/收藏
     * @param userId   用户ID
     * @param essayId  文章ID
     * @param type     类型
     */
    @Transactional(rollbackFor = Exception.class)
    public void delUserFaves(Long userId,Long essayId,byte type){
        userFavesRepository.deleteUserFavesByUserIdAndEssayIdAndFaveType(userId,essayId,type);
    }

    /**
     * 关注
     * @param userId   用户ID
     * @param followId 被关注ID
     */
    @Transactional(rollbackFor = Exception.class)
    public void addUserFollow(Long userId,Long followId){
        UserFollow userFollow = new UserFollow();
        userFollow.setUserId(userId);
        userFollow.setFollowId(followId);
        userFollowRepository.save(userFollow);
    }

    /**
     * 取消关注
     * @param userId    用户ID
     * @param followId  被关注ID
     */
    @Transactional(rollbackFor = Exception.class)
    public void delUserFollow(Long userId,Long followId){
        userFollowRepository.deleteUserFollowByUserIdAndFollowId(userId,followId);
    }

    /**
     * websocket 通知
     */
    public void notifyUser(@NotNull News news){
        //TODO 待完善
        //群发
        messageTemplate.convertAndSend("/topic/notify", "webSocket消息测试");
        //对某个用户发
        messageTemplate.convertAndSendToUser("ngcly","/queue/notify",news.getContent());
    }
}
