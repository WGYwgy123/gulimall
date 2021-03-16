package com.wgy.gulimall.ware.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wgy.common.utils.PageUtils;
import com.wgy.common.utils.Query;

import com.wgy.gulimall.ware.dao.PurchaseDetailDao;
import com.wgy.gulimall.ware.entity.PurchaseDetailEntity;
import com.wgy.gulimall.ware.service.PurchaseDetailService;


@Service("purchaseDetailService")
public class PurchaseDetailServiceImpl extends ServiceImpl<PurchaseDetailDao, PurchaseDetailEntity> implements PurchaseDetailService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        final String key = (String) params.get("key");
        final String status = (String) params.get("status");
        final String wareId = (String) params.get("wareId");
        IPage<PurchaseDetailEntity> page = this.page(
                new Query<PurchaseDetailEntity>().getPage(params),
                Wrappers.<PurchaseDetailEntity>lambdaQuery()
                        .and(StringUtils.isNotBlank(key), wrapper-> wrapper.eq(PurchaseDetailEntity::getSkuId, key)
                                .or().eq(PurchaseDetailEntity::getPurchaseId, key))
                        .eq(StringUtils.isNotBlank(status), PurchaseDetailEntity::getStatus, status)
                        .eq(StringUtils.isNotBlank(wareId), PurchaseDetailEntity::getWareId, wareId)
        );

        return new PageUtils(page);
    }

    @Override
    public List<PurchaseDetailEntity> listDetailByPurchaseId(Long id) {
        return list(Wrappers.<PurchaseDetailEntity>lambdaQuery().eq(PurchaseDetailEntity::getPurchaseId, id));
    }

}