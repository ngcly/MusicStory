package com.cn.dao;

import com.cn.entity.Notice;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public interface NoticeRepository extends JpaRepository<Notice,Long>, JpaSpecificationExecutor<Notice> {
    /**
     * 获取展示公告
     */
    List<Notice> getNoticesByBeginTimeBeforeAndEndTimeAfterOrderByCreatedTimeDesc(Date time1,Date time2);

    /**
     * 动态查询公告
     */
    static Specification<Notice> getNoticeList(String title, Date showTime){
        return (Root<Notice> root, CriteriaQuery<?> query, CriteriaBuilder cb)->{
            List<Predicate> predicates = new ArrayList<>();
            if(!StringUtils.isEmpty(title)) {
                predicates.add(cb.like(root.get("title"),"%"+title+"%"));
            }
            if(showTime!=null){
                predicates.add(cb.lessThanOrEqualTo(root.get("beginTime"), showTime));
                predicates.add(cb.greaterThanOrEqualTo(root.get("endTime"), showTime));
            }
            return query.where(cb.and(predicates.toArray(new Predicate[predicates.size()]))).getRestriction();
        };
    }
}
