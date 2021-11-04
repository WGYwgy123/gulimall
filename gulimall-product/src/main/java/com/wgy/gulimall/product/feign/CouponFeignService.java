package com.wgy.gulimall.product.feign;

import com.wgy.common.to.SkuReductionTo;
import com.wgy.common.to.SpuBoundTo;
import com.wgy.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Author WGY
 * @Date 2021/3/13
 * @Time 19:48
 * To change this template use File | Settings | File Templates.
 **/
@FeignClient("gulimall-coupon")
public interface CouponFeignService {

    /**
     * 1. @RequestBody 将 spuBoundTo 转成json
     * 2. 在注册中心找到 gulimall-coupon服务， 给/coupon/spubounds/save发送请求
     *      ，将上一步的json放在请求体的位置发送请求
     * 3. 对方服务收到请求。请求体里有json数据
     *       @PostMapping("/save")
     *       public R save(@RequestBody SpuBoundsEntity spuBounds) 将请求体的类型转成 SpuBoundsEntity
     * 只要json数据模型是兼容的，双方服务无需使用同一个to，命名也不用相同，是根据路径来发送请求的
     * @param spuBoundTo 应用层传递数据模型
     * @return
     */
    @PostMapping("/coupon/spubounds/save")
    R saveSpuBounds(@RequestBody SpuBoundTo spuBoundTo);

    @PostMapping("/coupon/skufullreduction/saveinfo")
    R saveSkuReduction(@RequestBody SkuReductionTo skuReductionTo);
}
