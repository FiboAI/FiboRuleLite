package com.fibo.rule.test.node;

import com.fibo.rule.core.node.FiboNode;
import com.fibo.rule.test.context.RewardContext;
import lombok.Data;

import java.math.BigDecimal;

/**
 * <p>奖励余额节点</p>
 *
 * @author JPX
 * @since 2022-11-18 10:42
 */
@Data
public class RewardMoneyNode extends FiboNode {

    private BigDecimal threshold;

    @Override
    public void runnerStep() {
        RewardContext contextBean = this.getContextBean(RewardContext.class);
        contextBean.setRewardMoney(threshold);
    }

}
