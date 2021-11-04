package com.wgy.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wgy.common.constant.ProductConstant;
import com.wgy.common.utils.PageUtils;
import com.wgy.common.utils.Query;
import com.wgy.gulimall.product.dao.AttrDao;
import com.wgy.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.wgy.gulimall.product.entity.AttrEntity;
import com.wgy.gulimall.product.entity.AttrGroupEntity;
import com.wgy.gulimall.product.entity.CategoryEntity;
import com.wgy.gulimall.product.service.AttrAttrgroupRelationService;
import com.wgy.gulimall.product.service.AttrGroupService;
import com.wgy.gulimall.product.service.AttrService;
import com.wgy.gulimall.product.service.CategoryService;
import com.wgy.gulimall.product.vo.AttrGroupRelationVo;
import com.wgy.gulimall.product.vo.AttrRespVo;
import com.wgy.gulimall.product.vo.AttrVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {

    @Resource
    private AttrAttrgroupRelationService attrAttrgroupRelationService;

    @Resource
    private AttrGroupService attrGroupService;

    @Resource
    private CategoryService categoryService;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                new QueryWrapper<AttrEntity>()
        );

        return new PageUtils(page);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveAttr(AttrVo attr) {
        final AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attr, attrEntity);
        save(attrEntity);
        if (attr.getAttrType() == ProductConstant.AttrEum.ATTR_TYPE_BASE.getCode() && attr.getAttrGroupId() != null) {
            final AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = new AttrAttrgroupRelationEntity();
            attrAttrgroupRelationEntity.setAttrGroupId(attr.getAttrGroupId());
            attrAttrgroupRelationEntity.setAttrId(attrEntity.getAttrId());
            attrAttrgroupRelationService.save(attrAttrgroupRelationEntity);
        }
    }

    @Override
    public PageUtils queryBaseAttrPage(Map<String, Object> params, Long catelogId, String attrType) {
        final String key = (String) params.get("key");
        // 如果and后面判断是空的话会有where() 会报错，在开始的时候得做判断
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                Wrappers.<AttrEntity>lambdaQuery()
                        .eq(ObjectUtils.isNotEmpty(catelogId) && catelogId != 0, AttrEntity::getCatelogId, catelogId)
                        .eq(AttrEntity::getAttrType, "base".equals(attrType) ? ProductConstant.AttrEum.ATTR_TYPE_BASE.getCode() : ProductConstant.AttrEum.ATTR_TYPE_SALE.getCode())
                        .and(StringUtils.isNotBlank(key), item -> item.eq(AttrEntity::getAttrId, key)
                                .or()
                                .like(AttrEntity::getAttrName, key))

        );
        final PageUtils pageUtils = new PageUtils(page);
        final List<AttrEntity> records = page.getRecords();
        final List<AttrRespVo> collect = records.stream().map(attrEntity -> {
            final AttrRespVo attrRespVo = new AttrRespVo();
            BeanUtils.copyProperties(attrEntity, attrRespVo);

            if ("base".equals(attrType)) {
                final AttrAttrgroupRelationEntity relationEntity = attrAttrgroupRelationService.getOne(Wrappers.<AttrAttrgroupRelationEntity>lambdaQuery()
                        .eq(AttrAttrgroupRelationEntity::getAttrId, attrEntity.getAttrId()));
                if (ObjectUtils.isNotEmpty(relationEntity) && ObjectUtils.isNotEmpty(relationEntity.getAttrId())) {
                    final AttrGroupEntity attrGroupEntity = attrGroupService.getById(relationEntity.getAttrGroupId());
                    attrRespVo.setGroupName(attrGroupEntity.getAttrGroupName());
                }
            }
            final CategoryEntity categoryEntity = categoryService.getById(attrEntity.getCatelogId());
            if (ObjectUtils.isNotEmpty(categoryEntity)) {
                attrRespVo.setCatelogName(categoryEntity.getName());
            }
            return attrRespVo;
        }).collect(Collectors.toList());
        pageUtils.setList(collect);
        return pageUtils;
    }

    @Override
    public AttrRespVo getAttrInfo(Long attrId) {
        final AttrRespVo attrRespVo = new AttrRespVo();
        final AttrEntity attrEntity = getById(attrId);
        BeanUtils.copyProperties(attrEntity, attrRespVo);

        // 设置分组信息
        if (attrEntity.getAttrType() == ProductConstant.AttrEum.ATTR_TYPE_BASE.getCode()) {
            final AttrAttrgroupRelationEntity attrAttrgroupRelationEntity =
                    attrAttrgroupRelationService.getOne(Wrappers.<AttrAttrgroupRelationEntity>lambdaQuery()
                    .eq(AttrAttrgroupRelationEntity::getAttrId, attrEntity.getAttrId()));
            if (ObjectUtils.isNotEmpty(attrAttrgroupRelationEntity)) {
                attrRespVo.setAttrGroupId(attrAttrgroupRelationEntity.getAttrGroupId());

                final AttrGroupEntity attrGroupEntity = attrGroupService.getById(attrAttrgroupRelationEntity.getAttrGroupId());
                if (ObjectUtils.isNotEmpty(attrGroupEntity)) {
                    attrRespVo.setGroupName(attrGroupEntity.getAttrGroupName());
                }
            }
        }
        // 分类信息
        final Long[] catelogPath = categoryService.findCatelogPath(attrEntity.getCatelogId());
        attrRespVo.setCatelogPath(catelogPath);
        final CategoryEntity categoryEntity = categoryService.getById(attrEntity.getCatelogId());
        if (ObjectUtils.isNotEmpty(categoryEntity)) {
            attrRespVo.setCatelogName(categoryEntity.getName());
        }
        return attrRespVo;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateAttr(AttrVo attr) {
        final AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attr, attrEntity);
        updateById(attrEntity);

        if (attrEntity.getAttrType() == ProductConstant.AttrEum.ATTR_TYPE_BASE.getCode()) {
            final AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = new AttrAttrgroupRelationEntity();
            attrAttrgroupRelationEntity.setAttrGroupId(attr.getAttrGroupId());
            attrAttrgroupRelationEntity.setAttrId(attr.getAttrId());
            final int count = attrAttrgroupRelationService.count(Wrappers.<AttrAttrgroupRelationEntity>lambdaQuery()
                    .eq(AttrAttrgroupRelationEntity::getAttrId, attr.getAttrId()));
            if (count > 0) {
                attrAttrgroupRelationService.update(attrAttrgroupRelationEntity,
                        Wrappers.<AttrAttrgroupRelationEntity>lambdaUpdate()
                                .eq(AttrAttrgroupRelationEntity::getAttrId, attr.getAttrId()));
            } else {
                attrAttrgroupRelationService.save(attrAttrgroupRelationEntity);
            }
        }
    }

    @Override
    public List<AttrEntity> getRelationAttr(Long attrgroupId) {
        final List<Long> collect = attrAttrgroupRelationService.list(Wrappers.<AttrAttrgroupRelationEntity>lambdaQuery()
                .eq(AttrAttrgroupRelationEntity::getAttrGroupId, attrgroupId))
                .stream()
                .map(AttrAttrgroupRelationEntity::getAttrId)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(collect)) {
            return new ArrayList<>();
        }
        return listByIds(collect);
    }

    @Override
    public void deleteRelation(List<AttrGroupRelationVo> relationVoList) {
        final List<AttrAttrgroupRelationEntity> collect = relationVoList.stream().map(item -> {
            final AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
            BeanUtils.copyProperties(item, relationEntity);
            return relationEntity;
        }).collect(Collectors.toList());

        attrAttrgroupRelationService.deleteBatchRelation(collect);
    }

    @Override
    public PageUtils getNoRelationAttr(Map<String, Object> params, Long attrgroupId) {
        // 当前分组只能关联自己所属的分类里面的所有信息
        final AttrGroupEntity attrGroupEntity = attrGroupService.getById(attrgroupId);
        // 当前分组只能关联别的分组没有引用的属性
        final Long catelogId = attrGroupEntity.getCatelogId();
        final List<AttrGroupEntity> attrGroupEntityList = attrGroupService.list(Wrappers.<AttrGroupEntity>lambdaQuery()
                .eq(AttrGroupEntity::getCatelogId, catelogId));
        final List<Long> collect = attrGroupEntityList
                .stream()
                .map(AttrGroupEntity::getAttrGroupId)
                .collect(Collectors.toList());
        // 可以关联的属性集合
        if (CollectionUtils.isEmpty(collect)) {
            return new PageUtils(new Page<>());
        }
        final List<AttrAttrgroupRelationEntity> relationEntityList = attrAttrgroupRelationService.list(Wrappers.<AttrAttrgroupRelationEntity>lambdaQuery()
                .in(AttrAttrgroupRelationEntity::getAttrGroupId, collect));
        final List<Long> attrIdList = relationEntityList
                .stream()
                .map(AttrAttrgroupRelationEntity::getAttrId)
                .collect(Collectors.toList());
        final String key = (String) params.get("key");
        final IPage<AttrEntity> page = page(new Query<AttrEntity>().getPage(params), Wrappers.<AttrEntity>lambdaQuery()
                .eq(AttrEntity::getCatelogId, catelogId)
                .eq(AttrEntity::getAttrType, ProductConstant.AttrEum.ATTR_TYPE_BASE.getCode())
                .notIn(CollectionUtils.isNotEmpty(attrIdList), AttrEntity::getAttrId, attrIdList)
                .and(StringUtils.isNotBlank(key), item -> item
                        .eq(AttrEntity::getAttrId, key)
                        .or()
                        .like(AttrEntity::getAttrName, key)));
        return new PageUtils(page);
    }

}