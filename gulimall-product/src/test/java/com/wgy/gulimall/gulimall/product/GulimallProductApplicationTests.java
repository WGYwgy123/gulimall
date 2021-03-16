package com.wgy.gulimall.gulimall.product;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class GulimallProductApplicationTests {

    @Test
    void contextLoads() {
    }

    /*public void testUpload() throws FileNotFoundException {
        // Endpoint以杭州为例，其它Region请按实际情况填写。
        String endpoint = "oss-cn-hangzhou.aliyuncs.com";
        // 云账号AccessKey有所有API访问权限，建议遵循阿里云安全最佳实践，创建并使用RAM子账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建。
        String accessKeyId = "LTAI4G6E7urG4Q8TkfW8Xexj";
        String accessKeySecret = "pe5XsH9MkarckCJ05xl99r8w6vVNua";

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 上传文件流。
        InputStream inputStream = new FileInputStream("<yourlocalFile>");
        ossClient.putObject("gulimall-wgy", "<yourObjectName>", inputStream);

        // 关闭OSSClient。
        ossClient.shutdown();
    }*/

}
