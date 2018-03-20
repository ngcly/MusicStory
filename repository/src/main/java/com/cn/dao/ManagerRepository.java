package com.cn.dao;

import com.cn.entity.Manager;
import jdk.nashorn.internal.runtime.Specialization;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author chen
 * @date 2018-01-02 17:11
 */
@Repository
public interface ManagerRepository extends JpaRepository<Manager,String>,JpaSpecificationExecutor<Manager> {

    @Query("select t from Manager t where t.username=:name")
    Manager findUserByName(@Param("name") String name);

    @Query("select t from Manager t where t.state='1'")
    List<Manager> findNormalUsers();

    static Specification<Manager> getManagerList(final String username,final Byte state,final Byte gender,final Date beginTime,final Date endTime){
        return new Specification<Manager>() {
            @Override
            public Predicate toPredicate(Root<Manager> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<Predicate>();
                if(username!=null&&!"".equals(username)) {
                    predicates.add(cb.equal(root.get("username"),username));
                }

                if(state!=null) {
                    predicates.add(cb.equal(root.get("state"), state));
                }

                if(gender!=null) {
                    predicates.add(cb.equal(root.get("gender"), gender));
                }

                if(beginTime!=null){
                    predicates.add(cb.greaterThanOrEqualTo(root.get("createTime"), beginTime));
                }

                if(endTime!=null){
                    predicates.add(cb.lessThanOrEqualTo(root.get("createTime"), endTime));
                }

                return query.where(new Predicate[predicates.size()]).getRestriction();
            }
        };
    }
}
