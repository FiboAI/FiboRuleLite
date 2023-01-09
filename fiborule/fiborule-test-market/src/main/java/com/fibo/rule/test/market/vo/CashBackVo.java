package com.fibo.rule.test.market.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * <p>返现结果</p>
 *
 * @author JPX
 * @since 2023-01-05 10:56
 */
@Data
public class CashBackVo {

    /**返现金额*/
    private BigDecimal cashBackAmount;
    /**返现支付方式 1：红包，2：消费冲抵*/
    private Integer cashbackPayType;

}
