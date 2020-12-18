package com.wgy.gulimall.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 1. 整合MyBatis-Plus
 *      1. 导入依赖
 *      2. 配置
 *          1. 配置数据源
 *          2. 配置MyBatis-Plus
 *              1. 使用@MapperScan 扫描mapper接口
 *              2. 告诉其配置.xml文件的位置
 */
@MapperScan("com.wgy.gulimall.product.dao")
@EnableDiscoveryClient
@SpringBootApplication
public class GulimallProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(GulimallProductApplication.class, args);
    }

}
