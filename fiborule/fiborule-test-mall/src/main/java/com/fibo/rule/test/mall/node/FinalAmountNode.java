package com.fibo.rule.test.mall.node;

import com.fibo.rule.core.annotation.FiboBean;
import com.fibo.rule.core.node.FiboNode;
import com.fibo.rule.test.mall.context.PriceContext;
import com.fibo.rule.test.mall.vo.AmountStepVo;

import java.math.BigDecimal;

/**
 *<p>最终金额计算</p>
 *
 *@author JPX
 *@since 2022/12/14 15:30
 */
@FiboBean(name = "最终金额计算", desc = "最终金额计算")
public class FinalAmountNode extends FiboNode {
    @Override
    public void runnerStep() {
        //获取context
        PriceContext priceContext = this.getContextBean(PriceContext.class);
        BigDecimal finalPrice = new BigDecimal(0);
        for (AmountStepVo amountStepVo : priceContext.getAmountStepList()) {
            finalPrice = finalPrice.add(amountStepVo.getPriceChange());
        }
        priceContext.setFinalPrice(finalPrice);
    }
}
