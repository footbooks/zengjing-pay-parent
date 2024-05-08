package com.mayikt.pay.controller;

import com.mayikt.pay.api.base.BaseApiController;
import com.mayikt.pay.api.base.BaseResponse;
import com.mayikt.pay.dto.resp.PayMerchantQrCodeRespDto;
import com.mayikt.pay.service.PayMerchantQrCodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/merchant")
@CrossOrigin
@Api(tags = "二维码接口")
public class PayMerchantQrCodeController extends BaseApiController {
    @Autowired
    private PayMerchantQrCodeService payMerchantQrCodeService;
    @GetMapping("/generateQrCode")
    @ApiOperation(value = "二维码接口", notes = "生成临时二维码")
    private BaseResponse<PayMerchantQrCodeRespDto> generateQrCode(){
        PayMerchantQrCodeRespDto payMerchantQrCodeRespDto = payMerchantQrCodeService.generateQrCode();
        if (payMerchantQrCodeRespDto == null){
            log.error("临时二维码生成失败");
            return setResultError("payMerchantQrCodeRespDto is null");
        }
        return setResultSuccessData(payMerchantQrCodeRespDto);
    }

    @GetMapping("/getQrCodeTokenResult")
    @ApiOperation(value = "二维码扫码查询", notes = "根据token查询是否扫码过")
    public BaseResponse<String> getQrCodeTokenResult(String qrCodeToken){
        //验证参数
        if (StringUtils.isEmpty(qrCodeToken)){
            return setResultError("qrCodeToken is null");
        }
        Boolean result = payMerchantQrCodeService.getQrCodeTokenResult(qrCodeToken);
        return result ? setResultSuccess():setResultError();
    }

}
