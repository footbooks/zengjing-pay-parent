package com.mayikt.pay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mayikt.pay.constant.Constant;
import com.mayikt.pay.entity.PayMerchantInfoDO;
import com.mayikt.pay.mapper.PayMerchantInfoMapper;
import com.mayikt.pay.service.PayMerchantInfoService;
import com.mayikt.pay.utils.SnowflakeId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class payMerchantInfoServiceImpl implements PayMerchantInfoService {
    @Autowired
    private PayMerchantInfoMapper payMerchantInfoMapper;
    @Override
    public boolean addPayMerchantInfo(PayMerchantInfoDO payMerchantInfo) {
        // 默认状态为0 未审核
        payMerchantInfo.setAuditStatus(Constant.MERCHANT_UNAPPROVED_STATUS);
        // 生成一个唯一全局的id
        payMerchantInfo.setMerchantId(SnowflakeId.getIdStr());
        return payMerchantInfoMapper.insert(payMerchantInfo)
                > 0 ? Boolean.TRUE : Boolean.FALSE;
    }
    @Override
    public PayMerchantInfoDO getBySocialCreditCodePayMerchantInfo(String socialCreditCode) {
        QueryWrapper<PayMerchantInfoDO> payMerchantInfoDOQueryWrapper = new QueryWrapper<>();
        payMerchantInfoDOQueryWrapper.eq("social_credit_code", socialCreditCode);
        PayMerchantInfoDO payMerchantInfo = payMerchantInfoMapper.selectOne(payMerchantInfoDOQueryWrapper);
        return payMerchantInfo;
    }

    @Override
    public PayMerchantInfoDO getByMerchantIdInfo(String merchantId) {
        QueryWrapper<PayMerchantInfoDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("merchant_id",merchantId);
        PayMerchantInfoDO payMerchantInfoDO = payMerchantInfoMapper.selectOne(queryWrapper);
        return payMerchantInfoDO;
    }
}
