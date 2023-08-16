package com.cn.dao;

import com.cn.entity.CarouselCategory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
public interface CarouselRepository extends JpaRepository<CarouselCategory,Long>, JpaSpecificationExecutor<CarouselCategory> {

    /**
     * 动态获取轮播图分类
     * @param name 名称
     * @return Specification<CarouselCategory>
     */
    static Specification<CarouselCategory> getCarouselCategory(String name){
        return (Root<CarouselCategory> root, CriteriaQuery<?> query, CriteriaBuilder cb)->{
            List<Predicate> predicates = new ArrayList<>();
            if(StringUtils.hasLength(name)) {
                predicates.add(cb.like(root.get("title"),"%"+name+"%"));
            }
            return query.where(cb.and(predicates.toArray(new Predicate[0]))).getRestriction();
        };
    }

    /**
     * 删除轮播图分类
     * @param id 主键
     */
    @Modifying
    @Query(value = "delete from carousel where id=:id",nativeQuery = true)
    void deleteCarousel(@Param("id") Long id);
}
