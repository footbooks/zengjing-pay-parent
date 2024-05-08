package com.mayikt.pay.dto.req;

import lombok.Data;

/**
 * @author 余胜军
 * @ClassName PaymentChannelDTO
 */
@Data
public class PaymentChannelReqDTO {
    /**
     * 渠道ID
     */
    private String channelId;
    /**
     * 支付令牌
     */
    private String payToken;
}
