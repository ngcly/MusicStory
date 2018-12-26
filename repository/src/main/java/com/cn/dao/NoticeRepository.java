package com.cn.dao;

import com.cn.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeRepository extends JpaRepository<Notice,String> {
    //获取最新公告
    Notice findTopByOrderByCreatedTimeDesc();
}
