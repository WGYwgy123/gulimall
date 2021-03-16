package com.wgy.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wgy.gulimall.product.entity.CategoryBrandRelationEntity;
import com.wgy.gulimall.product.service.CategoryBrandRelationService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wgy.common.utils.PageUtils;
import com.wgy.common.utils.Query;

import com.wgy.gulimall.product.dao.BrandDao;
import com.wgy.gulimall.product.entity.BrandEntity;
import com.wgy.gulimall.product.service.BrandService;

import javax.annotation.Resource;


@Service("brandService")
public class BrandServiceImpl extends ServiceImpl<BrandDao, BrandEntity> implements BrandService {

    @Resource
    private CategoryBrandRelationService categoryBrandRelationService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        final String key = (String) params.get("key");
        IPage<BrandEntity> page = this.page(
                new Query<BrandEntity>().getPage(params),
                new LambdaQueryWrapper<BrandEntity>()
                        .eq(StringUtils.isNotBlank(key), BrandEntity::getBrandId, key)
                        .or()
                        .like(StringUtils.isNotBlank(key), BrandEntity::getName, key)
        );

        return new PageUtils(page);
    }

    @Override
    public Boolean updateDetail(BrandEntity brand) {
        updateById(brand);
        if (StringUtils.isNotBlank(brand.getName())) {
            categoryBrandRelationService.updateBrand(brand.getBrandId(), brand.getName());
        }
        return null;
    }

}