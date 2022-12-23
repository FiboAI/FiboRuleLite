package com.fibo.rule.test.mall.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 *<p>发送消息</p>
 *
 *@author JPX
 *@since 2022/12/16 14:03
 */
@Service
@Slf4j
public class SendService {

    public boolean sendOriginalPrice(String orderNo, BigDecimal value) {
        //do send amount
        log.info("=======send original Price orderNo:{}, value:{}", orderNo, value);
        return true;
    }

    public boolean sendFinalPrice(String orderNo, BigDecimal value) {
        //do send point
        log.info("=======send final Price orderNo:{}, value:{}", orderNo, value);
        return true;
    }
}
