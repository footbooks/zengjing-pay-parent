package com.mayikt.pay.strategy;

import com.mayikt.pay.entity.PaymentChannelDO;
import com.mayikt.pay.entity.PaymentInfoDO;

public interface PayInfoStrategy {
    String toPay(PaymentInfoDO paymentInfoDO, PaymentChannelDO paymentChannelDO);

    /**
     * 同步支付状态
     * @param paymentInfoDO
     * @return
     */
    boolean payIdByPayResult(PaymentInfoDO paymentInfoDO);
}
