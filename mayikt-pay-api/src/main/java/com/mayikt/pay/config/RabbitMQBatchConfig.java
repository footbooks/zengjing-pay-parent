package com.mayikt.pay.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author 余胜军
 * @ClassName RabbitMQBatchConfig
 */
@Component
public class RabbitMQBatchConfig {
    /**
     * 定义交换机的名称batch.mayikt_ex
     */
    public static final String
            EXCHANGE_BATCH_MAYIKT_NAME = "batch.zengjing_ex";


    /**
     * 微信模板队列
     */
    public static final String MAYIKT_BATCH_WECHAT_TEMPLATE_QUEUE
            = "batch_fanout_wechatTemplate_queue";
    /**
     * 缓冲区2条msg
     */
    public static final Integer BUFFER_SIZE = 2;
    /**
     * 间隔同步时间
     */
    public static final Long BUFFER_INTERVAL_TIME = 1000l;

    /**
     * 配置微信消息模板队列
     *
     * @return
     */
    @Bean
    public Queue fanoutBatchWechatTemplateQueue() {
        return new Queue(MAYIKT_BATCH_WECHAT_TEMPLATE_QUEUE);
    }

    /**
     * 配置fanoutExchange
     *
     * @return
     */
    @Bean
    public FanoutExchange
    fanoutBatchExchange() {
        return new FanoutExchange(EXCHANGE_BATCH_MAYIKT_NAME);
    }

    // 绑定交换机 Wechattemplate
    @Bean
    public Binding bindingBatchWechattemplateFanoutExchange(Queue fanoutBatchWechatTemplateQueue, FanoutExchange fanoutBatchExchange) {
        return BindingBuilder.bind(fanoutBatchWechatTemplateQueue)
                .to(fanoutBatchExchange);
    }
}
