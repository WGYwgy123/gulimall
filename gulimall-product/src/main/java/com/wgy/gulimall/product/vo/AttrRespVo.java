package com.wgy.gulimall.product.vo;

import lombok.Data;

/**
 * @Author WGY
 * @Date 2021/3/10
 * @Time 16:20
 * To change this template use File | Settings | File Templates.
 **/
@Data
public class AttrRespVo extends AttrVo{
    /**
     * 分类名字
     */
    private String catelogName;

    /**
     * 分组名字
     */
    private String groupName;

    /**
     * 分类路径
     */
    private Long[] catelogPath;
}
