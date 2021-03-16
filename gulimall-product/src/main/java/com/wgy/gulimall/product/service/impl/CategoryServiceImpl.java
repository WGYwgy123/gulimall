package com.wgy.gulimall.product.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wgy.common.utils.PageUtils;
import com.wgy.common.utils.Query;
import com.wgy.gulimall.product.dao.CategoryDao;
import com.wgy.gulimall.product.entity.CategoryEntity;
import com.wgy.gulimall.product.service.CategoryBrandRelationService;
import com.wgy.gulimall.product.service.CategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Resource
    private CategoryBrandRelationService categoryBrandRelationService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listWithTree() {
        List<CategoryEntity> categoryEntityList = list();

        List<CategoryEntity> entityList = categoryEntityList.stream()
                .filter(categoryEntity -> categoryEntity.getParentCid() == 0)
                .map(menu -> {
                    menu.setChildren(getChildren(menu, categoryEntityList));
                    return menu;
                })
                .sorted((menu1, menu2) -> (menu1.getSort() == null ? 0 : menu1.getSort()) - (menu2.getSort() == null ? 0 : menu2.getSort()))
                .collect(Collectors.toList());
        return entityList;
    }

    @Override
    public void removeMenuByIds(List<Long> asList) {
        //TODO 1. 检查当前删除的菜单是否被其他地方引用
        removeByIds(asList);
    }

    @Override
    public Long[] findCatelogPath(Long catelogId) {
        final List<Long> path = new ArrayList<>();
        final List<Long> parentPath = findParentPath(catelogId, path);
        CollectionUtil.reverse(parentPath);
        return parentPath.toArray(new Long[parentPath.size()]);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateCascade(CategoryEntity category) {
        updateById(category);
        if (StringUtils.isNotBlank(category.getName())) {
            categoryBrandRelationService.updateCategory(category.getCatId(), category.getName());
        }
        return true;
    }

    // 递归查找父节点ID
    private List<Long> findParentPath(Long catelogId, List<Long> path) {
        path.add(catelogId);
        final CategoryEntity categoryEntity = getById(catelogId);
        if (categoryEntity.getParentCid() != 0) {
            findParentPath(categoryEntity.getParentCid(), path);
        }
        return path;
    }

    //递归查找当前菜单的子菜单
    private List<CategoryEntity> getChildren(CategoryEntity root, List<CategoryEntity> all) {
        List<CategoryEntity> childrenList = all.stream()
                .filter(categoryEntity -> categoryEntity.getParentCid().equals(root.getCatId()))
                .map(menu -> {
                    menu.setChildren(getChildren(menu, all));
                    return menu;
                })
                .sorted((menu1, menu2) -> (menu1.getSort() == null ? 0 : menu1.getSort()) - (menu2.getSort() == null ? 0 : menu2.getSort()))
                .collect(Collectors.toList());
        return childrenList;
    }
}