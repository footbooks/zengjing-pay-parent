package com.matike.pay.consumer.service;

import com.matike.pay.entity.MayiktConsumerLog;

public interface MayiktConsumerLogService {
    /**
     * 根据消息id查找消费记录
     *
     * @return
     */
    MayiktConsumerLog getByMsgIdConsumerLog(String messageId);

    /**
     * 插入调用微信模板消费接口日志
     */
    int addWechatTemplateConsumerLog(String wechatTemplateLog, String msgId);
}