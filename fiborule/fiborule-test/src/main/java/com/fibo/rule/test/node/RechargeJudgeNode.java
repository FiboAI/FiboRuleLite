package com.fibo.rule.test.node;

import com.fibo.rule.core.node.FiboIfNode;
import com.fibo.rule.test.context.RewardContext;
import lombok.Data;

import java.math.BigDecimal;

/**
 * <p>充值阈值判断</p>
 *
 * @author JPX
 * @since 2022-11-18 10:42
 */
@Data
public class RechargeJudgeNode extends FiboIfNode {

    /**充值金额判断阈值*/
    private BigDecimal threshold;

    @Override
    public boolean runnerStepIf() {
        RewardContext contextBean = this.getContextBean(RewardContext.class);
        if(contextBean.getRechargeNum().compareTo(threshold) >= 0) {
            return true;
        } else {
            return false;
        }
    }

}
