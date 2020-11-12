package com.github.tanyueran;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)// 取消数据源的自动创建
@EnableDiscoveryClient
public class CakeApplication {
    public static void main(String[] args) {
        SpringApplication.run(CakeApplication.class, args);
    }
}
