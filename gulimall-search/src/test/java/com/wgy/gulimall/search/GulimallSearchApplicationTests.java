package com.wgy.gulimall.search;

import com.alibaba.fastjson.JSON;
import com.wgy.gulimall.search.config.ElasticSearchConfig;
import lombok.Data;
import lombok.ToString;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.Avg;
import org.elasticsearch.search.aggregations.metrics.AvgAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GulimallSearchApplicationTests {

	@ToString
	@Data
	static public class Account {

		private int account_number;
		private int balance;
		private String firstname;
		private String lastname;
		private int age;
		private String gender;
		private String address;
		private String employer;
		private String email;
		private String city;
		private String state;

	}

	@Data
	class User {
		private String userName;
		private String gender;
		private Integer age;
	}

    @Resource
    private RestHighLevelClient client;

	/*
	搜索 address 中包含 mill 的所有人的年龄分布以及平均年龄，但不显示这些人的详情。
	 */
	@Test
	public void searchData() throws IOException {
		// 1. 创建检索请求
		final SearchRequest searchRequest = new SearchRequest();
		// 2. 指定索引
		searchRequest.indices("bank");
		// 3. 指定DSL，索引条件
		// SearchSourceBuilder sourcebuild 封装的条件
		final SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		// 3.1 构造检索条件
		searchSourceBuilder.query(QueryBuilders.matchAllQuery());

		// 3.2 按照年龄的值分布进行聚合
		final TermsAggregationBuilder ageAgg = AggregationBuilders
				.terms("ageAgg").field("age").size(10);
		searchSourceBuilder.aggregation(ageAgg);

		// 3.3 计算平均薪资
		final AvgAggregationBuilder balanceAvg = AggregationBuilders
				.avg("balanceAvg").field("balance");
		searchSourceBuilder.aggregation(balanceAvg);

		searchRequest.source(searchSourceBuilder);


		// 4. 执行检索
		final SearchResponse response = client.search(searchRequest, ElasticSearchConfig.COMMON_OPTIONS);
		// 5. 分析结果
		System.out.println(response);
//		final Map map = JSON.parseObject(response.toString(), Map.class);
		// 5.1 获取所有查到的数据
		final SearchHits hits = response.getHits();
		final SearchHit[] hitsHits = hits.getHits();
//		final ArrayList<Account> accounts = new ArrayList<>();
		final List<Account> accountList = Arrays.stream(hitsHits)
				.map(item -> JSON.parseObject(item.getSourceAsString(), Account.class))
				.collect(Collectors.toList());
//		for (SearchHit hitsHit : hitsHits) {
//			final String string = hitsHit.getSourceAsString();
//			final Account account = JSON.parseObject(string, Account.class);
//			accounts.add(account);
//		}
		accountList.forEach(account -> System.out.println(account.toString()));

		// 5.2 获取检索到的分析数据
		final Aggregations aggregations = response.getAggregations();
		final Terms ageAgg1 = aggregations.get("ageAgg");
		for (Terms.Bucket bucket : ageAgg1.getBuckets()) {
			final String keyAsString = bucket.getKeyAsString();
			System.out.println(keyAsString);
			System.out.println(bucket.getDocCount());
		}

		final Avg balanceAgg = aggregations.get("balanceAvg");
		System.out.println(balanceAgg.getValue());

	}


    /**
     * 测试存储数据到es
	 * 更新也可以
     */
    @Test
    public void indexData() throws IOException {
        final IndexRequest indexRequest = new IndexRequest("users");
        // 数据的ID
        indexRequest.id("1");
		final User user = new User();
		user.setAge(18);
		user.setGender("男");
		final String jsonString = JSON.toJSONString(user);
		// 数据的内容
		indexRequest.source(jsonString, XContentType.JSON);
//        indexRequest.source("userName", "zhangsan","age", 18,"gender", "男");

		// 执行操作
		final IndexResponse index = client.index(indexRequest, ElasticSearchConfig.COMMON_OPTIONS);

		// 提取有用的响应数据
		System.out.println(index);
	}

	@Test
	public void getData() throws IOException {
		final GetRequest getRequest = new GetRequest("users", "1");

		// 可选参数，用于指定包含和不包含的内容
		String[] includes = new String[]{"age", "userName"};
		String[] excludes = Strings.EMPTY_ARRAY;
		FetchSourceContext fetchSourceContext =
				new FetchSourceContext(true, includes, excludes);
		getRequest.fetchSourceContext(fetchSourceContext);

		/**
		 * store为false时(默认配置），这些field只存储在"_source" field中。
		 * 当store为true时，这些field的value会存储在一个跟 _source
		 * 平级的独立的field中。同时也会存储在_source中，所以有两份拷贝。
		 */
//		getRequest.storedFields("age");

		final GetResponse response = client.get(getRequest, ElasticSearchConfig.COMMON_OPTIONS);
		System.out.println(response);

//		String age = response.getField("age").getValue();
//		System.out.println(message);
	}

    @Test
    public void contextLoads() {
        System.out.println(client);
    }

}
