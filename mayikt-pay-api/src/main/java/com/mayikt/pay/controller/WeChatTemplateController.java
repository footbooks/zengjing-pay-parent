package com.mayikt.pay.controller;

import com.mayikt.pay.bo.NotifyMerchantBO;
import com.mayikt.pay.buffer.MqBatchDeliveryBuffer;
import com.mayikt.pay.service.impl.PayMessageTemplateImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@RestController
@Slf4j
@CrossOrigin
@Api(tags = "微信模板接口")
public class WeChatTemplateController {
    /**
     * 测试代码
     */
    @Autowired
    private PayMessageTemplateImpl payMessageTemplateImpl;
    @Autowired
    private MqBatchDeliveryBuffer mqBatchDeliveryBuffer;

    /**
     * 测试代码
     */
    @GetMapping("/testSend")
    @ApiOperation(value = "发送微信模板接口",notes = "测试发送微信模板")
    public String testSend(String merchantId,
                           String merchantOpenId,
                           BigDecimal paymentAmount,
                           @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date payDate,
                           String paymentChannel, String paymentId) throws ExecutionException, InterruptedException {
//        Future<Boolean> booleanFuture = payMessageTemplateImpl.notifyMerchantPaymentResultsThread(notifyMerchantBO);
//        payMessageTemplateImpl.notifyMerchantPaymentResultsMq(new NotifyMerchantBO(merchantId, merchantOpenId, paymentAmount, payDate, paymentChannel, paymentId));
        payMessageTemplateImpl.notifyMerchantPaymentResultsBatchMq(new NotifyMerchantBO(merchantId, merchantOpenId, paymentAmount, payDate, paymentChannel, UUID.randomUUID().toString()));
        return "ok";
    }
}
