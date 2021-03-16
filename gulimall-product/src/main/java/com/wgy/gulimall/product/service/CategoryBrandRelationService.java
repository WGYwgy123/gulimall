package com.wgy.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wgy.common.utils.PageUtils;
import com.wgy.gulimall.product.entity.BrandEntity;
import com.wgy.gulimall.product.entity.CategoryBrandRelationEntity;

import java.util.List;
import java.util.Map;

/**
 * 品牌分类关联
 *
 * @author wugaoyao
 * @email wugaoyao@gmail.com
 * @date 2020-12-10 20:44:49
 */
public interface CategoryBrandRelationService extends IService<CategoryBrandRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);

    Boolean saveDetail(CategoryBrandRelationEntity categoryBrandRelation);

    Boolean updateBrand(Long brandId, String name);

    void updateCategory(Long catId, String name);

    List<BrandEntity> getBrandsByCatId(Long catId);
}

