package com.mayikt.pay.controller.open.api;

import cn.hutool.extra.spring.SpringUtil;
import com.mayikt.pay.entity.PaymentChannelDO;
import com.mayikt.pay.service.open.api.PaymentChannelService;
import com.mayikt.pay.template.AbstractPayCallbackTemplate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/callback")
public class PayCallbackCoreController {
    @Autowired
    private PaymentChannelService paymentChannelService;
    /**
     * 1.同步回调----以浏览器的形式，回调通知 解析报文参数
     * 2.异步回调----第三方支付系统单独发送rest请求通知
     */
    @RequestMapping("/synchCallback")
    public String syncCallback(){
        return "ok";
    }
    @RequestMapping("/asynchCallback/{channelId}")
    public String asynchCallback(@PathVariable("channelId") String channelId){
        //参数验证
        if (StringUtils.isEmpty(channelId)){
            return "channelId is error";
        }
        //根据渠道id查询渠道信息
        PaymentChannelDO paymentChannelDO = paymentChannelService.getByChannelIdInfo(channelId);
        if (paymentChannelDO== null){
            return "渠道关闭或者错误";
        }
        //获取渠道对应的回调beanId
        AbstractPayCallbackTemplate callbackTemplate =
                SpringUtil.getBean(paymentChannelDO.getSynCallbackBeanId(), AbstractPayCallbackTemplate.class);
        return callbackTemplate.asynCallback();
    }
}
