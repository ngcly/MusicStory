package com.cn.dao;

import com.cn.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 消息 Repository
 *
 * @author chenning
 */
@Repository
public interface NewsRepository extends JpaRepository<News, Long> {

    /**
     * 根据接收人用户ID查询消息列表 (倒序)
     *
     * @param userId 接收人用户ID
     * @return 消息列表
     */
    List<News> findByUserIdOrderByCreateTimeDesc(Long userId);

}
