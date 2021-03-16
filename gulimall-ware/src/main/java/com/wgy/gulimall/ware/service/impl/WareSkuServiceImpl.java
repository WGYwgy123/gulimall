package com.wgy.gulimall.ware.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wgy.common.utils.R;
import com.wgy.gulimall.ware.feign.ProductFeignService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wgy.common.utils.PageUtils;
import com.wgy.common.utils.Query;

import com.wgy.gulimall.ware.dao.WareSkuDao;
import com.wgy.gulimall.ware.entity.WareSkuEntity;
import com.wgy.gulimall.ware.service.WareSkuService;

import javax.annotation.Resource;


@Service("wareSkuService")
public class WareSkuServiceImpl extends ServiceImpl<WareSkuDao, WareSkuEntity> implements WareSkuService {

    @Resource
    private ProductFeignService productFeignService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        final String skuId = (String) params.get("skuId");
        final String wareId = (String) params.get("wareId");
        IPage<WareSkuEntity> page = this.page(
                new Query<WareSkuEntity>().getPage(params),
                Wrappers.<WareSkuEntity>lambdaQuery()
                        .eq(StringUtils.isNotBlank(skuId), WareSkuEntity::getSkuId, skuId)
                        .eq(StringUtils.isNotBlank(wareId), WareSkuEntity::getWareId, wareId)
        );

        return new PageUtils(page);
    }

    @Override
    public void addStock(Long skuId, Long wareId, Integer skuNum) {
        final List<WareSkuEntity> list = list(Wrappers.<WareSkuEntity>lambdaQuery()
                .eq(WareSkuEntity::getSkuId, skuId)
                .eq(WareSkuEntity::getWareId, wareId));
        if (CollectionUtils.isEmpty(list)) {
            final WareSkuEntity wareSkuEntity = new WareSkuEntity();
            wareSkuEntity.setSkuId(skuId);
            wareSkuEntity.setStock(skuNum);
            wareSkuEntity.setWareId(wareId);
            // TODO 远程查询商品名字 失败不回滚
            // 1. 自己catch异常
            // TODO 异常出现不回滚
            try {
                final R info = productFeignService.info(skuId);
                final Map<String, Object> skuInfo = (Map<String, Object>) info.get("skuInfo");
                wareSkuEntity.setSkuName((String) skuInfo.get("skuName"));
            } catch (Exception e) {

            }
            wareSkuEntity.setStockLocked(0);
            save(wareSkuEntity);
        } else {
            baseMapper.addStock(skuId, wareId, skuNum);
        }
    }

}