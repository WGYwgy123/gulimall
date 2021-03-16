package com.wgy.gulimall.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.server.ServerWebExchange;

/**
 * @Author WGY
 * @Date 2020/12/20
 * @Time 21:19
 * To change this template use File | Settings | File Templates.
 **/
@Configuration
public class MyCorsConfiguration {

    @Bean
    public CorsWebFilter corsWebFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        //1. 配置跨域
        //允许所带的请求头信息
        corsConfiguration.addAllowedHeader("*");
        //允许跨域的方法
        corsConfiguration.addAllowedMethod("*");
        //允许跨域的来源
        corsConfiguration.addAllowedOrigin("*");
        //是否允许携带cookie信息
        corsConfiguration.setAllowCredentials(true);

        source.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsWebFilter(source);
    }
}
