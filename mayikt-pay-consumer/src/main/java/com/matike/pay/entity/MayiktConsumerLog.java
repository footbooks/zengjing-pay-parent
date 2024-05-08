package com.matike.pay.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * <p>
 *
 * </p>
 *
 * @author mayikt
 * @since 2023-02-16
 */
@TableName("mayikt_consumer_log")
@Data
public class MayiktConsumerLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String messageId;

    private String relationId;

    private String remarks;

    private LocalDateTime createDate;

    private LocalDateTime updateDate;

    public MayiktConsumerLog() {

    }

    public MayiktConsumerLog( String messageId, String relationId) {
        this.messageId = messageId;
        this.relationId = relationId;
        this.remarks = remarks;
    }

}