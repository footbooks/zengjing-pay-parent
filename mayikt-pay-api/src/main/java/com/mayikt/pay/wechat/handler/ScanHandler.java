package com.mayikt.pay.wechat.handler;

import java.util.Map;

import com.mayikt.pay.service.PayMerchantQrCodeService;
import com.mayikt.pay.wechat.builder.TextBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;

@Component
public class ScanHandler extends AbstractHandler {
    @Autowired
    private PayMerchantQrCodeService payMerchantQrCodeService;
    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMpXmlMessage, Map<String, Object> map,
                                    WxMpService wxMpService, WxSessionManager wxSessionManager) throws WxErrorException {
        // 扫码事件处理
        boolean result = payMerchantQrCodeService.scanningRelevanceOpenId(wxMpXmlMessage);
        return result ?
                setResult("关联成功", wxMpXmlMessage, wxMpService) :
                setResult("关联失败,该二维码已失效", wxMpXmlMessage, wxMpService);
    }
    private WxMpXmlOutMessage setResult(String content, WxMpXmlMessage wxMessage,
                                        WxMpService service) {
        return new TextBuilder().build(content, wxMessage, service);
    }
}
