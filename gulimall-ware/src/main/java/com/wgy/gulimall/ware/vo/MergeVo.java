package com.wgy.gulimall.ware.vo;

import lombok.Data;

import java.util.List;

/**
 * @Author WGY
 * @Date 2021/3/14
 * @Time 19:42
 * To change this template use File | Settings | File Templates.
 **/
@Data
public class MergeVo {
    private Long purchaseId;
    private List<Long> items;
}
