package com.wgy.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wgy.gulimall.product.entity.BrandEntity;
import com.wgy.gulimall.product.entity.CategoryEntity;
import com.wgy.gulimall.product.service.BrandService;
import com.wgy.gulimall.product.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wgy.common.utils.PageUtils;
import com.wgy.common.utils.Query;

import com.wgy.gulimall.product.dao.CategoryBrandRelationDao;
import com.wgy.gulimall.product.entity.CategoryBrandRelationEntity;
import com.wgy.gulimall.product.service.CategoryBrandRelationService;

import javax.annotation.Resource;


@Service("categoryBrandRelationService")
public class CategoryBrandRelationServiceImpl extends ServiceImpl<CategoryBrandRelationDao, CategoryBrandRelationEntity> implements CategoryBrandRelationService {

    @Resource
    private BrandService brandService;

    @Resource
    private CategoryService categoryService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryBrandRelationEntity> page = this.page(
                new Query<CategoryBrandRelationEntity>().getPage(params),
                new QueryWrapper<CategoryBrandRelationEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public Boolean saveDetail(CategoryBrandRelationEntity categoryBrandRelation) {
        final Long brandId = categoryBrandRelation.getBrandId();
        final Long catelogId = categoryBrandRelation.getCatelogId();
        final BrandEntity brandEntity = brandService.getById(brandId);
        final CategoryEntity categoryEntity = categoryService.getById(catelogId);
        categoryBrandRelation.setBrandName(brandEntity.getName());
        categoryBrandRelation.setCatelogName(categoryEntity.getName());
        return save(categoryBrandRelation);
    }

    @Override
    public Boolean updateBrand(Long brandId, String name) {
        final CategoryBrandRelationEntity categoryBrandRelationEntity = new CategoryBrandRelationEntity();
        categoryBrandRelationEntity.setBrandId(brandId);
        categoryBrandRelationEntity.setBrandName(name);
        return update(categoryBrandRelationEntity, Wrappers.<CategoryBrandRelationEntity>lambdaUpdate().eq(CategoryBrandRelationEntity::getBrandId, brandId));
    }

    @Override
    public void updateCategory(Long catId, String name) {
        baseMapper.updateCategory(catId, name);
    }

    @Override
    public List<BrandEntity> getBrandsByCatId(Long catId) {
        final List<Long> longList = list(Wrappers.<CategoryBrandRelationEntity>lambdaQuery()
                .eq(CategoryBrandRelationEntity::getCatelogId, catId))
                .stream()
                .map(CategoryBrandRelationEntity::getBrandId)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(longList)) {
            return new ArrayList<>();
        }
        return brandService.listByIds(longList);
    }

}