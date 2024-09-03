package com.starseaoj.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

// 排除数据库配置
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableDiscoveryClient
public class StarseaojBackendGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(StarseaojBackendGatewayApplication.class, args);
    }

}
