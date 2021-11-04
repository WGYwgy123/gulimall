package com.wgy.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wgy.common.to.SkuReductionTo;
import com.wgy.common.to.SpuBoundTo;
import com.wgy.gulimall.product.entity.*;
import com.wgy.gulimall.product.feign.CouponFeignService;
import com.wgy.gulimall.product.service.*;
import com.wgy.gulimall.product.vo.*;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wgy.common.utils.PageUtils;
import com.wgy.common.utils.Query;

import com.wgy.gulimall.product.dao.SpuInfoDao;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {

    @Resource
    private SpuInfoDescService spuInfoDescService;

    @Resource
    private SpuImagesService spuImagesService;

    @Resource
    private AttrService attrService;

    @Resource
    private ProductAttrValueService productAttrValueService;

    @Resource
    private SkuInfoService skuInfoService;

    @Resource
    private SkuImagesService skuImagesService;

    @Resource
    private SkuSaleAttrValueService skuSaleAttrValueService;

    @Resource
    private CouponFeignService couponFeignService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<SpuInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveSpuInfo(SpuSaveVo vo) {
        // 1. 保存spu基本信息 pms_spu_info
        final SpuInfoEntity spuInfoEntity = new SpuInfoEntity();
        BeanUtils.copyProperties(vo, spuInfoEntity);
        // 这完全可以用MP的自动注入来注入时间
        spuInfoEntity.setCreateTime(new Date());
        spuInfoEntity.setUpdateTime(new Date());
        // 保存基本数据
        saveBaseSpuInfo(spuInfoEntity);
        // 2. 保存spu描述图片 pms_spu_info_desc
        final List<String> spuImageDescList = vo.getDecript();
        if (CollectionUtils.isNotEmpty(spuImageDescList)) {
            final SpuInfoDescEntity spuInfoDescEntity = new SpuInfoDescEntity();
            spuInfoDescEntity.setSpuId(spuInfoEntity.getId());
            spuInfoDescEntity.setDecript(String.join(",", spuImageDescList));
            // 保存spu描述图片
            spuInfoDescService.save(spuInfoDescEntity);
        }
        // 3. 保存spu图片集 pms_spu_images
        final List<String> images = vo.getImages();
        spuImagesService.saveImages(spuInfoEntity.getId(), images);
        // 4. 保存spu规格参数 pms_product_attr_value
        final List<BaseAttrs> baseAttrs = vo.getBaseAttrs();
        final List<ProductAttrValueEntity> valueEntityList = baseAttrs.stream().map(attr -> {
            final ProductAttrValueEntity productAttrValueEntity = new ProductAttrValueEntity();
            productAttrValueEntity.setAttrId(attr.getAttrId());
            final AttrEntity attrEntity = attrService.getById(attr.getAttrId());
            productAttrValueEntity.setAttrName(attrEntity.getAttrName());
            productAttrValueEntity.setAttrValue(attr.getAttrValues());
            productAttrValueEntity.setQuickShow(attr.getShowDesc());
            productAttrValueEntity.setSpuId(spuInfoEntity.getId());
            return productAttrValueEntity;
        }).collect(Collectors.toList());
        productAttrValueService.saveBatch(valueEntityList);
        // 5. 保存spu积分信息 gulimall_sms->sms_spu_bounds
        final Bounds bounds = vo.getBounds();
        final SpuBoundTo spuBoundTo = new SpuBoundTo();
        BeanUtils.copyProperties(bounds, spuBoundTo);
        spuBoundTo.setSpuId(spuInfoEntity.getId());
        couponFeignService.saveSpuBounds(spuBoundTo);
        // 6. 保存当前spu对应的所有sku信息
        final List<Skus> skus = vo.getSkus();
        if (CollectionUtils.isNotEmpty(skus)) {
            skus.forEach(sku -> {
                String defaultImage = "";
                final List<Images> imagesList = sku.getImages()
                        .stream()
                        .filter(image -> image.getDefaultImg() == 1)
                        .collect(Collectors.toList());
                if (imagesList.size() == 1) {
                    defaultImage = imagesList.get(0).getImgUrl();
                }
                final SkuInfoEntity skuInfoEntity = new SkuInfoEntity();
                BeanUtils.copyProperties(sku, skuInfoEntity);
                skuInfoEntity.setBrandId(spuInfoEntity.getBrandId());
                skuInfoEntity.setCatalogId(spuInfoEntity.getCatalogId());
                skuInfoEntity.setSaleCount(0L);
                skuInfoEntity.setSpuId(spuInfoEntity.getId());
                skuInfoEntity.setSkuDefaultImg(defaultImage);
                // 6.1 保存sku基本信息 pms_sku_info
                skuInfoService.save(skuInfoEntity);

                final Long skuId = skuInfoEntity.getSkuId();
                final List<Images> skuImages = sku.getImages();
                final List<SkuImagesEntity> imagesEntityList = skuImages.stream().map(item -> {
                    final SkuImagesEntity skuImagesEntity = new SkuImagesEntity();
                    skuImagesEntity.setSkuId(skuId);
                    skuImagesEntity.setImgUrl(item.getImgUrl());
                    skuImagesEntity.setDefaultImg(item.getDefaultImg());
                    return skuImagesEntity;
                }).filter(entity -> StringUtils.isNotBlank(entity.getImgUrl())).collect(Collectors.toList());
                // 6.2 保存sku图片信息 pms_sku_images
                skuImagesService.saveBatch(imagesEntityList);
                // TODO 没有图片，路径无需存储

                // 6.3 保存sku销售属性信息 pms_sku_sale_attr_value
                final List<Attr> attr = sku.getAttr();
                final List<SkuSaleAttrValueEntity> attrValueEntityList = attr.stream().map(item -> {
                    final SkuSaleAttrValueEntity skuSaleAttrValueEntity = new SkuSaleAttrValueEntity();
                    BeanUtils.copyProperties(item, skuSaleAttrValueEntity);
                    skuSaleAttrValueEntity.setSkuId(skuId);
                    return skuSaleAttrValueEntity;
                }).collect(Collectors.toList());
                skuSaleAttrValueService.saveBatch(attrValueEntityList);

                // 6.4 保存sku优惠满减信息 gulimall_sms->sms_sku_ladder/sms_sku_full_reduction/sms_member_price
                final SkuReductionTo skuReductionTo = new SkuReductionTo();
                BeanUtils.copyProperties(sku, skuReductionTo);
                skuReductionTo.setSkuId(skuId);
                if (skuReductionTo.getFullCount() > 0 || skuReductionTo.getFullPrice().compareTo(new BigDecimal("0")) == 1) {
                    couponFeignService.saveSkuReduction(skuReductionTo);
                }
            });
        }
    }

    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {
        final String key = (String) params.get("key");
        final String status = (String) params.get("status");
        final String brandId = (String) params.get("brandId");
        final String catalogId = (String) params.get("catalogId");
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                Wrappers.<SpuInfoEntity>lambdaQuery()
                        .and(StringUtils.isNotBlank(key), wrapper -> wrapper.eq(SpuInfoEntity::getId, key)
                                .or()
                                .like(SpuInfoEntity::getSpuName,key))
                        .eq(StringUtils.isNotBlank(status),SpuInfoEntity::getPublishStatus, status)
                        .eq(StringUtils.isNotBlank(brandId),SpuInfoEntity::getBrandId, brandId)
                        .eq(StringUtils.isNotBlank(catalogId),SpuInfoEntity::getCatalogId, catalogId)
        );
        return new PageUtils(page);
    }

    private void saveBaseSpuInfo(SpuInfoEntity spuInfoEntity) {
        save(spuInfoEntity);
    }

}