package com.starseaoj.questionservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.starseaoj.questionservice.mapper")
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@ComponentScan("com.starseaoj")
// 启用服务发现功能，使得应用程序能够注册到服务注册中心，并且能够发现和调用其他注册在服务注册中心的服务。
@EnableDiscoveryClient
// 启用 Feign 客户端功能，允许你在 Spring 应用中使用 Feign 来调用其他服务。
// Feign 是一种声明式的 HTTP 客户端，可以将远程服务调用抽象为接口调用。
// 如果没有调用其他服务的话可以不添加
@EnableFeignClients(basePackages = "com.starseaoj.serviceclient")
public class StarseaojBackendQuestionServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(StarseaojBackendQuestionServiceApplication.class, args);
    }

}
