package com.mayikt.pay.service.impl;

import com.mayikt.pay.constant.Constant;
import com.mayikt.pay.dto.resp.PayMerchantQrCodeRespDto;
import com.mayikt.pay.service.PayMerchantQrCodeService;
import com.mayikt.pay.utils.RedisUtils;
import com.mayikt.pay.utils.TokenUtils;
import com.mayikt.pay.wechat.config.WxMpConfiguration;
import com.mayikt.pay.wechat.config.WxMpProperties;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpQrcodeService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Slf4j
public class PayMerchantQrCodeServiceImpl implements PayMerchantQrCodeService {
    @Autowired
    private WxMpProperties wxMpProperties;
    /**
     * key=token
     * value=二维码生成的链接地址，返回给前端，前端根据链接展示一个二维码
     * @return
     */
    @Override
    public PayMerchantQrCodeRespDto generateQrCode() {
        try{
            // 1.生成二维码sceneId
            String sceneIdStr = TokenUtils.getToken();
            String appId = wxMpProperties.getConfigs().get(Constant.DEFAULT_VALUE_ZERO).getAppId();
            WxMpQrcodeService qrcodeService = WxMpConfiguration.getMpServices().get(appId).getQrcodeService();
            // 2.生成二维码 有效期30天
            WxMpQrCodeTicket wxMpQrCodeTicket = qrcodeService.qrCodeCreateTmpTicket(sceneIdStr, 2592000);
            String url = wxMpQrCodeTicket.getUrl();
            return new PayMerchantQrCodeRespDto(sceneIdStr,url);
        }catch (Exception e){
            log.info("e:{}",e);
        }
        return null;
    }
    @Override
    public boolean scanningRelevanceOpenId(WxMpXmlMessage wxMpXmlMessage) {
        // 扫码事件处理
        // 1.获取二维码中的参数
        String eventKey = wxMpXmlMessage.getEventKey();
        if (StringUtils.isEmpty(eventKey)) {
            return Boolean.FALSE;
        }
        //2.获取用户的openid
        String openId = wxMpXmlMessage.getFromUser();
        //判断该二维码是否已经被人扫过了
        String redisOpenId = RedisUtils.getString(eventKey);
        if (openId.equals(redisOpenId)){
            return Boolean.TRUE;
        }
        if (!StringUtils.isEmpty(redisOpenId)){
            return Boolean.FALSE;
        }
        //3.redis中记录该key用户对应的openid
        RedisUtils.setString(eventKey, openId);
        return Boolean.TRUE;
    }

    @Override
    public Boolean getQrCodeTokenResult(String qrCodetoken) {
        //从redis中获取商家的openid
        String openId = RedisUtils.getString(qrCodetoken);
        return !StringUtils.isEmpty(openId) ? Boolean.TRUE:Boolean.FALSE;
    }
}
