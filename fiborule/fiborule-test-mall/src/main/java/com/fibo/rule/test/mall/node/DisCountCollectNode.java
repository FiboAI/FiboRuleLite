package com.fibo.rule.test.mall.node;

import com.fibo.rule.core.annotation.FiboBean;
import com.fibo.rule.core.node.FiboNode;
import com.fibo.rule.test.mall.context.PriceContext;
import com.fibo.rule.test.mall.vo.AmountStepVo;
import com.fibo.rule.test.mall.vo.DiscountVo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * <p>最大折扣金额计算</p>
 *
 * @author JPX
 * @since 2022-12-14 14:15
 */
@Data
@FiboBean(name = "最大折扣金额计算", desc = "从折扣列表中取最大折扣，计算折扣后金额")
public class DisCountCollectNode extends FiboNode {
    @Override
    public void runnerStep() {
        //获取context
        PriceContext priceContext = this.getContextBean(PriceContext.class);

        BigDecimal prePrice = priceContext.getLastestAmountStep().getCurrPrice();

        List<DiscountVo> discountList = priceContext.getDiscountList();
        Optional<DiscountVo> discountVo = discountList.stream().min(Comparator.comparing(DiscountVo::getPriceChange));
        BigDecimal currPrice = prePrice.add(discountVo.get().getPriceChange());
        if(BigDecimal.ZERO.compareTo(currPrice) > 0) {
            currPrice = BigDecimal.ZERO;
        }

        List<AmountStepVo> amountStepList = priceContext.getAmountStepList();
        amountStepList.add(new AmountStepVo(discountVo.get().getAmountType(),
                prePrice,
                currPrice.subtract(prePrice),
                currPrice,
                discountVo.get().getDesc()));
    }
}
