package com.fibo.rule.test.mall.node;

import cn.hutool.core.util.StrUtil;
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
 * <p>国内运费计算</p>
 *
 * @author JPX
 * @since 2022-12-14 14:15
 */
@Data
@FiboBean(name = "国内运费计算", desc = "国内运费计算")
public class HomeFreightNode extends FiboNode {

    @FiboField(name = "满足金额免运费", desc = "满足金额免运费金额")
    private BigDecimal fullValue;
    @FiboField(name = "国内运费", desc = "国内运费")
    private BigDecimal freightValue;

    @Override
    public void runnerStep() {
        //获取context
        PriceContext priceContext = this.getContextBean(PriceContext.class);

        BigDecimal prePrice = priceContext.getLastestAmountStep().getCurrPrice();

        List<AmountStepVo> amountStepList = priceContext.getAmountStepList();
        amountStepList.add(new AmountStepVo(AmountTypeEnum.POSTAGE,
                prePrice,
                freightValue,
                prePrice.add(freightValue),
                AmountTypeEnum.POSTAGE.getName()));
        //满足条件免运费
        if(prePrice.compareTo(fullValue) >= 0){
            BigDecimal subtract = BigDecimal.ZERO.subtract(freightValue);
            prePrice = priceContext.getLastestAmountStep().getCurrPrice();
            amountStepList.add(new AmountStepVo(AmountTypeEnum.POSTAGE_FREE,
                    prePrice,
                    subtract,
                    prePrice.add(subtract),
                    StrUtil.format("{}(满{})", AmountTypeEnum.POSTAGE_FREE.getName(), fullValue)));
        }

    }
}
