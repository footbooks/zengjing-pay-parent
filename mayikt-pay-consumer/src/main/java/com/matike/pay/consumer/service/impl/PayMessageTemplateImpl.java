package com.matike.pay.consumer.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.matike.pay.bo.NotifyMerchantBO;
import com.matike.pay.consumer.service.MayiktConsumerLogService;
import com.matike.pay.consumer.service.PayMessageTemplate;
import com.matike.pay.consumer.wechat.config.WxMpConfiguration;
import com.matike.pay.consumer.wechat.config.WxMpProperties;
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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

@Service
@Slf4j
public class PayMessageTemplateImpl implements PayMessageTemplate {
    @Autowired
    private WxMpProperties wxMpProperties;
    @Value("${zengjing.pay.notifyMerchantTemplateId}")
    private String notifyMerchantTemplateId;
    @Autowired
    private MayiktConsumerLogService mayiktConsumerLogService;
    @Override
    public Boolean notifyMerchantPaymentResults(NotifyMerchantBO notifyMerchantBO) {
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
            String templateMsgId = templateMsgService.sendTemplateMsg(wxMpTemplateMessage);
            log.info("templateMsgId:{}", templateMsgId);
            int result = mayiktConsumerLogService.addWechatTemplateConsumerLog(templateMsgId, notifyMerchantBO.getPaymentId());
            if (result > 0){
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        } catch (Exception e) {
            // 记录错误日志
            log.error("e:{}", e);
            return Boolean.FALSE;
        }
    }
}
