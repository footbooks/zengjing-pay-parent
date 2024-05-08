package com.mayikt.pay.job.entity;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author 余胜军
 * @ClassName AggregateAmount
 */
@Data
public class AggregateAmountDO {
    private String merchantId;
    private BigDecimal payAmount;

}