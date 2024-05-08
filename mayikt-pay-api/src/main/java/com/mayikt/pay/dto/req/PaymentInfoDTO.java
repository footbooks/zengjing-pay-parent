package com.mayikt.pay.dto.req;

import lombok.Data;

@Data
public class PaymentInfoDTO extends OpenBaseDTO{
    /**
     * 订单名称
     */
    private String orderName;
    /**
     * 支付金额
     */
    private String paymentAmount;
}
