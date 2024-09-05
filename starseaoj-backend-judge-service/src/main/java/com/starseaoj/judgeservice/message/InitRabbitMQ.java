package com.starseaoj.judgeservice.message;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * 用于创建测试程序用到的交换机和队列（只需在程序启动前执行一次）。
 * 此程序连接到 RabbitMQ，创建一个直连交换机和一个持久化队列，
 * 并将队列绑定到交换机上，使用指定的路由键。
 */
@Configuration
@Slf4j
public class InitRabbitMQ {
    // 从配置文件中读取参数
    @Value("${spring.rabbitmq.host}")
    private String host;

    @Value("${spring.rabbitmq.port}")
    private int port;

    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;

    @PostConstruct
    public void doInit() {
        try {
            // 创建连接工厂，用于配置与 RabbitMQ 的连接参数
            ConnectionFactory factory = new ConnectionFactory();

            // 设置 RabbitMQ 服务器
            factory.setHost(host);
            factory.setPort(port);
            factory.setUsername(username);
            factory.setPassword(password);

            // 创建与 RabbitMQ 的连接
            Connection connection = factory.newConnection();

            // 创建通道（Channel），大部分 RabbitMQ 操作都是通过通道完成的
            Channel channel = connection.createChannel();

            // 定义交换机的名称
            String EXCHANGE_NAME = "code_exchange";

            // 声明一个交换机，类型为 "direct"（直连交换机）
            // 如果交换机已存在，不会重复创建
            channel.exchangeDeclare(EXCHANGE_NAME, "direct");

            // 定义队列的名称
            String queueName = "code_queue";

            /**
             * 声明一个队列，如果该队列已存在，则不会重新创建
             * 参数解析：
             * queueName：队列名称
             * true：队列是否持久化（即服务器重启后队列仍然存在）
             * false：是否独占队列（即仅当前连接可以使用该队列）
             * false：当没有消费者时，是否自动删除该队列
             * null：队列的其他属性（如 TTL，消息大小限制等），这里为 null 表示没有额外属性
             */
            channel.queueDeclare(queueName, true, false, false, null);

            /**
             * 将队列绑定到交换机，并指定路由键
             * 参数解析：
             * queueName：队列名称
             * EXCHANGE_NAME：交换机名称
             * "my_routingKey"：路由键，用于匹配交换机发送的消息
             * 通过绑定操作，只有携带此路由键的消息才会路由到该队列中
             */
            channel.queueBind(queueName, EXCHANGE_NAME, "my_routingKey");
            log.info("创建消息队列成功");
        } catch (Exception e) {
            // 异常处理，如果发生异常，打印堆栈信息
            log.info("创建消息队列失败");
            e.printStackTrace();
        }
    }
}
