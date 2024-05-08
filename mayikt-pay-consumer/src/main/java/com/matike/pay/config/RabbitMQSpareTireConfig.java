package com.matike.pay.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.ImmediateRequeueMessageRecoverer;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author 余胜军
 * @ClassName RabbitConfig
 */
@Component
public class RabbitMQSpareTireConfig {
    public static final String RETRY_QUEUE = "retry_queue";
    public static final String RETRY_EXCHANGE = "retry.exchange";

    /**
     * 配置重试队列
     *
     * @return
     */
    @Bean
    public Queue fanoutRetryQueueQueue() {

        return new Queue(RETRY_QUEUE);
    }

    /**
     * 配置重试交换机
     *
     * @return
     */
    @Bean
    public FanoutExchange
    fanoutExchange() {
        return new FanoutExchange(RETRY_EXCHANGE);
    }

    // 重试队列与交换机绑定
    @Bean
    public Binding bindingRetryWechattemplateFanoutExchange(Queue fanoutRetryQueueQueue, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(fanoutRetryQueueQueue)
                .to(fanoutExchange);
    }
}