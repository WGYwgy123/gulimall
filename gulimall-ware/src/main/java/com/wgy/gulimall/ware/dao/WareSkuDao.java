package com.wgy.gulimall.ware.dao;

import com.wgy.gulimall.ware.entity.WareSkuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品库存
 * 
 * @author wugaoyao
 * @email wugaoyao@gmail.com
 * @date 2020-12-13 15:01:33
 */
@Mapper
public interface WareSkuDao extends BaseMapper<WareSkuEntity> {
	
}
