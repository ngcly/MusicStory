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
        return carouselRepository.findById(id).orElse(null);
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
     * 添加轮播图
     * @param id  分类ID
     * @param url 图片路径
     */
    public void addCarousel(String id,String url){
        CarouselCategory carouselCategory = carouselRepository.getOne(id);
        Carousel carousel = new Carousel();
        carousel.setImageUrl(url);
        carousel.setSort(carouselCategory.getCarousels().size()+1);
        carouselCategory.getCarousels().add(carousel);
        carouselRepository.save(carouselCategory);
    }

    /**
     * 删除轮播分类
     * @param id 唯一标识
     */
    @Transactional
    public void deleteCarouselCategory(String id){
        CarouselCategory carouselCategory = carouselRepository.getOne(id);
        try {
            for(Carousel carousel:carouselCategory.getCarousels()){
                UploadUtil.deleteFileByAli(carousel.getImageUrl());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        carouselRepository.delete(carouselCategory);
    }

    /**
     * 删除轮播图
     * @param id 主键
     * @param url 图片地址
     */
    @Transactional
    public void deleteCarousel(String id,String url){
        try {
            UploadUtil.deleteFileByAli(url);
        }catch (Exception e){
            e.printStackTrace();
        }
        carouselRepository.deleteCarousel(id);
    }
}
