package com.wgy.gulimall.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.wgy.gulimall.product.entity.AttrEntity;
import com.wgy.gulimall.product.service.AttrAttrgroupRelationService;
import com.wgy.gulimall.product.service.AttrService;
import com.wgy.gulimall.product.service.CategoryService;
import com.wgy.gulimall.product.vo.AttrGroupRelationVo;
import com.wgy.gulimall.product.vo.AttrGroupWithAttrsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.wgy.gulimall.product.entity.AttrGroupEntity;
import com.wgy.gulimall.product.service.AttrGroupService;
import com.wgy.common.utils.PageUtils;
import com.wgy.common.utils.R;

import javax.annotation.Resource;


/**
 * 属性分组
 *
 * @author wugaoyao
 * @email wugaoyao@gmail.com
 * @date 2020-12-10 20:44:49
 */
@RestController
@RequestMapping("product/attrgroup")
public class AttrGroupController {
    @Autowired
    private AttrGroupService attrGroupService;

    @Resource
    private CategoryService categoryService;

    @Resource
    private AttrService attrService;

    @Resource
    private AttrAttrgroupRelationService attrAttrgroupRelationService;

    @GetMapping("/{catelogId}/withattr")
    public R getAttrGroupWithAttrs(@PathVariable Long catelogId) {
        List<AttrGroupWithAttrsVo> list = attrGroupService.getAttrGroupWithAttrsByCatelogId(catelogId);
        return R.ok().put("data", list);
    }

    @PostMapping("/attr/relation")
    public R addRelation(@RequestBody List<AttrGroupRelationVo> relationVos) {
        attrAttrgroupRelationService.saveBatch(relationVos);
        return R.ok();
    }

    @PostMapping("/attr/relation/delete")
    public R deleteRelation(@RequestBody List<AttrGroupRelationVo> relationVoList) {
        attrService.deleteRelation(relationVoList);
        return R.ok();
    }

    @GetMapping("/{attrgroupId}/attr/relation")
    public R attrRelation(@PathVariable Long attrgroupId) {
        List<AttrEntity> attrEntityList = attrService.getRelationAttr(attrgroupId);
        return R.ok().put("data", attrEntityList);
    }

    @GetMapping("/{attrgroupId}/noattr/relation")
    public R attrNoRelation(@RequestParam Map<String, Object> params,
                            @PathVariable Long attrgroupId) {
        PageUtils pageUtils = attrService.getNoRelationAttr(params, attrgroupId);
        return R.ok().put("page", pageUtils);
    }

    /**
     * 列表
     */
    @RequestMapping("/list/{catelogId}")
    //@RequiresPermissions("product:attrgroup:list")
    public R list(@RequestParam Map<String, Object> params,
                  @PathVariable("catelogId") Long catelogId){
//        PageUtils page = attrGroupService.queryPage(params);
        PageUtils page = attrGroupService.queryPage(params, catelogId);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrGroupId}")
    //@RequiresPermissions("product:attrgroup:info")
    public R info(@PathVariable("attrGroupId") Long attrGroupId){
		AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);
        final Long catelogId = attrGroup.getCatelogId();
        final Long[] path = categoryService.findCatelogPath(catelogId);
        attrGroup.setCatelogPath(path);
        return R.ok().put("attrGroup", attrGroup);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:attrgroup:save")
    public R save(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.save(attrGroup);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:attrgroup:update")
    public R update(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.updateById(attrGroup);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:attrgroup:delete")
    public R delete(@RequestBody Long[] attrGroupIds){
		attrGroupService.removeByIds(Arrays.asList(attrGroupIds));

        return R.ok();
    }

}
