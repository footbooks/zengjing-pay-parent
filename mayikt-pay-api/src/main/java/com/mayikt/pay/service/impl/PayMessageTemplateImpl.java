package com.mayikt.pay.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.mayikt.pay.bo.NotifyMerchantBO;
import com.mayikt.pay.buffer.MqBatchDeliveryBuffer;
import com.mayikt.pay.config.RabbitMQConfig;
import com.mayikt.pay.entity.PayMerchantInfoDO;
import com.mayikt.pay.service.PayMessageTemplate;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpTemplateMsgService;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import com.mayikt.pay.wechat.config.WxMpConfiguration;
import com.mayikt.pay.wechat.config.WxMpProperties;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;

@Service
@Slf4j
public class PayMessageTemplateImpl implements PayMessageTemplate {
    @Autowired
    private WxMpProperties wxMpProperties;
    @Value("${zengjing.pay.notifyMerchantTemplateId}")
    private String notifyMerchantTemplateId;
    @Value("${zengjing.pay.notifyMechantSettlementTemplateId}")
    private String notifyMechantSettlementTemplateId;
    @Autowired
    private AmqpTemplate amqpTemplate;
    @Autowired
    private MqBatchDeliveryBuffer mqBatchDeliveryBuffer;
    @Override
    @Async("taskExecutor")
    public Future<Boolean> notifyMerchantPaymentResultsThread(NotifyMerchantBO notifyMerchantBO) {
        WxMpTemplateMessage wxMpTemplateMessage = new WxMpTemplateMessage();
        wxMpTemplateMessage.setTemplateId(notifyMerchantTemplateId);
//        wxMpTemplateMessage.setToUser(merchantOpenId);
        wxMpTemplateMessage.setToUser("o5yRF6Wn0Vp0M8zOy4lzgfID0eFs");
        List<WxMpTemplateData> data = new ArrayList<>();
        data.add(new WxMpTemplateData("first", notifyMerchantBO.getMerchantId()));
        data.add(new WxMpTemplateData("keyword1", DateUtil.formatDateTime(notifyMerchantBO.getPayDate())));
        data.add(new WxMpTemplateData("keyword2", notifyMerchantBO.getPaymentChannel()));
        data.add(new WxMpTemplateData("keyword3", notifyMerchantBO.getPaymentAmount().toString()));
        data.add(new WxMpTemplateData("keyword4", notifyMerchantBO.getPaymentId()));
        wxMpTemplateMessage.setData(data);
        wxMpTemplateMessage.setUrl("http://www.mayikt.com");
        try {
            String appId = wxMpProperties.getConfigs().get(0).getAppId();
            WxMpTemplateMsgService templateMsgService = WxMpConfiguration.getMpServices().get(appId).getTemplateMsgService();
            String result = templateMsgService.sendTemplateMsg(wxMpTemplateMessage);
            log.info("result:{}", result);
            return new AsyncResult<>(Boolean.TRUE);
        } catch (Exception e) {
            // 记录错误日志
            log.error("e:{}", e);
            return new AsyncResult<>(Boolean.FALSE);
        }
    }
    @Override
    public void notifyMerchantPaymentResultsMq(NotifyMerchantBO notifyMerchantBO) {
        String msg = JSONObject.toJSONString(notifyMerchantBO);
        amqpTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_MAYIKT_NAME,"",msg);
        log.info("<mq中投递的消息：>{}",msg);
    }

    @Override
    public void notifyMerchantPaymentResultsBatchMq(NotifyMerchantBO notifyMerchantBO) {
        mqBatchDeliveryBuffer.add(notifyMerchantBO);
    }

    /**
     * 商家入驻提交资料通知
     */
    @Override
    @Async("taskExecutor")
    public void notifyMechantSettlementThread(PayMerchantInfoDO payMerchantInfoDO) {
        WxMpTemplateMessage wxMpTemplateMessage = new WxMpTemplateMessage();
        wxMpTemplateMessage.setTemplateId(notifyMechantSettlementTemplateId);
        wxMpTemplateMessage.setToUser(payMerchantInfoDO.getContactWxOpenid());
        List<WxMpTemplateData> data = new ArrayList<>();
        data.add(new WxMpTemplateData("first", payMerchantInfoDO.getEnterpriseName()));
        wxMpTemplateMessage.setData(data);
        wxMpTemplateMessage.setUrl("http://www.mayikt.com");
        try {
            String appId = wxMpProperties.getConfigs().get(0).getAppId();
            WxMpTemplateMsgService templateMsgService = WxMpConfiguration.getMpServices().get(appId).getTemplateMsgService();
            String result = templateMsgService.sendTemplateMsg(wxMpTemplateMessage);
            log.info("result:{}", result);
        } catch (Exception e) {
            // 记录错误日志
            log.error("e:{}", e);
        }
    }
}
