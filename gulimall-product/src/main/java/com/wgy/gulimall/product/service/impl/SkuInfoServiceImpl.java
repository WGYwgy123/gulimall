package com.wgy.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wgy.common.utils.PageUtils;
import com.wgy.common.utils.Query;

import com.wgy.gulimall.product.dao.SkuInfoDao;
import com.wgy.gulimall.product.entity.SkuInfoEntity;
import com.wgy.gulimall.product.service.SkuInfoService;


@Service("skuInfoService")
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoDao, SkuInfoEntity> implements SkuInfoService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                new QueryWrapper<SkuInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageCondition(Map<String, Object> params) {
        final String key = (String) params.get("key");
        final String catelogId = (String) params.get("catelogId");
        final String brandId = (String) params.get("brandId");
        final String min = (String) params.get("min");
        final String max = (String) params.get("max");
        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                Wrappers.<SkuInfoEntity>lambdaQuery()
                        .and(StringUtils.isNotBlank(key), wrapper -> wrapper.eq(SkuInfoEntity::getSkuId, key)
                                .or()
                                .like(SkuInfoEntity::getSkuName, key))
                        .eq(StringUtils.isNotBlank(catelogId) && !catelogId.equals("0"), SkuInfoEntity::getCatalogId, catelogId)
                        .eq(StringUtils.isNotBlank(brandId) && !brandId.equals("0"), SkuInfoEntity::getBrandId, brandId)
                        .between((StringUtils.isNotBlank(min) && new BigDecimal(min).compareTo(new BigDecimal("0")) == 1) && (StringUtils.isNotBlank(max) && new BigDecimal(max).compareTo(new BigDecimal("0")) == 1), SkuInfoEntity::getPrice, min, max)
        );

        return new PageUtils(page);
    }

}