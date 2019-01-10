package com.cn;

import com.cn.dao.CarouselRepository;
import com.cn.entity.Carousel;
import com.cn.util.UploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CarouselService {
    @Autowired
    CarouselRepository carouselRepository;

    /**
     * 获取轮播图详情
     * @param id 主键
     */
    public Carousel getCarouselDetail(String id){
        return carouselRepository.getOne(id);
    }

    /**
     * 新增修改公告
     * @param carousel 轮播图
     */
    @Transactional
    public void addOrUpdateCarousel(Carousel carousel){
        carouselRepository.save(carousel);
    }

    /**
     * 删除轮播图
     * @param id 主键
     * @param url 图片地址
     */
    @Transactional
    public void deleteCarousel(String id,String url){
        UploadUtil.deleteFileByAli(url);
        carouselRepository.deleteById(id);
    }
}
