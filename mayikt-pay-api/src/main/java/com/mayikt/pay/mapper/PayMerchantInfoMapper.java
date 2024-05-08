package com.mayikt.pay.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mayikt.pay.entity.PayMerchantInfoDO;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zengjing
 * @since 2023-12-30
 */
public interface PayMerchantInfoMapper extends BaseMapper<PayMerchantInfoDO> {
    @Update("update pay_merchant_info set \n" +
            "contact_wx_openid=null where contact_wx_openid=#{openid};")
    int updateOpenidIsNull(String openid);
}
