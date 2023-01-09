package com.fibo.rule.test.market.node;

import com.fibo.rule.core.annotation.FiboBean;
import com.fibo.rule.core.node.FiboIfNode;
import com.fibo.rule.test.market.context.MarketContext;
import lombok.Data;

/**
 * <p>是否报名判断</p>
 *
 * @author JPX
 * @since 2023-01-05 15:58
 */
@Data
@FiboBean(name = "是否报名节点", desc = "是否报名判断")
public class EnrollJudgeNode extends FiboIfNode {

    @Override
    public boolean runnerStepIf() {
        MarketContext marketContext = this.getContextBean(MarketContext.class);
        //可调用接口判断是否报名，现mock为已报名
        boolean isEnroll = true;
        if(isEnroll) {
            return true;
        }
        //设置未命中原因
        marketContext.getResult().setHit(false);
        marketContext.getResult().setUnHitCause("未报名该活动");
        return false;
    }

}
