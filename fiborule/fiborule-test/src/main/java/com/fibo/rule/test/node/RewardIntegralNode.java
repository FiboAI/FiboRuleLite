package com.fibo.rule.test.node;

import com.fibo.rule.core.node.FiboNode;
import com.fibo.rule.test.context.RewardContext;
import lombok.Data;

/**
 * <p>奖励积分节点</p>
 *
 * @author JPX
 * @since 2022-11-18 10:42
 */
@Data
public class RewardIntegralNode extends FiboNode {

    private Integer threshold;

    @Override
    public void runnerStep(String nodeCode) {
        RewardContext contextBean = this.getContextBean(RewardContext.class);
        contextBean.setIntegral(threshold);
    }

}
