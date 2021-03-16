package com.wgy.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wgy.common.utils.PageUtils;
import com.wgy.gulimall.product.entity.AttrEntity;
import com.wgy.gulimall.product.vo.AttrGroupRelationVo;
import com.wgy.gulimall.product.vo.AttrRespVo;
import com.wgy.gulimall.product.vo.AttrVo;

import java.util.List;
import java.util.Map;

/**
 * 商品属性
 *
 * @author wugaoyao
 * @email wugaoyao@gmail.com
 * @date 2020-12-10 20:44:48
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveAttr(AttrVo attr);

    PageUtils queryBaseAttrPage(Map<String, Object> params, Long catelogId, String attrType);

    AttrRespVo getAttrInfo(Long attrId);

    void updateAttr(AttrVo attr);

    List<AttrEntity> getRelationAttr(Long attrgroupId);

    void deleteRelation(List<AttrGroupRelationVo> relationVoList);

    PageUtils getNoRelationAttr(Map<String, Object> params, Long attrgroupId);
}

