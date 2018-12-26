package com.cn;

import com.cn.dao.NoticeRepository;
import com.cn.entity.Notice;
import com.cn.util.RestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;

@Service
public class NoticeService {
    @Autowired
    NoticeRepository noticeRepository;

    /**
     * 获取最新公告
     * @return
     */
    public ModelMap getNotice(){
        return RestUtil.Success(noticeRepository.findTopByOrderByCreatedTimeDesc());
    }

    /**
     * 获取历史公告
     * @return
     */
    public ModelMap getNoticeList(){
        return RestUtil.Success(noticeRepository.findAll(Sort.by("createdTime")));
    }

    /**
     * 新增公告
     * @param notice
     */
    @Transactional
    public void addNotice(Notice notice){
        noticeRepository.save(notice);
    }

    /**
     * 修改公告
     * @param notice
     */
    @Transactional
    public void updateNotice(Notice notice){
        noticeRepository.save(notice);
    }
}
