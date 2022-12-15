package com.fibo.rule.test.mall.node;

import cn.hutool.core.util.StrUtil;
import com.fibo.rule.core.annotation.FiboBean;
import com.fibo.rule.core.annotation.FiboField;
import com.fibo.rule.core.node.FiboNode;
import com.fibo.rule.test.mall.context.PriceContext;
import com.fibo.rule.test.mall.enums.AmountTypeEnum;
import com.fibo.rule.test.mall.vo.DiscountVo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>满减券计算</p>
 *
 * @author JPX
 * @since 2022-12-14 14:15
 */
@Data
@FiboBean(name = "满减券计算", desc = "满减券计算")
public class FullDecrementNode extends FiboNode {

    @FiboField(name = "满足金额", desc = "满足金额")
    private BigDecimal fullValue;
    @FiboField(name = "减免金额", desc = "减免金额")
    private BigDecimal decrementValue;

    @Override
    public void runnerStep() {
        //获取context
        PriceContext priceContext = this.getContextBean(PriceContext.class);
        //不满足条件
        if(priceContext.getOriginalPrice().compareTo(fullValue) < 0) {
            return;
        }
        List<DiscountVo> discountList = priceContext.getDiscountList();
        //添加满减记录
        discountList.add(new DiscountVo(AmountTypeEnum.FULL_DECREMENT,
                BigDecimal.ZERO.subtract(decrementValue),
                StrUtil.format( "{}(满{}减{})", AmountTypeEnum.FULL_DECREMENT.getName(), fullValue, decrementValue)));
    }
}
