package com.wgy.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wgy.common.utils.PageUtils;
import com.wgy.common.utils.Query;
import com.wgy.gulimall.product.dao.AttrGroupDao;
import com.wgy.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.wgy.gulimall.product.entity.AttrEntity;
import com.wgy.gulimall.product.entity.AttrGroupEntity;
import com.wgy.gulimall.product.service.AttrAttrgroupRelationService;
import com.wgy.gulimall.product.service.AttrGroupService;
import com.wgy.gulimall.product.service.AttrService;
import com.wgy.gulimall.product.vo.AttrGroupWithAttrsVo;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

    @Resource
    private AttrService attrService;

    @Resource
    private AttrAttrgroupRelationService attrAttrgroupRelationService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                new QueryWrapper<AttrGroupEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, Long catelogId) {
        final String key = (String) params.get("key");
        final LambdaQueryWrapper<AttrGroupEntity> wrapper = new LambdaQueryWrapper<AttrGroupEntity>()
                .eq(ObjectUtils.isNotEmpty(catelogId) && catelogId != 0, AttrGroupEntity::getCatelogId, catelogId)
                .and(StringUtils.isNotBlank(key), group -> group
                        .eq(AttrGroupEntity::getAttrGroupId, key)
                        .or().like(AttrGroupEntity::getAttrGroupName, key));
        final IPage<AttrGroupEntity> page = this.page(new Query<AttrGroupEntity>().getPage(params), wrapper);
        return new PageUtils(page);
    }

    @Override
    public List<AttrGroupWithAttrsVo> getAttrGroupWithAttrsByCatelogId(Long catelogId) {
        // 查询分组信息
        final List<AttrGroupEntity> attrGroupEntityList = list(Wrappers.<AttrGroupEntity>lambdaQuery()
                .eq(AttrGroupEntity::getCatelogId, catelogId));
        // 查询分组属性
        return attrGroupEntityList
                .stream()
                .map(item -> {
                    final AttrGroupWithAttrsVo attrGroupWithAttrsVo = new AttrGroupWithAttrsVo();
                    BeanUtils.copyProperties(item, attrGroupWithAttrsVo);
                    final List<AttrEntity> attrEntities = attrService.getRelationAttr(attrGroupWithAttrsVo.getAttrGroupId());
                    attrGroupWithAttrsVo.setAttrs(attrEntities);
                    return attrGroupWithAttrsVo;
                })
                .collect(Collectors.toList());
    }

}