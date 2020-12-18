package com.wgy.gulimall.coupon.dao;

import com.wgy.gulimall.coupon.entity.MemberPriceEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品会员价格
 * 
 * @author wugaoyao
 * @email wugaoyao@gmail.com
 * @date 2020-12-13 14:41:16
 */
@Mapper
public interface MemberPriceDao extends BaseMapper<MemberPriceEntity> {
	
}
