package com.cn;

import com.cn.dao.NoticeRepository;
import com.cn.entity.Notice;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author ngcly
 */
@Service
@AllArgsConstructor
public class NoticeService {
    private final NoticeRepository noticeRepository;

    /**
     * 获取展示公告
     */
    public List<Notice> getNotice(){
        LocalDateTime now = LocalDateTime.now();
        return noticeRepository.getNoticesByBeginTimeBeforeAndEndTimeAfterOrderByCreatedAtDesc(now,now);
    }

    /**
     * 获取历史公告
     * @return List<Notice>
     */
    public List<Notice> getNoticeList(){
        return noticeRepository.findAll(Sort.by("createdAt"));
    }

    /**
     * 获取公告详情
     * @param id 主键
     * @return Notice
     */
    public Notice getNoticeDetail(long id){
        return noticeRepository.getReferenceById(id);
    }

    /**
     * 根据条件获取公告
     * @param pageable 分页
     * @param notice 公告
     * @return Page<Notice>
     */
    public Page<Notice> getNoticeList(Pageable pageable, Notice notice){
        return noticeRepository.findAll(NoticeRepository.getNoticeList(notice.getTitle(),notice.getShowTime()),pageable);
    }

    /**
     * 新增修改公告
     * @param notice 公告
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
