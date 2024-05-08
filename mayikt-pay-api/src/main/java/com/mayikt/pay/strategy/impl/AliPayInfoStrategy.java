package com.mayikt.pay.strategy.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.mayikt.pay.config.AlipayConfig;
import com.mayikt.pay.constant.Constant;
import com.mayikt.pay.entity.PaymentChannelDO;
import com.mayikt.pay.entity.PaymentInfoDO;
import com.mayikt.pay.service.open.api.OpenPaymentCoreService;
import com.mayikt.pay.strategy.PayInfoStrategy;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Component
@Slf4j
public class AliPayInfoStrategy implements PayInfoStrategy {
    private         //获得初始化的AlipayClient
    AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id, AlipayConfig.merchant_private_key, "json", AlipayConfig.charset, AlipayConfig.alipay_public_key, AlipayConfig.sign_type);

    @Autowired
    private OpenPaymentCoreService openPaymentCoreService;
    @SneakyThrows
    @Override
    public String toPay(PaymentInfoDO paymentInfoDO, PaymentChannelDO paymentChannelDO) {
        //设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(paymentChannelDO.getSyncUrl());
        alipayRequest.setNotifyUrl(paymentChannelDO.getAsynUrl());

        //商户订单号，商户网站订单系统中唯一订单号，必填
        String out_trade_no = paymentInfoDO.getOrderId();
        //付款金额，必填
        BigDecimal total_amount = paymentInfoDO.getPayAmount();
        //订单名称，必填
        String subject = paymentInfoDO.getOrderName();
        //商品描述，可空
        String body = paymentInfoDO.getOrderBody();

        alipayRequest.setBizContent("{\"out_trade_no\":\"" + out_trade_no + "\","
                + "\"total_amount\":\"" + total_amount + "\","
                + "\"subject\":\"" + subject + "\","
                + "\"body\":\"" + body + "\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

        //若想给BizContent增加其他可选请求参数，以增加自定义超时时间参数timeout_express来举例说明
        //alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
        //		+ "\"total_amount\":\""+ total_amount +"\","
        //		+ "\"subject\":\""+ subject +"\","
        //		+ "\"body\":\""+ body +"\","
        //		+ "\"timeout_express\":\"10m\","
        //		+ "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
        //请求参数可查阅【电脑网站支付的API文档-alipay.trade.page.pay-请求参数】章节

        //请求
        String result = alipayClient.pageExecute(alipayRequest).getBody();
        return result;
    }

    @Override
    public boolean payIdByPayResult(PaymentInfoDO paymentInfoDO) {
        AlipayTradeQueryRequest alipayRequest =
                new AlipayTradeQueryRequest();
        AlipayTradeQueryModel model = new AlipayTradeQueryModel();
        model.setOutTradeNo(paymentInfoDO.getOrderId());
        model.setTradeNo(paymentInfoDO.getPartyPayId());
        alipayRequest.setBizModel(model);
        try {
            AlipayTradeQueryResponse alipayResponse =
                    alipayClient.execute(alipayRequest);
            log.info("<alipay_response:{}>", alipayResponse);
            // 2.解析json数据 比对金额是否一致
            String respCode = alipayResponse.getCode();
            if (!respCode.equals(
                    Constant.ALI_PAY_QUERY_RESPONSE_CODE)) {
                return Boolean.FALSE;
            }
            // 判断支付金额是否与表中一致
            BigDecimal totalAmount = new BigDecimal(alipayResponse.getTotalAmount());
            String outTradeNo = alipayResponse.getOutTradeNo();
            String tradeNo = alipayResponse.getTradeNo();
            String tradeStatus = alipayResponse.getTradeStatus();
            if (!(paymentInfoDO.getPayAmount()
                    .equals(totalAmount)
                    && Constant.ALI_PAY_TRADESTATUS_SUCCESS
                    .equals(tradeStatus)
            )) {
                // 用户实际支付金额与表中存在金额不一致
                log.error("tradeNo:{},outTradeNo:{} 实际金额为：{}", tradeNo, outTradeNo, totalAmount);
                return Boolean.FALSE;
            }
            // 用户实际支付金额与表中存在金额一致的情况下 判断状态 是否已经支付
            Integer paymentStatus = paymentInfoDO.getPaymentStatus();
            if (Constant.PAY_PAYMENT_COMPLETED.equals(
                    paymentStatus
            )) {
                return Boolean.TRUE;
            }
            // 则更新表中数据状态
            paymentInfoDO.setPartyPayId(tradeNo);
            int updateResult = openPaymentCoreService.updatePaymentStatusOk(paymentInfoDO);
            return updateResult > Constant.DEFAULT_VALUE_ZERO;
        } catch (Exception e) {
            log.error("e:{}", e);
            return Boolean.FALSE;
        }

    }

}
