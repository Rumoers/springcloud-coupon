package com.xzh.coupon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;


/**
 * 模版微服务
 */
@EnableScheduling        //开启定时任务
@EnableJpaAuditing       //审计功能 主动注入创建时间 修改时间
@EnableEurekaClient      //标识位Eureka Client
@SpringBootApplication
public class TemplateApplication {

    public static void main(String[] args) {
        SpringApplication.run(TemplateApplication.class, args);
    }
}
