package com.fibo.rule.test.mall.vo;

import com.fibo.rule.test.mall.enums.AmountTypeEnum;
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
public class AmountStepVo {

    /**折扣类型*/
    private AmountTypeEnum amountType;
    /**上一步的订单总价格*/
    private BigDecimal prePrice;
    /**价格变动值*/
    private BigDecimal priceChange;
    /**这步价格计算后的订单总价格*/
    private BigDecimal currPrice;
    /**价格步骤描述*/
    private String stepDesc;

}
