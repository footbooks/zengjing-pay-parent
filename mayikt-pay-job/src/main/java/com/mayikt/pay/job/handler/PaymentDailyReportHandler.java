package com.mayikt.pay.job.handler;


import com.google.common.collect.Lists;
import com.mayikt.pay.job.entity.AggregateAmountDO;
import com.mayikt.pay.job.service.PaymentJobDailyReportService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author 余胜军
 * @ClassName PaymentDailyReportHandler
 */
@Component
@Slf4j
public class PaymentDailyReportHandler {
    @Value("${server.port}")
    private String serverPort;
    private static final Integer PARTION_SIZE = 1;
    @Autowired
    private PaymentJobDailyReportService paymentJobDailyReportService;
    @XxlJob("payDailyReportHandler")
    public void payDailyReport() {
        int shardIndex = XxlJobHelper.getShardIndex();
        int shardTotal = XxlJobHelper.getShardTotal();
        log.info("分片参数：当前分片序号 = {}, 总分片数 = {},serverPort:{}", shardIndex, shardTotal, serverPort);
        //1.查询到昨日所有商户的总金额
        // 1.查询昨日交易数据
        List<AggregateAmountDO> aggregateAmountList = paymentJobDailyReportService.
                merchantTransactionAmount(shardIndex);
        log.info("serverPort:{},昨日交易额数据:{}", serverPort
                , aggregateAmountList);
        // 2.多线程开始分批处理
        List<List<AggregateAmountDO>>
                aggregateAmountPartitionListPartition
                = Lists.partition(aggregateAmountList, PARTION_SIZE);
        // 3.多线程分批开始处理数据（整合线程池）
        for (List<AggregateAmountDO> merchantAmountList :
                aggregateAmountPartitionListPartition) {
            // 多线程异步处理
            paymentJobDailyReportService.
                    asynSendMerchantTransactionAmount(merchantAmountList);
        }
        return;
    }
}