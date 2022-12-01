package com.cn.dao;

import org.hibernate.query.sql.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

/**
 * 自定义sql查询
 * @author ngcly
 */
@Repository
public class CustomizeRepository {
    //注入实体管理器
    @PersistenceContext
    private EntityManager entityManager;

    public void save(Object entity) {
        entityManager.persist(entity);
    }

    public void update(Object entity) {
        entityManager.merge(entity);
    }

    public <T> void delete(Class<T> entityClass, Object entityid) {
        delete(entityClass, new Object[] { entityid });
    }

    public <T> void delete(Class<T> entityClass, Object[] entityids) {
        for (Object id : entityids) {
            entityManager.remove(entityManager.getReference(entityClass, id));
        }
    }
    private Query createNativeQuery(String sql, Object... params) {
        Query q = entityManager.createNativeQuery(sql);
        if (params != null && params.length > 0) {
            for (int i = 0; i < params.length; i++) {
                // 与Hibernate不同,jpa query从位置1开始
                q.setParameter(i + 1, params[i]);
            }
        }
        return q;
    }

    private Query createPageNativeQuery(String sql, Pageable pageable,Object... params) {
        Query q = entityManager.createNativeQuery(sql);
        if (params != null && params.length > 0) {
            for (int i = 0; i < params.length; i++) {
                // 与Hibernate不同,jpa query从位置1开始
                q.setParameter(i + 1, params[i]);
            }
            q.setFirstResult(pageable.getPageNumber());
            q.setMaxResults(pageable.getPageSize());
        }
        return q;
    }

    private BigInteger countPageQuery(String sql, Object... params){
        Query q = entityManager.createNativeQuery(sql);
        if (params != null && params.length > 0) {
            for (int i = 0; i < params.length; i++) {
                q.setParameter(i + 1, params[i]);
            }
        }
        return (BigInteger) q.getSingleResult();
    }

    public <T> List<T> nativeQueryList(String nativeSql, Object... params) {
        Query q = createNativeQuery(nativeSql, params);
        q.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.TO_LIST);
        return q.getResultList();
    }

    public <T> Page<T> nativePageQueryList(String nativeSql, Pageable pageable, Object... params) {
        Query q = createPageNativeQuery(nativeSql, pageable, params);
        q.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.TO_LIST);
        return new PageImpl<T>(q.getResultList(), pageable, countPageQuery("select count(*) from ("+nativeSql+") tall",params).intValue());
    }

    public <T> List<T> nativeQueryListModel(Class<T> resultClass,
                                            String nativeSql, Object... params) {
        Query q = createNativeQuery(nativeSql, params);
        q.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.aliasToBean(resultClass));
        return q.getResultList();
    }

    public <T> Page<T> nativePageQueryListModel(Class<T> resultClass, Pageable pageable,
                                            String nativeSql, Object... params) {
        Query q = createPageNativeQuery(nativeSql, pageable, params);
        q.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.aliasToBean(resultClass));
        Page<T> page = new PageImpl<T>(q.getResultList(), pageable, countPageQuery("select count(*) from ("+nativeSql+") tall",params).intValue());
        return page;
    }

    public List<Map<String,Object>> nativeQueryListMap(String nativeSql, Object... params) {
        Query q = createNativeQuery(nativeSql, params);
        q.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return q.getResultList();
    }

    public Page<Map<String,Object>> nativePageQueryListMap(String nativeSql, Pageable pageable, Object... params) {
        Query q = createNativeQuery(nativeSql, params);
        q.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        Page<Map<String,Object>> page = new PageImpl<Map<String,Object>>(q.getResultList(), pageable,
                countPageQuery("select count(*) from ("+nativeSql+") tall",params).intValue());
        return page;
    }
}
