package com.matike.pay.consumer;

import com.alibaba.fastjson.JSONObject;
import com.matike.pay.bo.NotifyMerchantBO;
import com.matike.pay.config.RabbitMQBatchConfig;
import com.matike.pay.config.RabbitMQSpareTireConfig;
import com.matike.pay.consumer.service.MayiktConsumerLogService;
import com.matike.pay.consumer.service.PayMessageTemplate;
import com.matike.pay.entity.MayiktConsumerLog;
import com.mayikt.pay.utils.RedisUtils;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RabbitListener(queues = RabbitMQBatchConfig.MAYIKT_BATCH_WECHAT_TEMPLATE_QUEUE)
public class WechatBatchTemplateConsumer {
    @Autowired
    private PayMessageTemplate payMessageTemplate;
    @Autowired
    private AmqpTemplate amqpTemplate;
    @Autowired
    private MayiktConsumerLogService mayiktConsumerLogService;
    @RabbitHandler
    public void process(String msg, Message message, Channel channel){
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try{
            List<NotifyMerchantBO> notifyMerchantBOS =
                    JSONObject.parseArray(msg, NotifyMerchantBO.class);
            notifyMerchantBOS.forEach((notifyMerchantBO)->{
                //先根据msgId查询该消息是否已经被消费，如果被消费了，自动提交。
                String msgId = notifyMerchantBO.getPaymentId();
                MayiktConsumerLog mayiktConsumerLog = mayiktConsumerLogService.getByMsgIdConsumerLog(msgId);
                if (mayiktConsumerLog == null){
                    Boolean result = payMessageTemplate.notifyMerchantPaymentResults(notifyMerchantBO);
                    if (!result) {
                        //返回false手动ack失败 需要MQ给我们消费者补偿
                        //拒绝签收 抛出异常（mq服务器端间隔一段重试该msg）
                        log.error("[调用消息模板接口发送消息失败,notifyMerchantBO:{}]",
                                notifyMerchantBO);
                        // 不要抛出异常，将该异常消息存入到死信队列中
                        String errorMsg = JSONObject.toJSONString(notifyMerchantBO);
                        amqpTemplate.convertAndSend(RabbitMQSpareTireConfig.
                                        RETRY_EXCHANGE,
                                "", errorMsg);
                    }
                }
            });
        }catch (Exception e){
            log.info("e:{}",e);
            throw new RuntimeException("系统错误");
        }
    }
}
