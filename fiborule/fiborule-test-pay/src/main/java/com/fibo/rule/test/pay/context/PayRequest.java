package com.fibo.rule.test.pay.context;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * <p>价格计算结果</p>
 *
 * @author JPX
 * @since 2022/12/14 11:16
 */
@Data
public class PayRequest {

    /**
     * 充值金额
     */
    private BigDecimal payAmount;
    
    /**
     * 充值时间
     */
    private LocalDate payTime;


}
