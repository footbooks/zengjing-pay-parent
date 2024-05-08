package com.mayikt.pay.dto.req;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 支付交易
 * </p>
 *
 * @author mayikt
 * @since 2023-03-21
 */

@Data
public class PaymentInfoReqDTO {
    /**
     * 商户编号
     */
    private String merchantId;
    /**
     * 支付金额
     */
    private BigDecimal payAmount;

    /**
     * 订单号码
     */
    private String orderId;
    /**
     * 订单名称
     */
    private String orderName;

    /**
     * 订单内容
     */
    private String orderBody;
}
