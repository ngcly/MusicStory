package com.cn.dao;

import com.cn.entity.Classify;
import com.cn.entity.Essay;
import com.cn.entity.User;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public interface EssayRepository extends JpaRepository<Essay,String>, JpaSpecificationExecutor<Essay> {

    Essay getById(String id);

    void deleteEssayByIdAndUserId(String id,String userId);

    /**
     * 动态查询文章数据
     */
    static Specification<Essay> getEssayList(Essay essay){
        return (Root<Essay> root, CriteriaQuery<?> query, CriteriaBuilder cb)->{
            List<Predicate> predicates = new ArrayList<>();
            if(!StringUtils.isEmpty(essay.getTitle())) {
                predicates.add(cb.like(root.get("title"),"%"+essay.getTitle()+"%"));
            }
            if(essay.getClassify()!=null&&!StringUtils.isEmpty(essay.getClassify().getId())) {
                Join<Essay, Classify> join = root.join("classify", JoinType.INNER);
                predicates.add(cb.equal(join.get("id"),essay.getClassify().getId()));
            }
            if(essay.getState()!=null) {
                predicates.add(cb.equal(root.get("state"), essay.getState()));
            }
            if(essay.getUser()!=null&&!StringUtils.isEmpty(essay.getUser().getUsername())){
                Join<Essay, User> join = root.join("user", JoinType.INNER);
                predicates.add(cb.like(join.get("username"),"%"+essay.getUser().getUsername()+"%"));
            }
            return query.where(cb.and(predicates.toArray(new Predicate[predicates.size()]))).getRestriction();
        };
    }
}
