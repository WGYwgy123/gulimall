package com.wgy.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wgy.common.utils.PageUtils;
import com.wgy.gulimall.ware.entity.WareSkuEntity;

import java.util.Map;

/**
 * 商品库存
 *
 * @author wugaoyao
 * @email wugaoyao@gmail.com
 * @date 2020-12-13 15:01:33
 */
public interface WareSkuService extends IService<WareSkuEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void addStock(Long skuId, Long wareId, Integer skuNum);
}

