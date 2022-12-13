package com.fibo.rule.test.node;

import com.fibo.rule.core.annotation.FiboBean;
import com.fibo.rule.core.node.FiboNode;
import com.fibo.rule.test.context.RewardContext;
import com.fibo.rule.test.request.RechargeRequestVo;

/**
 * <p>参数初始化节点</p>
 *
 * @author JPX
 * @since 2022-11-18 10:42
 */
@FiboBean(name = "contextInitNode", desc = "参数初始化节点")
public class ContextInitNode extends FiboNode {

    @Override
    public void runnerStep() {
        RechargeRequestVo req = this.getRequestData();
        RewardContext contextBean = this.getContextBean(RewardContext.class);
        contextBean.setRechargeDate(req.getRechargeDate());
        contextBean.setRechargeNum(req.getRechargeNum());
    }

}
