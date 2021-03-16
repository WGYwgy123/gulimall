package com.wgy.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wgy.common.utils.PageUtils;
import com.wgy.gulimall.product.entity.CategoryEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品三级分类
 *
 * @author wugaoyao
 * @email wugaoyao@gmail.com
 * @date 2020-12-10 20:44:49
 */
public interface CategoryService extends IService<CategoryEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     *
     * @description 查出所有分类以及子分类，以树形结构组装起来
     * @author 吴高耀
     * @email 709581924@qq.com
     * @date 2020/12/18 15:32
     */
    List<CategoryEntity> listWithTree();

    /**
     *
     * @description 批量删除目录
     * @author 吴高耀
     * @email 709581924@qq.com
     * @date 2020/12/21 15:55
     * @param asList
     */
    void removeMenuByIds(List<Long> asList);

    /**
     * 找到catelogId的完整路径
     * @param catelogId 目录ID
     * @return 完整路径
     */
    Long[] findCatelogPath(Long catelogId);

    Boolean updateCascade(CategoryEntity category);
}

