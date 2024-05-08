package com.mayikt.pay.service;

import com.mayikt.pay.dto.resp.PayMerchantQrCodeRespDto;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;

import java.util.Map;

public interface PayMerchantQrCodeService {
    PayMerchantQrCodeRespDto generateQrCode();
    boolean scanningRelevanceOpenId(WxMpXmlMessage wxMpXmlMessage);
    Boolean getQrCodeTokenResult(String qrCodetoken);
}
