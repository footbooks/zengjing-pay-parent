package com.mayikt.pay.job.service.impl;

import com.mayikt.pay.job.entity.AggregateAmountDO;
import com.mayikt.pay.job.mapper.PaymentInfoMapper;
import com.mayikt.pay.job.service.PaymentJobDailyReportService;
import com.mayikt.pay.job.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * @author 余胜军
 * @ClassName PaymentJobDailyReportServiceImpl
 */
@Service
@Slf4j
public class PaymentJobDailyReportServiceImpl implements PaymentJobDailyReportService {
    @Autowired
    private PaymentInfoMapper paymentInfoMapper;

    private static final Integer PAGE_SIZE = 2;

    @Override
    public List<AggregateAmountDO> merchantTransactionAmount(Integer shardIndex) {
        // 1.获取昨日的时间
        String yesterdayTime = DateUtils.yesterdayTime();
        // 2.获取本分片中昨日交易额数据
        List<AggregateAmountDO> aggregateAmountList = paymentInfoMapper.merchantTransactionAmount
                (yesterdayTime, shardIndex * PAGE_SIZE);
        return aggregateAmountList;
    }


    @Async("taskExecutor")
    public void asynSendMerchantTransactionAmount(List<AggregateAmountDO>
                                                          aggregateAmountPartitionList) {
        for (AggregateAmountDO aggregateAmount :
                aggregateAmountPartitionList) {
            String merchantId = aggregateAmount.getMerchantId();
            BigDecimal payAmount = aggregateAmount.getPayAmount();
            log.info("merchantId:{},payAmount:{}",
                    merchantId, payAmount);
        }
    }
}
