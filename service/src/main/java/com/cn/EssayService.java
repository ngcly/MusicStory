package com.cn;

import com.cn.config.RabbitConfig;
import com.cn.dao.ClassifyRepository;
import com.cn.dao.CommentRepository;
import com.cn.dao.CustomizeRepository;
import com.cn.dao.EssayRepository;
import com.cn.entity.*;
import com.cn.enums.EssayState;
import com.cn.util.MailUtil;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author ngcly
 */
@Service
@AllArgsConstructor
public class EssayService {
    private final EssayRepository essayRepository;
    private final ClassifyRepository classifyRepository;
    private final CommentRepository commentRepository;
    private final CustomizeRepository customizeRepository;
    private final BookService bookService;
    private final MailUtil mailUtil;
    private final RabbitTemplate rabbitTemplate;

    /**
     * 获取文章列表
     * @return List<Map<String,Object>>
     */
    public List<Map<String,Object>> getEssayList(int page,int pageSize){
        String sql = "SELECT t.id,t.title,t.synopsis,t3.username,t2.name,t.created_at,t.updated_at,t.read_num " +
                "FROM essay t,classify t2,user t3 WHERE t.classify_id=t2.id AND t.user_id=t3.id " +
                "AND (t.state=3 OR t.state=4) order by t.updated_at desc LIMIT ?,?";
        return customizeRepository.nativeQueryListMap(sql,(page-1)*pageSize,pageSize);
    }

    /**
     * 获取用户的文章列表
     * @param pageable 分页
     * @param userId 用户ID
     * @return List<Essay>
     */
    public List<Essay> getUserEssayList(Pageable pageable,Long userId){
        Page<Essay> essayList = essayRepository.findEssayByUserId(userId,pageable);
        return essayList.getContent();
    }

    /**
     * 根据ID获取文章详情
     * @param id 文章ID
     * @return Essay
     */
    public Essay getEssayDetail(Long id){
        return essayRepository.getReferenceById(id);
    }

    /**
     * 文章阅读数+1
     */
    @Transactional(rollbackFor = Exception.class)
    public void readEssay(Long id){
        essayRepository.readOne(id);
    }

    /**
     * 用户写文章
     * @param essay 文章内容
     * @return id
     */
    @Transactional(rollbackFor = Exception.class)
    public Long createEssay(Long classifyId, Essay essay){
        Classify classify = classifyRepository.getReferenceById(classifyId);
        essay.setClassify(classify);
        essay.setReadNum(0);
        essay.setState(EssayState.DRAFT.getCode());
        return essayRepository.save(essay).getId();
    }

    /**
     * 用户修改文章
     * @param essay 文章内容
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateEssay(Long classifyId, Essay essay){
        essay.setState(EssayState.PENDING.getCode());
        Classify classify = classifyRepository.getReferenceById(classifyId);
        essay.setClassify(classify);
        essay.setReadNum(0);
        essayRepository.save(essay);
    }

    /**
     * 用户删除文章
     * @param userId  用户ID
     * @param essayId 文章ID
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean delUserEssay(Long userId,Long essayId){
        int num = essayRepository.deleteEssayByIdAndUserId(essayId,userId);
        return num>0;
    }

    /**
     * 根据条件获取文章列表
     * @param pageable 分页
     * @param essay  条件
     * @return Page<Essay>
     */
    public Page<Essay> getEssayList(Pageable pageable, Essay essay){
        return essayRepository.findAll(EssayRepository.getEssayList(essay),pageable);
    }

    /**
     * 审查文章
     * @param essay 文章
     */
    @Transactional(rollbackFor = Exception.class)
    public void altEssayState(Essay essay){
        Essay essay1 = essayRepository.getReferenceById(essay.getId());
        essay1.setState(essay.getState());
        //审核不通过
        if(EssayState.FORBIDDEN.getCode() == essay.getState()){
            essay1.setRemark(essay.getRemark());
            News news = new News();
            news.setUserId(essay1.getUser().getId());
            news.setContent(essay.getRemark());
            news.setCreateTime(LocalDateTime.now());
            news.setSenderId(1L);
            news.setSendTime(LocalDateTime.now());
            //在线消息通知 由于后台服务与api服务分开部署 无法在此直接发送websocket消息 所以通过Rabbit转发
            rabbitTemplate.convertAndSend(RabbitConfig.NOTIFY_QUEUE,news);
            //邮件通知作者
            mailUtil.sendSimpleMail(essay1.getUser().getEmail(),"文章审核不通过",essay1.getTitle()+"审核失败，理由： "+essay.getRemark());
        }else{
            Book book = new Book();
            book.setId(essay1.getId());
            book.setAuthor(essay1.getUser().getUsername());
            book.setTitle(essay1.getTitle());
            book.setContent(essay1.getContent());
            bookService.save(book);
        }
    }

    /**
     * 获取文章评论
     * @param id     文章Id
     * @param page   页数
     */
    public Page<Map<String,Object>> getComments(Long id,int page){
        return commentRepository.selectComments(id,PageRequest.of(page-1,20));
    }

    /**
     * 评论文章
     * @param userId 用户Id
     * @param comment 评论内容
     */
    public void addComments(Long userId,Comment comment){
        comment.setUserId(userId);
        commentRepository.save(comment);
    }

    /**
     * 获取用户 点赞/收藏 文章
     * @param userId 用户Id
     * @param faveType 类型
     * @return Page<Essay>
     */
    public Page<Essay> getUserFavesEssay(Long userId,Byte faveType,Pageable pageable){
        return essayRepository.findUserFaveEssay(userId,faveType,pageable);
    }

    /**
     * 初始化ES数据 仅供测试使用
     */
    public void initBookDataTest(){
        List<Essay> essayList = essayRepository.findAll();
        Book book;
        for(Essay essay:essayList){
            book = new Book();
            book.setId(essay.getId());
            book.setTitle(essay.getTitle());
            book.setAuthor(essay.getUser().getUsername());
            book.setContent(essay.getContent());
            bookService.save(book);
        }
    }
}
