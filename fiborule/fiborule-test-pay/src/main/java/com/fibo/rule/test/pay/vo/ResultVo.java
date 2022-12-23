package com.fibo.rule.test.pay.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

/**
 * <p>折扣信息实体</p>
 *
 * @author JPX
 * @since 2022-12-14 13:27
 */
@Data
@AllArgsConstructor
public class ResultVo {

    /**金额奖励*/
    private BigDecimal awardMoney;
    /**积分奖励*/
    private BigDecimal awardPoints;
    /**随机奖励*/
    private String randomAward;
    /**奖励过程描述*/
    private String stepDesc;

}
