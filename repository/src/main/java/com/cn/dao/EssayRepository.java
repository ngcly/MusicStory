package com.cn.dao;

import com.cn.entity.Essay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface EssayRepository extends JpaRepository<Essay,String>, JpaSpecificationExecutor<Essay> {

    Essay getById(String id);
}
