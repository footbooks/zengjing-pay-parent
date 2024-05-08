package com.mayikt.pay.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 支付渠道
 * </p>
 *
 * @author mayikt
 * @since 2023-03-21
 */
@TableName("payment_channel")
@Data
public class PaymentChannelDO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    /**
     * 渠道名称
     */
    private String channelName;

    /**
     * 渠道ID
     */
    private String channelId;

    /**
     * 同步回调URL
     */
    private String syncUrl;

    /**
     * 异步回调URL
     */
    private String asynUrl;

    /**
     * 公钥
     */
    private String publicKey;

    /**
     * 私钥
     */
    private String privateKey;

    /**
     * 乐观锁
     */
    private Integer revision;

    /**
     * 创建人
     */
    private String createdBy;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;

    /**
     * 更新人
     */
    private String updatedBy;

    /**
     * 更新时间
     */
    private LocalDateTime updatedTime;

    private String payBeanId;

    private String synCallbackBeanId;

    private Integer isDelete;


}
