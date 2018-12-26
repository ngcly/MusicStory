package com.cn.dao;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Map;

@Repository
public class OtherDAO {
    @PersistenceContext  //注入实体管理器
    private EntityManager entityManager;

    public List<Map<String, String>> getAll() {
        String sql = "select * from sys_user";
        Query query = entityManager.createNativeQuery(sql,Map.class);
        return query.getResultList();
    }

}
