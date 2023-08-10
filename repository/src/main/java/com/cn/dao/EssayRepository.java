package com.cn.dao;

import com.cn.entity.Classify;
import com.cn.entity.Essay;
import com.cn.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import jakarta.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author ngcly
 */
@Repository
public interface EssayRepository extends JpaRepository<Essay,Long>, JpaSpecificationExecutor<Essay> {

    /**
     * 删除用户的文章
     * @param id 文章id
     * @param userId 用户id
     * @return 影响行数
     */
    int deleteEssayByIdAndUserId(Long id,Long userId);

    /**
     * 动态查询文章数据
     * @param essay 查询条件
     * @return Specification<Essay> 文章列表
     */
    static Specification<Essay> getEssayList(Essay essay){
        return (Root<Essay> root, CriteriaQuery<?> query, CriteriaBuilder cb)->{
            List<Predicate> predicates = new ArrayList<>();
            if(StringUtils.hasLength(essay.getTitle())) {
                predicates.add(cb.like(root.get("title"),"%"+essay.getTitle()+"%"));
            }
            if(Objects.nonNull(essay.getClassify()) && Objects.nonNull(essay.getClassify().getId())) {
                Join<Essay, Classify> join = root.join("classify", JoinType.INNER);
                predicates.add(cb.equal(join.get("id"),essay.getClassify().getId()));
            }
            if(Objects.nonNull(essay.getState())) {
                predicates.add(cb.equal(root.get("state"), essay.getState()));
            }
            if(Objects.nonNull(essay.getUser()) && StringUtils.hasLength(essay.getUser().getUsername())){
                Join<Essay, User> join = root.join("user", JoinType.INNER);
                predicates.add(cb.like(join.get("username"),"%"+essay.getUser().getUsername()+"%"));
            }
            return query.where(cb.and(predicates.toArray(new Predicate[0]))).getRestriction();
        };
    }

    /**
     * 文章阅读数+1
     * @param id 文章id
     * @return int 影响行数
     */
    @Modifying
    @Query("update Essay set readNum=readNum+1 where id=:id")
    int readOne(@Param("id") Long id);

    /**
     * 查询用户 收藏/点赞 文章
     * @param userId 用户id
     * @param faveType 收藏/点赞类型
     * @param pageable 分页
     * @return Page<Essay> 分页文章列表
     */
    @Query("select e from Essay e, UserFaves u where u.userId =:userId and u.essayId = e.id and u.faveType=:faveType")
    Page<Essay> findUserFaveEssay(@Param("userId") Long userId, @Param("faveType") Byte faveType, Pageable pageable);

    /**
     * 获取用户的文章
     * @param userId 用户id
     * @param pageable 分页
     * @return Page<Essay>
     */
    Page<Essay> findEssayByUserId(@Param("userId") Long userId,Pageable pageable);
}
