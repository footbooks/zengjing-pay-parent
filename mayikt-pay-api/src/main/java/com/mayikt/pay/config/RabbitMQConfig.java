package com.mayikt.pay.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQConfig {
    /**
     * 定义交换机的名/mayikt_ex
     */
    public static final String EXCHANGE_MAYIKT_NAME = "/zengjing_ex";


    /**
     * 微信模板队列
     */
    public static final String MAYIKT_WECHAT_TEMPLATE_QUEUE = "fanout_wechattemplate_queue";


    /**
     * 配置微信消息模板队列
     *
     * @return
     */
    @Bean
    public Queue fanoutWechatTemplateQueue() {
        return new Queue(MAYIKT_WECHAT_TEMPLATE_QUEUE);
    }

    /**
     * 配置fanoutExchange
     *
     * @return
     */
    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange(EXCHANGE_MAYIKT_NAME);
    }

    // 绑定交换机 Wechattemplate
    @Bean
    public Binding bindingWechattemplateFanoutExchange(Queue fanoutWechatTemplateQueue, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(fanoutWechatTemplateQueue).to(fanoutExchange);
    }
}