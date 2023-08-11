package com.cn.dao;

import com.cn.entity.UserFaves;
import com.cn.enums.FaveTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author ngcly
 */
@Repository
public interface UserFavesRepository extends JpaRepository<UserFaves, Long> {

    /**
     * 删除用户 点赞/收藏 记录
     *
     * @param userId  用户id
     * @param essayId 文章id
     * @param type    类型
     */
    void deleteUserFavesByUserIdAndEssayIdAndFaveType(long userId, long essayId, FaveTypeEnum type);

}
