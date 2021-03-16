package com.wgy.gulimall.product.dao;

import com.wgy.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 属性&属性分组关联
 * 
 * @author wugaoyao
 * @email wugaoyao@gmail.com
 * @date 2020-12-10 20:44:48
 */
@Mapper
public interface AttrAttrgroupRelationDao extends BaseMapper<AttrAttrgroupRelationEntity> {

    void deleteBatchRelation(@Param("relationEntityList") List<AttrAttrgroupRelationEntity> relationEntityList);
}
