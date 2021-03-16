package com.wgy.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wgy.common.utils.PageUtils;
import com.wgy.gulimall.ware.entity.PurchaseDetailEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author wugaoyao
 * @email wugaoyao@gmail.com
 * @date 2020-12-13 15:01:33
 */
public interface PurchaseDetailService extends IService<PurchaseDetailEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<PurchaseDetailEntity> listDetailByPurchaseId(Long id);
}

