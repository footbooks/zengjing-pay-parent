package com.mayikt.pay.controller;

import cn.hutool.extra.spring.SpringUtil;
import com.mayikt.pay.api.base.BaseApiController;
import com.mayikt.pay.api.base.BaseResponse;
import com.mayikt.pay.entity.PaymentChannelDO;
import com.mayikt.pay.entity.PaymentInfoDO;
import com.mayikt.pay.service.open.api.OpenPaymentCoreService;
import com.mayikt.pay.service.open.api.PaymentChannelService;
import com.mayikt.pay.strategy.PayInfoStrategy;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pay")
public class PaymentController extends BaseApiController {
    @Autowired
    private PaymentChannelService paymentChannelService;
    @Autowired
    private OpenPaymentCoreService openPaymentCoreService;
    @GetMapping("/payIdByPayResult/{channelId}")
    public BaseResponse<String> payIdByPayResult(
            @PathVariable("channelId") String channelId, String orderId){
        //1.参数验证
        if (StringUtils.isEmpty(channelId)){
            return setResultError("channelId is null");
        }
        if (StringUtils.isEmpty(orderId)){
            return setResultError("orderId is null");
        }
        //2.查找策略类
        PaymentChannelDO paymentChannelDO = paymentChannelService.getByChannelIdInfo(channelId);
        if (paymentChannelDO == null){
            return setResultError("channelId is error");
        }
        String payBeanId = paymentChannelDO.getPayBeanId();
        PayInfoStrategy payInfoStrategy = SpringUtil.getBean(payBeanId, PayInfoStrategy.class);
        //3.查询支付信息
        PaymentInfoDO paymentInfoDO = openPaymentCoreService.getByOrderId(orderId);
        if (paymentInfoDO == null){
            return setResultError("order is error");
        }
        //4.策略调用具体  主动根据订单号码同步状态
        boolean result = payInfoStrategy.payIdByPayResult(paymentInfoDO);
        return result? setResultSuccess():setResultError("同步状态失败");
    }
}
