package com.matike.pay.consumer.service;


import com.matike.pay.bo.NotifyMerchantBO;

import java.util.concurrent.Future;

public interface PayMessageTemplate {

    Boolean notifyMerchantPaymentResults(NotifyMerchantBO notifyMerchantBO);
}