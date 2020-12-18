package com.wgy.gulimall.member.feign;

import com.wgy.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @Author WGY
 * @Date 2020/12/13
 * @Time 15:56
 * To change this template use File | Settings | File Templates.
 **/
// 这里括号里面写的是在注册中心中的名称，
// 也就是yml中spring.application.name = XXXXX，
// 这是一个声明式的远程调用
@FeignClient("gulimall-coupon")
public interface CouponFeignService {

    //这里的mapping用的是全路径
    @RequestMapping("/coupon/coupon/list")
    public R list(@RequestParam Map<String, Object> params);
}
