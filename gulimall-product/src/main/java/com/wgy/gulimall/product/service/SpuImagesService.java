package com.wgy.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wgy.common.utils.PageUtils;
import com.wgy.gulimall.product.entity.SpuImagesEntity;

import java.util.List;
import java.util.Map;

/**
 * spu图片
 *
 * @author wugaoyao
 * @email wugaoyao@gmail.com
 * @date 2020-12-10 20:44:48
 */
public interface SpuImagesService extends IService<SpuImagesEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveImages(Long spuId, List<String> images);
}

