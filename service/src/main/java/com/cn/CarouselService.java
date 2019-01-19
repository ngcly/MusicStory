package com.cn;

import com.cn.dao.CarouselRepository;
import com.cn.entity.Carousel;
import com.cn.entity.CarouselCategory;
import com.cn.util.UploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CarouselService {
    @Autowired
    CarouselRepository carouselRepository;

    /**
     * 根据条件获取轮播图列表
     */
    public Page<CarouselCategory> getCarouselList(String name, Pageable pageable){
        return carouselRepository.findAll(CarouselRepository.getNoticeList(name),pageable);
    }

    /**
     * 获取轮播图详情
     * @param id 主键
     */
    public CarouselCategory getCarouselDetail(String id){
        return carouselRepository.getOne(id);
    }

    /**
     * 新增修改轮播图
     * @param carouselCategory 轮播图
     */
    @Transactional
    public void addOrUpdateCarousel(CarouselCategory carouselCategory){
        carouselRepository.save(carouselCategory);
    }

    /**
     * 删除轮播分类
     * @param id 唯一标识
     */
    @Transactional
    public void deleteCarouselCategory(String id){
        CarouselCategory carouselCategory = carouselRepository.getOne(id);
        for(Carousel carousel:carouselCategory.getCarousels()){
            UploadUtil.deleteFileByAli(carousel.getImageUrl());
        }
        carouselRepository.deleteById(id);
    }

    /**
     * 删除轮播图
     * @param id 主键
     * @param url 图片地址
     */
    @Transactional
    public void deleteCarousel(String id,String url){
        UploadUtil.deleteFileByAli(url);
        carouselRepository.deleteCarousel(id);
    }
}
