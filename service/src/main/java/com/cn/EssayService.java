package com.cn;

import com.cn.dao.EssayRepository;
import com.cn.entity.Essay;
import com.cn.util.RestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;

@Service
public class EssayService {
    @Autowired
    EssayRepository essayRepository;

    /**
     * 获取文章列表
     * @return
     */
    public ModelMap getEssayList(int page,int pageSize){
        return RestUtil.Success(essayRepository.getEssayList(page,pageSize));
    }

    /**
     * 获取用户的文章列表
     * @param pageable 分页
     * @param userId 用户ID
     * @return
     */
    public ModelMap getUserEssayList(Pageable pageable,String userId){
        Page<Essay> essayList = essayRepository.findAll(pageable);
        return RestUtil.Success(essayList.getContent());
    }

    /**
     * 根据ID获取文章详情
     * @param id 文章ID
     * @return
     */
    public ModelMap getEssayDetail(String id){
        return RestUtil.Success(essayRepository.getById(id));
    }

    /**
     * 用户删除文章
     * @param userId  用户ID
     * @param essayId 文章ID
     */
    @Transactional
    public void delUserEssay(String userId,String essayId){
        essayRepository.deleteEssayByIdAndUserId(essayId,userId);
    }
}
