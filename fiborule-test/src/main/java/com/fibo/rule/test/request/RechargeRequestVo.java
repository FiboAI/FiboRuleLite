package com.fibo.rule.test.request;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p></p>
 *
 * @author JPX
 * @since 2022-11-22 11:35
 */
@Data
public class RechargeRequestVo {

    /**充值金额*/
    private BigDecimal rechargeNum;
    /**充值时间*/
    private Date rechargeDate;

}
