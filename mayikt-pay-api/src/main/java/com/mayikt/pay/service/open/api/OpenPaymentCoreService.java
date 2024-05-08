package com.mayikt.pay.service.open.api;

import com.mayikt.pay.dto.req.PaymentChannelReqDTO;
import com.mayikt.pay.dto.req.PaymentInfoDTO;
import com.mayikt.pay.dto.req.PaymentInfoReqDTO;
import com.mayikt.pay.entity.PaymentInfoDO;

public interface OpenPaymentCoreService {
    String createPayToken(PaymentInfoReqDTO paymentInfoReqDTO);

    PaymentInfoDO getPayToken(String payToken);

    PaymentInfoDO getByOrderId(String orderId);

    int updatePaymentStatus(PaymentInfoDO paymentInfoDO,Integer paymentStatus);

    int updatePaymentStatusOk(PaymentInfoDO paymentInfoDO);
}
