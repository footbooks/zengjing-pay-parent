package com.mayikt.pay.dto.resp;

import lombok.Data;

@Data
public class PayMerchantQrCodeRespDto {
    /**
     * token
     */
    private String qrCodeToken;
    /**
     * 二维码生成链接地址
     */
    private String url;

    public PayMerchantQrCodeRespDto(String qrCodetoken, String url) {
        this.qrCodeToken = qrCodetoken;
        this.url = url;
    }

    public PayMerchantQrCodeRespDto() {
    }
}
