package com.mayikt.pay.service;

import com.mayikt.pay.entity.MerchantSecretKeyDO;

public interface MerchantSecretKeyService {
    /**
     * 组合条件查询数据库
     * 如果能获取到数据  账号密码没问题
     * @param appId
     * @param appKey
     * @return
     */
    MerchantSecretKeyDO getAppIdKeyMerchantSecret(String appId,String appKey);
}
