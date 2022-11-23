package com.fibo.rule.test.context;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>奖励结果context</p>
 *
 * @author JPX
 * @since 2022-11-22 10:56
 */
@Data
public class RewardContext {

    /**充值金额*/
    private BigDecimal rechargeNum;
    /**充值时间*/
    private Date rechargeDate;
    /**奖励金额*/
    private BigDecimal rewardMoney;
    /**奖励积分*/
    private Integer integral;

}
