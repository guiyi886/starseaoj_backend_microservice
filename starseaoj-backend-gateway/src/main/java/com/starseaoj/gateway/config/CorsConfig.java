package com.starseaoj.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.util.pattern.PathPatternParser;

import java.util.Arrays;

// 跨域配置
@Configuration
public class CorsConfig {

    // 定义一个Bean，返回一个处理跨域请求的过滤器
    @Bean
    public CorsWebFilter corsFilter() {
        // 创建跨域配置对象
        CorsConfiguration config = new CorsConfiguration();

        // 允许所有HTTP方法（GET, POST, PUT, DELETE等）
        config.addAllowedMethod("*");

        // 允许发送Cookie信息
        config.setAllowCredentials(true);

        // 允许所有域名发起请求，使用通配符"*"匹配所有域
        config.setAllowedOriginPatterns(Arrays.asList("*"));

        // 允许所有请求头
        config.addAllowedHeader("*");

        // 创建跨域配置源对象，并设置请求路径的匹配规则
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(new PathPatternParser());

        // 将配置应用于所有请求路径（/** 表示所有路径）
        source.registerCorsConfiguration("/**", config);

        // 返回一个跨域过滤器
        return new CorsWebFilter(source);
    }
}


