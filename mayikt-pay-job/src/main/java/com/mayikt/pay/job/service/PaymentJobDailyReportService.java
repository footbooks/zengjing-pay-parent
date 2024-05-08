package com.mayikt.pay.job.service;

import com.mayikt.pay.job.entity.AggregateAmountDO;

import java.util.List;

public interface PaymentJobDailyReportService {

    /**
     * 查询每个商户 昨日交易金额
     *
     * @return
     */
    List<AggregateAmountDO> merchantTransactionAmount(Integer shardIndex);

    /**
     * 异步处理发送昨日商户交易金额
     */
    void asynSendMerchantTransactionAmount(List<AggregateAmountDO>
                                           aggregateAmountPartitionList);
}