package com.cn;

import com.cn.dao.EssayRepository;
import com.cn.util.RestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
     * 根据ID获取文章详情
     * @param id
     * @return
     */
    public ModelMap getEssayDetail(String id){
        return RestUtil.Success(essayRepository.getById(id));
    }
}
