package com.cn.dao;

import com.cn.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;


/**
 * @author ngcly
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment,String> {

    Page<Comment> findByEssayIdOrderByCreatedTimeDesc(String essayId, Pageable pageable);

    @Query("select new map(t2.id as userId,t2.username as username,t2.avatar as avatar,t1.id as id," +
            "t1.createdTime as createdTime,t1.replyUserId as replyUserId,t1.content as content)" +
            " from Comment t1 inner join User t2 on t1.userId=t2.id where t1.essayId=:essayId")
    Page<Map<String,Object>> selectComments(@Param("essayId") String essayId, Pageable pageable);
}
