package com.mayikt.pay.service;

import com.mayikt.pay.bo.NotifyMerchantBO;
import com.mayikt.pay.entity.PayMerchantInfoDO;

import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.Future;

public interface PayMessageTemplate {

    Future<Boolean> notifyMerchantPaymentResultsThread(NotifyMerchantBO notifyMerchantBO);
    void notifyMerchantPaymentResultsMq(NotifyMerchantBO notifyMerchantBO);

    /**
     * 批量投递消息
     * @param notifyMerchantBO
     */
    void notifyMerchantPaymentResultsBatchMq(NotifyMerchantBO notifyMerchantBO);

    void notifyMechantSettlementThread(PayMerchantInfoDO payMerchantInfoDO);
}