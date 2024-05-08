package com.matike.pay.consumer.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.matike.pay.consumer.service.MayiktConsumerLogService;
import com.matike.pay.entity.MayiktConsumerLog;
import com.matike.pay.mapper.MayiktConsumerLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 余胜军
 * @ClassName MayiktConsumerLogServiceImpl
 */
@Service
public class MayiktConsumerLogServiceImpl implements MayiktConsumerLogService {
    @Autowired
    private MayiktConsumerLogMapper mayiktConsumerLogMapper;

    @Override
    public MayiktConsumerLog getByMsgIdConsumerLog(String messageId) {
        return mayiktConsumerLogMapper.getByMsgIdConsumerLog(messageId);
    }

    @Override
    public int addWechatTemplateConsumerLog(String templateMsgId, String msgId) {
        MayiktConsumerLog mayiktConsumerLog =
                new MayiktConsumerLog(msgId, templateMsgId);
        int result = mayiktConsumerLogMapper.insert(mayiktConsumerLog);
        return result;
    }
}