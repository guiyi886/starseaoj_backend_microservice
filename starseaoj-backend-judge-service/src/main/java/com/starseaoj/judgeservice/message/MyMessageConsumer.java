package com.starseaoj.judgeservice.message;

import com.rabbitmq.client.Channel;
import com.starseaoj.judgeservice.judge.JudgeService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class MyMessageConsumer {

    // 用于消息处理中的业务逻辑
    @Resource
    private JudgeService judgeService;

    /**
     * 监听 RabbitMQ 中的消息队列 "code_queue"，并指定手动确认消息的机制。
     * 当消息被消费后，需要显式调用 `basicAck` 确认消息，或者调用 `basicNack` 拒绝消息。
     *
     * @param message     收到的消息内容，类型为 String
     * @param channel     与 RabbitMQ 通信的通道对象，用于手动确认、拒绝消息等操作
     * @param deliveryTag 消息的唯一标识，用于确认或拒绝消息
     */
    @SneakyThrows  // Lombok 提供的注解，简化代码，不需要显式捕获异常
    @RabbitListener(queues = {"code_queue"}, ackMode = "MANUAL")
    // 该方法用于接收消息，监听指定队列，并实现手动消息确认机制
    public void receiveMessage(String message, Channel channel,
                               @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        // 记录收到的消息
        log.info("receiveMessage message = {}", message);

        // 将收到的消息（字符串）转换为 long 类型的 questionSubmitId，表示题目提交 ID
        long questionSubmitId = Long.parseLong(message);

        try {
            // 调用业务逻辑进行判题操作
            judgeService.doJudge(questionSubmitId);

            // 消息成功处理后，手动确认消息，deliveryTag 是消息的唯一标识
            // false 表示只确认当前消息，而不是批量确认
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            // 发生异常时，手动拒绝消息，并将消息从队列中移除
            // basicNack 参数说明：
            // deliveryTag：要拒绝的消息唯一标识
            // false：拒绝当前消息（不批量拒绝）
            // false：拒绝后不重新放回队列
            channel.basicNack(deliveryTag, false, false);
        }
    }
}


