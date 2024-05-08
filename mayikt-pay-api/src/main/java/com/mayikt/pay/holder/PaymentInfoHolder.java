package com.mayikt.pay.holder;

import com.mayikt.pay.entity.PaymentInfoDO;

import java.util.Map;

public class PaymentInfoHolder {
    private static ThreadLocal<PaymentInfoDO> paramsThreadLocal =  new ThreadLocal<>();
    public static PaymentInfoDO get(){
        return paramsThreadLocal.get();
    }
    public static void set(PaymentInfoDO paymentInfoDO){
        paramsThreadLocal.set(paymentInfoDO);
    }
}
