package com.cn;

import com.cn.dao.ClassifyRepository;
import com.cn.entity.Classify;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author ngcly
 */
@Service
@AllArgsConstructor
public class ClassifyService {
    private final ClassifyRepository classifyRepository;

    /**
     * 根据条件获取分类列表
     * @param pageable 分页
     * @param classify 条件
     * @return Page<Classify>
     */
    public Page<Classify> getClassifyList(Pageable pageable, Classify classify){
        return classifyRepository.findAll(ClassifyRepository.getClassifyList(classify.getName()),pageable);
    }

    /**
     * 获取所有分类列表
     * @return List<Classify>
     */
    public List<Classify> getClassifyList(){
        return classifyRepository.findAll();
    }

    /**
     * 获取分类详情
     * @param id 主键
     * @return Classify
     */
    public Classify getClassifyDetail(Long id){
        return classifyRepository.getById(id);
    }

    /**
     * 保存分类
     * @param classify 分类内容
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveClassify(Classify classify){
        classifyRepository.save(classify);
    }

    /**
     * 根据id删除分类
     * @param id 主键
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteClassify(Long id){
        classifyRepository.deleteById(id);
    }

}
