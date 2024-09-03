package com.starseaoj.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

// 全局过滤器，用于在请求进入服务网关时进行权限检查
@Component
public class GlobalAuthFilter implements GlobalFilter, Ordered {

    // 使用AntPathMatcher来匹配请求路径，它支持Ant风格的路径匹配
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    // 实现GlobalFilter接口的filter方法，用于处理每个传入的请求
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 获取当前请求的ServerHttpRequest对象
        ServerHttpRequest serverHttpRequest = exchange.getRequest();
        // 获取请求的URI路径
        String path = serverHttpRequest.getURI().getPath();

        // 如果请求路径匹配 "/**/inner/**"（即任何路径中的“inner”子路径）
        if (antPathMatcher.match("/**/inner/**", path)) {
            // 获取当前的ServerHttpResponse对象
            ServerHttpResponse response = exchange.getResponse();
            // 设置响应状态码为403 Forbidden，表示拒绝访问
            response.setStatusCode(HttpStatus.FORBIDDEN);

            // 创建DataBufferFactory以便生成响应数据
            DataBufferFactory dataBufferFactory = response.bufferFactory();
            // 创建包含“无权限”信息的DataBuffer，设置为UTF-8编码
            DataBuffer dataBuffer = dataBufferFactory.wrap("无权限".getBytes(StandardCharsets.UTF_8));

            // 将DataBuffer写入响应并返回，结束请求处理
            return response.writeWith(Mono.just(dataBuffer));
        }

        // 如果路径不匹配"/**/inner/**"，则继续处理请求
        return chain.filter(exchange);
    }

    /**
     * 设为最高优先级
     *
     * @return
     */
    @Override
    public int getOrder() {
        return 0;
    }
}
