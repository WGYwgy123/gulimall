package com.wgy.gulimall.product.dao;

import com.wgy.gulimall.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author wugaoyao
 * @email wugaoyao@gmail.com
 * @date 2020-12-10 20:44:49
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
