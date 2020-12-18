package com.wgy.gulimall.order.dao;

import com.wgy.gulimall.order.entity.RefundInfoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 退款信息
 * 
 * @author wugaoyao
 * @email wugaoyao@gmail.com
 * @date 2020-12-13 14:55:52
 */
@Mapper
public interface RefundInfoDao extends BaseMapper<RefundInfoEntity> {
	
}
