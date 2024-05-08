package com.mayikt.pay.service.open.api.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mayikt.pay.entity.PaymentChannelDO;
import com.mayikt.pay.mapper.PaymentChannelMapper;
import com.mayikt.pay.service.open.api.PaymentChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentChannelServiceImpl implements PaymentChannelService {
    @Autowired
    private PaymentChannelMapper paymentChannelMapper;
    @Override
    public PaymentChannelDO getByChannelIdInfo(String channelId) {
        QueryWrapper<PaymentChannelDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("channel_id",channelId);
        PaymentChannelDO paymentChannelDO = paymentChannelMapper.selectOne(queryWrapper);
        return paymentChannelDO;
    }
}
