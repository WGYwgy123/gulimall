package com.wgy.gulimall.coupon.dao;

import com.wgy.gulimall.coupon.entity.CouponEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券信息
 * 
 * @author wugaoyao
 * @email wugaoyao@gmail.com
 * @date 2020-12-13 14:41:16
 */
@Mapper
public interface CouponDao extends BaseMapper<CouponEntity> {
	
}
