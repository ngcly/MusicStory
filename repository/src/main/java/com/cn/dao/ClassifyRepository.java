package com.cn.dao;

import com.cn.entity.Classify;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ngcly
 */
@Repository
public interface ClassifyRepository extends JpaRepository<Classify,Long>, JpaSpecificationExecutor<Classify> {

    /**
     *  动态查询分类数据
     * @param name 分类名
     * @return Specification<Classify>
     */
    static Specification<Classify> getClassifyList(String name){
        return (Root<Classify> root, CriteriaQuery<?> query, CriteriaBuilder cb)->{
            List<Predicate> predicates = new ArrayList<>();
            if(StringUtils.hasLength(name)) {
                predicates.add(cb.like(root.get("name"),"%"+name+"%"));
            }
            return query.where(cb.and(predicates.toArray(new Predicate[0]))).getRestriction();
        };
    }
}
