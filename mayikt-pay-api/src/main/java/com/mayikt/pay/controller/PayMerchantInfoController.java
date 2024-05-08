package com.mayikt.pay.controller;

import com.mayikt.pay.api.base.BaseApiController;
import com.mayikt.pay.api.base.BaseResponse;
import com.mayikt.pay.dto.req.PayMerchantInfoReqDTO;
import com.mayikt.pay.entity.PayMerchantInfoDO;
import com.mayikt.pay.service.PayMerchantInfoService;
import com.mayikt.pay.service.PayMessageTemplate;
import com.mayikt.pay.utils.RedisUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/merchant")
@CrossOrigin
@Api(tags = "商家接口")
public class PayMerchantInfoController extends BaseApiController {
    @Autowired
    private PayMerchantInfoService payMerchantInfoService;
    @Autowired
    private PayMessageTemplate payMessageTemplate;
    /**
     * 商家入驻接口
     */
    @PostMapping("/merchantSettlement")
    @ApiOperation(value = "商家入驻接口",notes = "商家入驻")
    public BaseResponse<String> merchantSettlement(
            @RequestBody PayMerchantInfoReqDTO payMerchantInfoReqDTO) {
        //参数验证
        if (payMerchantInfoReqDTO == null){
            return setResultError("payMerchantInfoReqDTO is null");
        }
        //根据企业信用代码作为全局id 插入时防止重复
        String socialCreditCode = payMerchantInfoReqDTO.getSocialCreditCode();
        PayMerchantInfoDO payMerchantInfoDb =
                payMerchantInfoService.
                        getBySocialCreditCodePayMerchantInfo(socialCreditCode);
        if (payMerchantInfoDb != null) {
            return setResultError("请勿重复提交");
        }
        // dto转化成do
        PayMerchantInfoDO payMerchantInfoDO = new PayMerchantInfoDO();
        BeanUtils.copyProperties(payMerchantInfoReqDTO, payMerchantInfoDO);
        //获取用户的openId
        String qrCodeToken = payMerchantInfoReqDTO.getQrCodeToken();
        if (!StringUtils.isEmpty(qrCodeToken)){
            payMerchantInfoDO.setContactWxOpenid(RedisUtils.getString(qrCodeToken));
        }
        // 插入数据
        payMerchantInfoDO.setCreateDate(new Date());
        payMerchantInfoDO.setUpdateDate(new Date());
        boolean insertResult = payMerchantInfoService.addPayMerchantInfo(payMerchantInfoDO);
        if (insertResult) {
            // 商家提交资料---发送消息模板推送
            payMessageTemplate.notifyMechantSettlementThread(payMerchantInfoDO);
        }
        return insertResult ? setResultSuccess() : setResultError();
    }
}
