package com.mayikt.pay.template.impl;

import com.alipay.api.internal.util.AlipaySignature;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mayikt.pay.config.AlipayConfig;
import com.mayikt.pay.constant.Constant;
import com.mayikt.pay.entity.PaymentInfoDO;
import com.mayikt.pay.enumclass.PaymentEnum;
import com.mayikt.pay.holder.PayThreadInfoHolder;
import com.mayikt.pay.holder.PaymentInfoHolder;
import com.mayikt.pay.mapper.PaymentInfoMapper;
import com.mayikt.pay.service.open.api.OpenPaymentCoreService;
import com.mayikt.pay.template.AbstractPayCallbackTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Component
@Slf4j
public class AliPayCallbackTemplateImpl extends AbstractPayCallbackTemplate {
    @Autowired
    private OpenPaymentCoreService openPaymentCoreService;
    @Override
    protected String asyncService() {
        //获取上一个流程验签成功参数
        Map<String, String> params = PayThreadInfoHolder.get();
        //获取订单号码
        String outTradeNo = params.get("out_trade_no");
        //根据订单号码查询表中是否存在该比信息
        PaymentInfoDO paymentInfoDO = openPaymentCoreService.getByOrderId(outTradeNo);
        if (paymentInfoDO == null){
            return resultFail();
        }
        //比较金额
        String totalAmount = params.get("total_amount");
        BigDecimal payAmount = paymentInfoDO.getPayAmount();
        if (!payAmount.equals(new BigDecimal(totalAmount))){
            //不相等，先告诉支付宝不继续重试  记录下来后期排查
            //记录为异常订单
            return resultOK();
        }
        PaymentInfoHolder.set(paymentInfoDO);
        //修改支付状态为已经支付  同步db状态
        //获取到第三方的支付id
        String tradeNo = params.get("trade_no");
        paymentInfoDO.setPartyPayId(tradeNo);
        //实际支付渠道
        paymentInfoDO.setPaymentChannel(PaymentEnum.ALIPAY.name());
        int result = openPaymentCoreService.updatePaymentStatus(paymentInfoDO, Constant.PAY_PAYMENT_COMPLETED);
        return result>Constant.DEFAULT_VALUE_ZERO ? resultOK():resultFail();
    }

    @Override
    protected String resultOK() {
        return "ok";
    }

    @Override
    protected String resultFail() {
        return "fail";
    }

    @Override
    protected boolean verifySignature() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            //获取支付宝GET过来反馈信息
            Map<String, String> params = new HashMap<String, String>();
            Map<String, String[]> requestParams = request.getParameterMap();
            for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
                String name = (String) iter.next();
                String[] values = (String[]) requestParams.get(name);
                String valueStr = "";
                for (int i = 0; i < values.length; i++) {
                    valueStr = (i == values.length - 1) ? valueStr + values[i]
                            : valueStr + values[i] + ",";
                }
                params.put(name, valueStr);
            }
//            params.remove("channelId");
//            boolean signVerified = AlipaySignature.rsaCheckV1(params, AlipayConfig.alipay_public_key, AlipayConfig.charset, AlipayConfig.sign_type); //调用SDK验证签名
//            //——请在这里编写您的程序（以下代码仅作参考）——
//            if (!signVerified) {
//                return Boolean.TRUE;
//            }
            //商户订单号
            String outTradeNo = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");
            //支付宝交易号
            String tradeNo = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");
            //付款金额
            String totalAmount = new String(request.getParameter("total_amount").getBytes("ISO-8859-1"), "UTF-8");
            PayThreadInfoHolder.add(params);
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error(">>e:{}<<", e);
            return Boolean.FALSE;
        }
    }
}
