package com.mayikt.pay.constant;

public interface Constant {
    Integer QINGYUNKE_RESULT_OK = 0;
    Object VALUE_IS_NULL = null;

    Integer DEFAULT_VALUE_ZERO = 0;
    /**
     * 未审核状态
     */
    Integer MERCHANT_UNAPPROVED_STATUS = 0;
    /**
     * 审核成功
     */
    Integer MERCHANT_SUCCESS_STATUS = 1;
    /**
     * 审核失败
     */
    Integer MERCHANT_FAIL_STATUS = 2;
    Integer RESULT_INSERT_ZERO = 0;
    String RESULT_STRING_NULL = null;
    String PAY_PREFIX = "mayikt.pay.";
    Integer PAY_PAYMENT_COMPLETED = 1;
    Integer ALI_PAY_CODE_OK = 200;

    String ALI_PAY_TRADESTATUS_SUCCESS = "TRADE_SUCCESS";

    String ALI_PAY_QUERY_RESPONSE_CODE = "10000";
}