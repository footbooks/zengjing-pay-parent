package com.mayikt.pay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mayikt.pay.entity.MerchantSecretKeyDO;
import com.mayikt.pay.mapper.MerchantSecretKeyMapper;
import com.mayikt.pay.service.MerchantSecretKeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MerchantSecretKeyServiceImpl implements MerchantSecretKeyService {
    @Autowired
    private MerchantSecretKeyMapper merchantSecretKeyMapper;
    @Override
    public MerchantSecretKeyDO getAppIdKeyMerchantSecret(String appId, String appKey) {
        QueryWrapper<MerchantSecretKeyDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("app_id",appId)
                .eq("app_key",appKey);
        return merchantSecretKeyMapper.selectOne(queryWrapper);
    }
}
