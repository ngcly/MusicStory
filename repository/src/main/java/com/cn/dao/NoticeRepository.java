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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author ngcly
 */
@Repository
public interface NoticeRepository extends JpaRepository<Notice,Long>, JpaSpecificationExecutor<Notice> {
    /**
     * 获取展示公告
     * @param beginTime 开始时间
     * @param endTime 结束时间
     * @return List<Notice>
     */
    List<Notice> getNoticesByBeginTimeBeforeAndEndTimeAfterOrderByCreatedAtDesc(LocalDateTime beginTime,LocalDateTime endTime);

    /**
     * 动态查询公告
     * @param title 标题
     * @param showTime 展示时间
     * @return Specification<Notice>
     */
    static Specification<Notice> getNoticeList(String title, LocalDateTime showTime){
        return (Root<Notice> root, CriteriaQuery<?> query, CriteriaBuilder cb)->{
            List<Predicate> predicates = new ArrayList<>();
            if(StringUtils.hasLength(title)) {
                predicates.add(cb.like(root.get("title"),"%"+title+"%"));
            }
            if(Objects.nonNull(showTime)){
                predicates.add(cb.lessThanOrEqualTo(root.get("beginTime"), showTime));
                predicates.add(cb.greaterThanOrEqualTo(root.get("endTime"), showTime));
            }
            return query.where(cb.and(predicates.toArray(new Predicate[predicates.size()]))).getRestriction();
        };
    }
}
