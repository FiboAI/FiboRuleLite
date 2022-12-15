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
public class DiscountVo {

    /**折扣类型*/
    private AmountTypeEnum amountType;
    /**价格变动值*/
    private BigDecimal priceChange;
    /**折扣描述*/
    private String desc;

}
