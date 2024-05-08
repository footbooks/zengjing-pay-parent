package com.mayikt.pay.service.open.api;

import com.mayikt.pay.entity.PaymentChannelDO;

public interface PaymentChannelService {
    PaymentChannelDO getByChannelIdInfo(String channelId);
}
