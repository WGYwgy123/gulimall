package com.wgy.gulimall.search.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * 1. 导入依赖
 * 2. 编写配置，给容器注入一个RestHighLevelClient
 * 3. 参照API https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/java-rest-high.html
 */

/**
 * @Author WGY
 * @Date 2021/8/22
 * @Time 15:43
 * To change this template use File | Settings | File Templates.
 **/
@Configuration
public class ElasticSearchConfig {

    public static final RequestOptions COMMON_OPTIONS;
    static {
        RequestOptions.Builder builder = RequestOptions.DEFAULT.toBuilder();
//        builder.addHeader("Authorization", "Bearer " + TOKEN);
//        builder.setHttpAsyncResponseConsumerFactory(
//                new HttpAsyncResponseConsumerFactory
//                        .HeapBufferedResponseConsumerFactory(30 * 1024 * 1024 * 1024));
        COMMON_OPTIONS = builder.build();
    }

    @Bean
    public RestHighLevelClient esRestClient() {
        RestClientBuilder builder = null;
        // final String hostname, final int port, final String scheme
        builder = RestClient.builder(new HttpHost("10.0.0.100", 9200, "http"));
        return new RestHighLevelClient(builder);
   /*     return new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("10.0.0.100", 9200, "http")));*/
    }
}
