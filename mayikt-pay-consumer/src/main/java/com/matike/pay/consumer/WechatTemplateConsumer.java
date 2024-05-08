package com.matike.pay.consumer;

import com.alibaba.fastjson.JSONObject;
import com.matike.pay.bo.NotifyMerchantBO;
import com.matike.pay.config.RabbitMQSpareTireConfig;
import com.matike.pay.consumer.service.MayiktConsumerLogService;
import com.matike.pay.consumer.service.PayMessageTemplate;
import com.matike.pay.entity.MayiktConsumerLog;
import com.mayikt.pay.constant.Constant;
import com.mayikt.pay.utils.RedisUtils;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
@RabbitListener(queues = RabbitMQSpareTireConfig.RETRY_QUEUE)
public class WechatTemplateConsumer {
    @Autowired
    private PayMessageTemplate payMessageTemplate;
    @Autowired
    private MayiktConsumerLogService mayiktConsumerLogService;
    @Autowired
    private AmqpTemplate amqpTemplate;
    @Value("${spring.rabbitmq.listener.simple.retry.max-attempts}")
    private Integer maxAttempts;
    @RabbitHandler
    public void process(String msg, Message message, Channel channel) throws IOException {
        log.info("消费者接收微信消息模板：{}",msg);
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        NotifyMerchantBO notifyMerchantBO = null;
        String msgId = null;
        try{
            /**
             * 调用第三方的微信模板接口
             */
            notifyMerchantBO = JSONObject.parseObject(msg, NotifyMerchantBO.class);
            //为了避免幂等性问题，设定msgId
            msgId = notifyMerchantBO.getPaymentId();
            MayiktConsumerLog msgIdConsumerLog = mayiktConsumerLogService.getByMsgIdConsumerLog(msgId);
            if (msgIdConsumerLog != null){
                //该消息已经消费，手动ack提交
                channel.basicAck(deliveryTag,Boolean.TRUE);
                return;
            }
            Boolean result = payMessageTemplate.notifyMerchantPaymentResults(notifyMerchantBO);
            if (!result){
                //拒绝签收,抛出异常
                log.info("<调用微信模板发送消息失败>notifyMerchantBO:{}",notifyMerchantBO);
                throw new RuntimeException("调用微信模板发送消息失败....");
            }
        }catch (Exception e){
            log.info("e:{}",e);
            //拒绝签收,抛出异常（mq间隔一段时间重试）
            //1.获取该msg消息被重试的次数
            String spareTire = RedisUtils.getString(msgId);
            Integer retrySpareTire = StringUtils.isEmpty(spareTire) ?
                    Constant.DEFAULT_VALUE_ZERO : Integer.parseInt(spareTire);
            //2.计数
            Integer newSpareTire = retrySpareTire + 1;
            //3.判断重试次数是否超过设定阈值
            if (newSpareTire >= maxAttempts){
                //转移到死信队列
                amqpTemplate.convertAndSend(RabbitMQSpareTireConfig.RETRY_EXCHANGE,"",msg);
                log.info("将该消息发送到死信队列");
                //删除该msgId的计数记录
                RedisUtils.delKey(msgId);
                return;
            }
            //4.再redis中修改重试次数
            RedisUtils.setString(msgId,newSpareTire);
            throw new RuntimeException(e);
        }
    }
}
