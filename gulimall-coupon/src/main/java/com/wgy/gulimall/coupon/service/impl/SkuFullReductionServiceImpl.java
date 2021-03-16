package com.wgy.gulimall.coupon.service.impl;

import com.wgy.common.to.MemberPrice;
import com.wgy.common.to.SkuReductionTo;
import com.wgy.gulimall.coupon.entity.MemberPriceEntity;
import com.wgy.gulimall.coupon.entity.SkuLadderEntity;
import com.wgy.gulimall.coupon.service.MemberPriceService;
import com.wgy.gulimall.coupon.service.SkuLadderService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wgy.common.utils.PageUtils;
import com.wgy.common.utils.Query;

import com.wgy.gulimall.coupon.dao.SkuFullReductionDao;
import com.wgy.gulimall.coupon.entity.SkuFullReductionEntity;
import com.wgy.gulimall.coupon.service.SkuFullReductionService;

import javax.annotation.Resource;


@Service("skuFullReductionService")
public class SkuFullReductionServiceImpl extends ServiceImpl<SkuFullReductionDao, SkuFullReductionEntity> implements SkuFullReductionService {

    @Resource
    private SkuLadderService skuLadderService;

    @Resource
    private MemberPriceService memberPriceService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuFullReductionEntity> page = this.page(
                new Query<SkuFullReductionEntity>().getPage(params),
                new QueryWrapper<SkuFullReductionEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveSkuReduction(SkuReductionTo skuReductionTo) {
        // 保存sku优惠满减信息 gulimall_sms->sms_sku_ladder/sms_sku_full_reduction/sms_member_price
        final SkuLadderEntity skuLadderEntity = new SkuLadderEntity();
        skuLadderEntity.setSkuId(skuReductionTo.getSkuId());
        skuLadderEntity.setFullCount(skuReductionTo.getFullCount());
        skuLadderEntity.setAddOther(skuReductionTo.getCountStatus());
        skuLadderEntity.setDiscount(skuReductionTo.getDiscount());
        if (skuLadderEntity.getFullCount() > 0) {
            skuLadderService.save(skuLadderEntity);
        }

        final SkuFullReductionEntity skuFullReductionEntity = new SkuFullReductionEntity();
        BeanUtils.copyProperties(skuReductionTo, skuFullReductionEntity);
        if (skuFullReductionEntity.getFullPrice().compareTo(new BigDecimal("0")) == 1) {
            save(skuFullReductionEntity);
        }

        final List<MemberPrice> memberPrice = skuReductionTo.getMemberPrice();
        final List<MemberPriceEntity> priceEntityList = memberPrice.stream().map(item -> {
            final MemberPriceEntity memberPriceEntity = new MemberPriceEntity();
            memberPriceEntity.setSkuId(skuReductionTo.getSkuId());
            memberPriceEntity.setMemberLevelId(item.getId());
            memberPriceEntity.setMemberLevelName(item.getName());
            memberPriceEntity.setMemberPrice(item.getPrice());
            memberPriceEntity.setAddOther(1);
            return memberPriceEntity;
        }).filter(item -> item.getMemberPrice().compareTo(new BigDecimal("0")) == 1).collect(Collectors.toList());
        memberPriceService.saveBatch(priceEntityList);
    }

}