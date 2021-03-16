package com.wgy.common.to;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author WGY
 * @Date 2021/3/13
 * @Time 20:15
 * To change this template use File | Settings | File Templates.
 **/
@Data
public class SkuReductionTo {

    private Long skuId;
    private int fullCount;
    private BigDecimal discount;
    private int countStatus;
    private BigDecimal fullPrice;
    private BigDecimal reducePrice;
    private int priceStatus;
    private List<MemberPrice> memberPrice;

}
