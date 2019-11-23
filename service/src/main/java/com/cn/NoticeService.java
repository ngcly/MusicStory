package com.cn;

import com.cn.dao.NoticeRepository;
import com.cn.entity.Notice;
import com.cn.util.RestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;

import java.util.Date;

@Service
public class NoticeService {
    @Autowired
    NoticeRepository noticeRepository;

    /**
     * 获取展示公告
     */
    public ModelMap getNotice(){
        Date now = new Date();
        return RestUtil.success(noticeRepository.getNoticesByBeginTimeBeforeAndEndTimeAfterOrderByCreatedTimeDesc(now,now));
    }

    /**
     * 获取历史公告
     * @return
     */
    public ModelMap getNoticeList(){
        return RestUtil.success(noticeRepository.findAll(Sort.by("createdTime")));
    }

    /**
     * 获取公告详情
     * @param id 主键
     * @return
     */
    public Notice getNoticeDetail(long id){
        return noticeRepository.getOne(id);
    }

    /**
     * 根据条件获取公告
     * @param pageable
     * @param notice
     * @return
     */
    public Page<Notice> getNoticeList(Pageable pageable, Notice notice){
        return noticeRepository.findAll(NoticeRepository.getNoticeList(notice.getTitle(),notice.getShowTime()),pageable);
    }

    /**
     * 新增修改公告
     * @param notice
     */
    @Transactional(rollbackFor = Exception.class)
    public void addOrUpdateNotice(Notice notice){
        noticeRepository.save(notice);
    }

    /**
     * 删除公告
     * @param id 主键
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteNotice(Long id){
        noticeRepository.deleteById(id);
    }
}
