package com.cn.dao;

import com.cn.entity.Classify;
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
import java.util.List;

@Repository
public interface ClassifyRepository extends JpaRepository<Classify,Long>, JpaSpecificationExecutor<Classify> {

    /**
     * 动态查询分类数据
     */
    static Specification<Classify> getClassifyList(String name){
        return (Root<Classify> root, CriteriaQuery<?> query, CriteriaBuilder cb)->{
            List<Predicate> predicates = new ArrayList<>();
            if(!StringUtils.isEmpty(name)) {
                predicates.add(cb.like(root.get("name"),"%"+name+"%"));
            }
            return query.where(cb.and(predicates.toArray(new Predicate[predicates.size()]))).getRestriction();
        };
    }
}
