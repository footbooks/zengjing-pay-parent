package com.mayikt.pay.template;

import com.mayikt.pay.bo.NotifyMerchantBO;
import com.mayikt.pay.entity.PayMerchantInfoDO;
import com.mayikt.pay.entity.PaymentInfoDO;
import com.mayikt.pay.enumclass.PaymentEnum;
import com.mayikt.pay.holder.PaymentInfoHolder;
import com.mayikt.pay.service.PayMerchantInfoService;
import com.mayikt.pay.service.PayMessageTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Date;

public abstract class  AbstractPayCallbackTemplate {
    @Autowired
    private PayMessageTemplate payMessageTemplate;
    @Autowired
    private PayMerchantInfoService payMerchantInfoService;
    /**
     * 异步回调处理
     */
    public String asynCallback(){
        //1.记录日志(相同）
       addPayLog();
        //2.验证签名（参数验证不同）
        boolean verifySignature = verifySignature();
        if (!verifySignature){
            //返回失败参数给第三方接口
            return resultFail();
        }
        //3.处理该参数业务逻辑（不同的）
        String result = asyncService();
        //4.使用mq异步的方式 发送通知给商户端支付结果 和 （通知回调）
        //实际支付结果告诉给商户端
        if (resultOK().equals(result)){
            notifyPaymentResult();
        }
        return result;
    }

    protected abstract String asyncService();

    protected abstract String resultOK();
    protected abstract String resultFail();
    protected abstract boolean verifySignature();

    public void addPayLog(){
        
    }

    private void notifyPaymentResult(){
        PaymentInfoDO paymentInfoDO = PaymentInfoHolder.get();
        if (paymentInfoDO == null){
            return;
        }
        String merchantId = paymentInfoDO.getMerchantId();
        PayMerchantInfoDO payMerchantInfoDO = payMerchantInfoService.getByMerchantIdInfo(merchantId);
        String contactWxOpenid = payMerchantInfoDO.getContactWxOpenid();
        BigDecimal payAmount = paymentInfoDO.getPayAmount();
        NotifyMerchantBO notifyMerchantBO = new NotifyMerchantBO(merchantId, contactWxOpenid, payAmount, new Date(),
                PaymentEnum.find(paymentInfoDO.getPaymentChannel()), paymentInfoDO.getPartyPayId());
        //发送微信公众号消息模板，消息推送
        payMessageTemplate.notifyMerchantPaymentResultsThread(notifyMerchantBO);
    }
}
