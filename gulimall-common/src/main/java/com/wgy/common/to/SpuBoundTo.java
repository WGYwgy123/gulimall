package com.wgy.common.to;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author WGY
 * @Date 2021/3/13
 * @Time 19:55
 * To change this template use File | Settings | File Templates.
 **/
@Data
public class SpuBoundTo {

    private Long spuId;
    private BigDecimal buyBounds;
    private BigDecimal growBounds;

}
