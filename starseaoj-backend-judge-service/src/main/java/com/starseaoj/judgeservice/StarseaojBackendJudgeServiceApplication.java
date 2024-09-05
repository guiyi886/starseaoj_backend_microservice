package com.starseaoj.judgeservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@ComponentScan("com.starseaoj")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.starseaoj.serviceclient")
public class StarseaojBackendJudgeServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(StarseaojBackendJudgeServiceApplication.class, args);
    }

}
