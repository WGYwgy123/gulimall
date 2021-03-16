package com.wgy.gulimall.ware.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wgy.common.utils.PageUtils;
import com.wgy.common.utils.Query;

import com.wgy.gulimall.ware.dao.WareInfoDao;
import com.wgy.gulimall.ware.entity.WareInfoEntity;
import com.wgy.gulimall.ware.service.WareInfoService;


@Service("wareInfoService")
public class WareInfoServiceImpl extends ServiceImpl<WareInfoDao, WareInfoEntity> implements WareInfoService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        final String key = (String) params.get("key");
        IPage<WareInfoEntity> page = this.page(
                new Query<WareInfoEntity>().getPage(params),
                Wrappers.<WareInfoEntity>lambdaQuery()
                        .eq(StringUtils.isNotBlank(key), WareInfoEntity::getId,key)
                        .or()
                        .like(StringUtils.isNotBlank(key), WareInfoEntity::getAddress, key)
                        .or()
                        .like(StringUtils.isNotBlank(key), WareInfoEntity::getAreacode, key)
        );

        return new PageUtils(page);
    }

}