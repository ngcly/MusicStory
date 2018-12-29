package com.cn.dao;

import com.cn.entity.Essay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EssayRepository extends JpaRepository<Essay,String>, JpaSpecificationExecutor<Essay> {

    @Query(value = "select new com.cn.entity.Essay(t.id,t.title,substring(t.content,0,300),t2.name,t.created_time,t.updated_time,t.readNum) " +
            "from essay t,classify t2 where t.classify_id=t2.id limit :page,:pageSize",nativeQuery = true)
    List<Essay> getEssayList(@Param("page")int page,@Param("pageSize")int pageSize);

    Essay getById(String id);
}
