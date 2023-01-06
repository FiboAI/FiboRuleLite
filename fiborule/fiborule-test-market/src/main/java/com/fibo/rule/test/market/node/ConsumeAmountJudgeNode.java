package com.fibo.rule.test.market.node;

import com.fibo.rule.core.annotation.FiboBean;
import com.fibo.rule.core.annotation.FiboField;
import com.fibo.rule.core.node.FiboIfNode;
import com.fibo.rule.test.market.context.MarketContext;
import lombok.Data;

import java.math.BigDecimal;

/**
 * <p>累计消费金额判断节点</p>
 *
 * @author JPX
 * @since 2023-01-05 15:58
 */
@Data
@FiboBean(name = "累计消费金额判断节点", desc = "累计消费金额判断")
public class ConsumeAmountJudgeNode extends FiboIfNode {

    @FiboField(name = "累计消费金额", desc = "累计消费金额")
    private BigDecimal consumeAmount;

    @Override
    public boolean runnerStepIf() {
        MarketContext marketContext = this.getContextBean(MarketContext.class);
        //可从redis内存数据库中获取累计消费金额，获取之后进行累计，现mock一个值
        BigDecimal curConsumeAmount = new BigDecimal(7000);
        //判断累计消费金额是否达标
        if(curConsumeAmount.compareTo(consumeAmount) >= 0) {
            return true;
        }
        //设置未命中原因
        marketContext.getResult().setHit(false);
        marketContext.getResult().setUnHitCause("累计消费金额不达标");
        return false;
    }
}
