package com.mayikt.pay.dto.req;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PayMerchantInfoReqDTO {

//    /**
//     * 商户id
//     */
//    private String merchantId;

    /**
     * 企业名称
     */
    private String enterpriseName;

    /**
     * 社会信用代码
     */
    private String socialCreditCode;

    /**
     * 营业执照图片
     */
    private String businessLicenseUrl;

    /**
     * 法人
     */
    private String juridicalPerson;

    /**
     * 法人身份证信息
     */
    private String juridicalPersonUrl;

    /**
     * 联系人s
     */
    private String contacts;

    /**
     * 联系人手机号码
     */
    private String contactNumber;

    /**
     * 联系人微信openid
     */
    private String contactWxOpenid;
    /**
     * 二维码token参数
     */
    private String qrCodeToken;
}
