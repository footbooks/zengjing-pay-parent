package com.mayikt.pay.strategy.impl;

import com.mayikt.pay.entity.PaymentChannelDO;
import com.mayikt.pay.entity.PaymentInfoDO;
import com.mayikt.pay.strategy.PayInfoStrategy;
import org.springframework.stereotype.Component;

@Component
public class WeChatInfoStrategy implements PayInfoStrategy {
    @Override
    public String toPay(PaymentInfoDO paymentInfoDO, PaymentChannelDO paymentChannelDO) {
        return "调用微信支付接口";
    }

    @Override
    public boolean payIdByPayResult(PaymentInfoDO paymentInfoDO) {
        return false;
    }
}
