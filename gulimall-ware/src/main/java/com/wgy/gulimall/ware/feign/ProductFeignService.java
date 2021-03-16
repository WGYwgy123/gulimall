package com.wgy.gulimall.ware.feign;

import com.wgy.common.to.SkuReductionTo;
import com.wgy.common.to.SpuBoundTo;
import com.wgy.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author WGY
 * @Date 2021/3/13
 * @Time 19:48
 * To change this template use File | Settings | File Templates.
 **/
@FeignClient("gulimall-product")
public interface ProductFeignService {

    /**
     * @FeignClient("gulimall-gateway") 给网关发请求 /api/product/skuinfo/info/{skuId}
     * @param skuId
     * @return
     */
    @RequestMapping("/product/skuinfo/info/{skuId}")
    //@RequiresPermissions("product:skuinfo:info")
    public R info(@PathVariable("skuId") Long skuId);
}
