package com.fibo.rule.test.mall.node;

import com.fibo.rule.core.annotation.FiboBean;
import com.fibo.rule.core.annotation.FiboField;
import com.fibo.rule.core.node.FiboNode;
import com.fibo.rule.test.mall.context.PriceContext;
import com.fibo.rule.test.mall.enums.AmountTypeEnum;
import com.fibo.rule.test.mall.vo.AmountStepVo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>海外运费计算</p>
 *
 * @author JPX
 * @since 2022-12-14 14:15
 */
@Data
@FiboBean(name = "海外运费计算", desc = "海外运费计算")
public class AbroadFreightNode extends FiboNode {

    @FiboField(name = "海外运费", desc = "海外运费")
    private BigDecimal freightValue;

    @Override
    public void runnerStep() {
        //获取context
        PriceContext priceContext = this.getContextBean(PriceContext.class);

        BigDecimal prePrice = priceContext.getLastestAmountStep().getCurrPrice();

        List<AmountStepVo> amountStepList = priceContext.getAmountStepList();
        amountStepList.add(new AmountStepVo(AmountTypeEnum.OVERSEAS_POSTAGE,
                prePrice,
                freightValue,
                prePrice.add(freightValue),
                AmountTypeEnum.OVERSEAS_POSTAGE.getName()));
    }
}
