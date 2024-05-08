package com.mayikt.pay.service;

import com.mayikt.pay.entity.PayMerchantInfoDO;

public interface PayMerchantInfoService {
    /**
     * 新增企业入驻信息 默认未审核状态
     * @param payMerchantInfo
     * @return
     */
    boolean addPayMerchantInfo(PayMerchantInfoDO payMerchantInfo);
    /**
     * 根据商家信用代码查询商家信息
     */
    PayMerchantInfoDO getBySocialCreditCodePayMerchantInfo(String socialCreditCode);
    PayMerchantInfoDO getByMerchantIdInfo(String merchantId);
}
