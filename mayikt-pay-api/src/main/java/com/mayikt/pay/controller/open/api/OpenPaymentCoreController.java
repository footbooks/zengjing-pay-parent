package com.mayikt.pay.controller.open.api;

import cn.hutool.extra.spring.SpringUtil;
import com.mayikt.pay.api.base.BaseApiController;
import com.mayikt.pay.api.base.BaseResponse;
import com.mayikt.pay.dto.req.PaymentChannelReqDTO;
import com.mayikt.pay.dto.req.PaymentInfoDTO;
import com.mayikt.pay.dto.req.PaymentInfoReqDTO;
import com.mayikt.pay.entity.PaymentChannelDO;
import com.mayikt.pay.entity.PaymentInfoDO;
import com.mayikt.pay.service.open.api.OpenPaymentCoreService;
import com.mayikt.pay.service.open.api.PaymentChannelService;
import com.mayikt.pay.strategy.PayInfoStrategy;
import com.mayikt.pay.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;


@RestController
@RequestMapping("/api/v1")
@Slf4j
public class OpenPaymentCoreController extends BaseApiController {
    @Autowired
    private OpenPaymentCoreService openPaymentCoreService;
    @Autowired
    private PaymentChannelService paymentChannelService;
    @PostMapping("testToPay")
    public BaseResponse<String> testToPay(
            @RequestBody(required = false)
                    PaymentInfoDTO paymentInfoDTO) {
        return setResultSuccess();
    }

    /**
     * 商家通过后台形式将支付信息传递给极简支付
     * 极简支付返回对应的支付令牌
     */
    @PostMapping("/createPayToken")
    public BaseResponse<String> createPayToken(@RequestBody PaymentInfoReqDTO paymentInfoReqDTO){
        //1.验证参数
        if (paymentInfoReqDTO == null){
            return setResultError();
        }
        String merchantId = paymentInfoReqDTO.getMerchantId();
        if (StringUtils.isEmpty(merchantId)) {
            return setResultError("merchantId is null");
        }
        BigDecimal paymentAmount = paymentInfoReqDTO.getPayAmount();
        if (StringUtils.isEmpty(paymentAmount)) {
            return setResultError("paymentAmount is null");
        }
        //2.新增支付数据
        String payToken = openPaymentCoreService.createPayToken(paymentInfoReqDTO);
        if (StringUtils.isEmpty(payToken)){
            return setResultError("payToken is null");
        }
        log.info("<支付令牌>:{}",payToken);
        //3.返回token令牌
        return setResultSuccessData(payToken);
    }

    /**
     * 参数1：支付渠道
     * 参数2：支付令牌
     * @return
     */
    @PostMapping("/toPay")
    public BaseResponse<String> toPay(@RequestBody PaymentChannelReqDTO paymentChannelReqDTO){
        //1.参数验证
        if (paymentChannelReqDTO == null){
            return setResultError("paymentChannelReqDTO is null");
        }
        String channelId = paymentChannelReqDTO.getChannelId();
        if (StringUtils.isEmpty(channelId)){
            return setResultError("channelId is null");
        }
        String payToken = paymentChannelReqDTO.getPayToken();
        if (StringUtils.isEmpty(payToken)){
            return setResultError("payToken is null");
        }
        //2.根据支付令牌，获取支付参数
        PaymentInfoDO paymentInfoDO = openPaymentCoreService.getPayToken(payToken);
        if (paymentInfoDO == null){
            return setResultError("payToken is error");
        }
        //3.根据渠道id查询支付渠道
        PaymentChannelDO paymenntChannelDo = paymentChannelService.getByChannelIdInfo(channelId);
        if (paymenntChannelDo == null){
            return setResultError("channelId is error");
        }
        String payBeanId = paymenntChannelDo.getPayBeanId();
        PayInfoStrategy payInfoStrategy = SpringUtil.getBean(payBeanId, PayInfoStrategy.class);
        String toPayHtml = payInfoStrategy.toPay(paymentInfoDO,paymenntChannelDo);
        log.info("<toPayHtml>:{}",toPayHtml);
        return setResultSuccessData(toPayHtml);
    }
}