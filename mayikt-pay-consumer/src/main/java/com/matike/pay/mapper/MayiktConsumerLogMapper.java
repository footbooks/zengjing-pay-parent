package com.matike.pay.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.matike.pay.entity.MayiktConsumerLog;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author mayikt
 * @since 2023-02-16
 */
public interface MayiktConsumerLogMapper extends BaseMapper<MayiktConsumerLog> {
    @Select("SELECT id as id ,message_id as messageid ,relation_id as \n" +
            "relationid,remarks as remarks,create_date as createdate,\n" +
            "update_date as updatedate\n" +
            "  from mayikt_consumer_log where message_id=#{messageId} ")
    MayiktConsumerLog getByMsgIdConsumerLog(String messageId);
}