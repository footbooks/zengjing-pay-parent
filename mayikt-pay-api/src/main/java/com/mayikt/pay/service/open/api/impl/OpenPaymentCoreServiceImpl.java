package com.mayikt.pay.service.open.api.impl;

import ch.qos.logback.core.joran.util.beans.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mayikt.pay.constant.Constant;
import com.mayikt.pay.dto.req.PaymentChannelReqDTO;
import com.mayikt.pay.dto.req.PaymentInfoDTO;
import com.mayikt.pay.dto.req.PaymentInfoReqDTO;
import com.mayikt.pay.entity.PaymentChannelDO;
import com.mayikt.pay.entity.PaymentInfoDO;
import com.mayikt.pay.mapper.PaymentChannelMapper;
import com.mayikt.pay.mapper.PaymentInfoMapper;
import com.mayikt.pay.service.open.api.OpenPaymentCoreService;
import com.mayikt.pay.utils.RedisUtils;
import com.mayikt.pay.utils.TokenUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class OpenPaymentCoreServiceImpl implements OpenPaymentCoreService {
    @Autowired
    private PaymentInfoMapper paymentInfoMapper;
    @Autowired
    private PaymentChannelMapper paymentChannelMapper;
    @Override
    @Transactional
    public String createPayToken(PaymentInfoReqDTO paymentInfoReqDTO) {
        //1.dto转化为do
        PaymentInfoDO paymentInfoDO = new PaymentInfoDO();
        BeanUtils.copyProperties(paymentInfoReqDTO,paymentInfoDO);
        //2.插入数据
        int insertResult = paymentInfoMapper.insert(paymentInfoDO);
        if (!(insertResult > Constant.RESULT_INSERT_ZERO)){
            return Constant.RESULT_STRING_NULL;
        }
        //3.生成对应的支付令牌
        Integer payId = paymentInfoDO.getId();
        String payToken = TokenUtils.getToken(Constant.PAY_PREFIX);
        RedisUtils.setString(payToken,String.valueOf(payId));
        return payToken;
    }

    @Override
    public PaymentInfoDO getPayToken(String payToken) {
        String payIdStr = RedisUtils.getString(payToken);
        if (StringUtils.isEmpty(payIdStr)){
            return null;
        }
        int payId = Integer.parseInt(payIdStr);
        PaymentInfoDO paymentInfoDO = paymentInfoMapper.selectById(payId);
        return paymentInfoDO;
    }

    @Override
    public PaymentInfoDO getByOrderId(String orderId) {
        QueryWrapper<PaymentInfoDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id",orderId);
        PaymentInfoDO paymentInfoDO = paymentInfoMapper.selectOne(queryWrapper);
        return paymentInfoDO;
    }

    @Override
    @Transactional
    public int updatePaymentStatus(PaymentInfoDO paymentInfoDO, Integer paymentStatus) {
        paymentInfoDO.setPaymentStatus(paymentStatus);
        int result = paymentInfoMapper.updateById(paymentInfoDO);
        return result;
    }

    @Override
    public int updatePaymentStatusOk(PaymentInfoDO paymentInfoDO) {
        paymentInfoDO.setPaymentStatus(Constant.PAY_PAYMENT_COMPLETED);
        int result = paymentInfoMapper.updateById(paymentInfoDO);
        return result;
    }

}
