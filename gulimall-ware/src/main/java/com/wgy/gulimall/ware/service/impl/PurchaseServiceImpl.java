package com.wgy.gulimall.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wgy.common.constant.WareConstant;
import com.wgy.common.utils.PageUtils;
import com.wgy.common.utils.Query;
import com.wgy.gulimall.ware.dao.PurchaseDao;
import com.wgy.gulimall.ware.entity.PurchaseDetailEntity;
import com.wgy.gulimall.ware.entity.PurchaseEntity;
import com.wgy.gulimall.ware.service.PurchaseDetailService;
import com.wgy.gulimall.ware.service.PurchaseService;
import com.wgy.gulimall.ware.service.WareSkuService;
import com.wgy.gulimall.ware.vo.ItemVo;
import com.wgy.gulimall.ware.vo.MergeVo;
import com.wgy.gulimall.ware.vo.PurchaseFinishVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("purchaseService")
public class PurchaseServiceImpl extends ServiceImpl<PurchaseDao, PurchaseEntity> implements PurchaseService {

    @Resource
    private PurchaseDetailService purchaseDetailService;

    @Resource
    private WareSkuService wareSkuService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageUnreceivePurchase(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                Wrappers.<PurchaseEntity>lambdaQuery()
                        .eq(PurchaseEntity::getStatus, 0)
                        .or()
                        .eq(PurchaseEntity::getStatus, 1)
        );

        return new PageUtils(page);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void mergePurchase(MergeVo mergeVo) {
        Long purchaseId = mergeVo.getPurchaseId();
        if (ObjectUtils.isEmpty(mergeVo.getPurchaseId())) {
            final PurchaseEntity purchaseEntity = new PurchaseEntity();
            purchaseEntity.setCreateTime(new Date());
            purchaseEntity.setUpdateTime(new Date());
            purchaseEntity.setStatus(WareConstant.PurchaseStatusEum.CREATED.getCode());
            save(purchaseEntity);
            purchaseId = purchaseEntity.getId();
        }
        // TODO 确认采购单状态是0，1才可以合并
        final List<Long> items = mergeVo.getItems();
        Long finalPurchaseId = purchaseId;
        final List<PurchaseDetailEntity> detailEntityList = items.stream().map(item -> {
            final PurchaseDetailEntity purchaseDetailEntity = new PurchaseDetailEntity();
            purchaseDetailEntity.setId(item);
            purchaseDetailEntity.setPurchaseId(finalPurchaseId);
            purchaseDetailEntity.setStatus(WareConstant.PurchaseDetailStatusEum.ASSIGNED.getCode());
            return purchaseDetailEntity;
        }).collect(Collectors.toList());
        purchaseDetailService.updateBatchById(detailEntityList);
        final PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseEntity.setId(purchaseId);
        purchaseEntity.setUpdateTime(new Date());
        updateById(purchaseEntity);
    }

    @Override
    public void receivedPurchase(List<Long> ids) {
        final List<PurchaseEntity> entityList = ids.stream().map(id -> {
            final PurchaseEntity purchaseEntity = getById(id);
            return purchaseEntity;
        }).filter(item -> item.getStatus().equals(WareConstant.PurchaseStatusEum.CREATED.getCode()) || item.getStatus().equals(WareConstant.PurchaseStatusEum.ASSIGNED.getCode()))
                .peek(item -> {
                    item.setStatus(WareConstant.PurchaseStatusEum.RECEIVED.getCode());
                    item.setUpdateTime(new Date());
                })
                .collect(Collectors.toList());
        updateBatchById(entityList);

        entityList.forEach(purchaseEntity -> {
            List<PurchaseDetailEntity> purchaseDetailEntityList = purchaseDetailService.listDetailByPurchaseId(purchaseEntity.getId());
            final List<PurchaseDetailEntity> detailEntityList = purchaseDetailEntityList.stream()
                    .peek(purchaseDetailEntity -> purchaseDetailEntity.setStatus(WareConstant.PurchaseDetailStatusEum.BUYING.getCode()))
                    .collect(Collectors.toList());
            purchaseDetailService.updateBatchById(detailEntityList);
        });
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void done(PurchaseFinishVo finishVo) {

        // 改变采购项状态
        Boolean flag = true;
        final List<ItemVo> items = finishVo.getItems();
        final List<PurchaseDetailEntity> detailEntities = new ArrayList<>();
        for (ItemVo item : items) {
            final PurchaseDetailEntity purchaseDetailEntity = new PurchaseDetailEntity();
            if(item.getStatus().equals(WareConstant.PurchaseDetailStatusEum.ERROR.getCode())) {
                flag = false;
                purchaseDetailEntity.setStatus(item.getStatus());
            } else {
                purchaseDetailEntity.setStatus(WareConstant.PurchaseDetailStatusEum.FINISH.getCode());
                // 成功的采购加入库存
                final PurchaseDetailEntity detailEntity = purchaseDetailService.getById(item.getItemId());
                wareSkuService.addStock(detailEntity.getSkuId(), detailEntity.getWareId(), detailEntity.getSkuNum());
            }
            purchaseDetailEntity.setId(item.getItemId());
            detailEntities.add(purchaseDetailEntity);
        }
        purchaseDetailService.updateBatchById(detailEntities);
        // 改变采购单状态
        final Long id = finishVo.getId();
        final PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseEntity.setId(id);
        purchaseEntity.setStatus(flag ? WareConstant.PurchaseStatusEum.FINISH.getCode() : WareConstant.PurchaseStatusEum.ERROR.getCode());
        purchaseEntity.setUpdateTime(new Date());
        updateById(purchaseEntity);


    }

}