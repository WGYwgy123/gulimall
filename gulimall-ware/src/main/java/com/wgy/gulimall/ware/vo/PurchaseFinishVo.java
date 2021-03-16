package com.wgy.gulimall.ware.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author WGY
 * @Date 2021/3/14
 * @Time 20:47
 * To change this template use File | Settings | File Templates.
 **/
@Data
public class PurchaseFinishVo {
    @NotNull
    private Long id;

    private List<ItemVo> items;
}
