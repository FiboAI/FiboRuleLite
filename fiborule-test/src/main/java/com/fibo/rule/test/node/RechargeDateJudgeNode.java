package com.fibo.rule.test.node;

import com.fibo.rule.core.annotation.FiboBean;
import com.fibo.rule.core.annotation.FiboField;
import com.fibo.rule.core.node.FiboIfNode;
import com.fibo.rule.test.context.RewardContext;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>充值时间阈值判断</p>
 *
 * @author JPX
 * @since 2022-11-18 10:42
 */
@Data
@FiboBean(name = "rechargeDateJudgeNode", desc = "充值时间判断节点")
public class RechargeDateJudgeNode extends FiboIfNode {

    /**充值时间阈值*/
    @FiboField(name = "beforeDate", desc = "大于等于时间")
    private Date beforeDate;
    /**充值时间阈值*/
    @FiboField(name = "afterDate", desc = "小于等于时间")
    private Date afterDate;

    @Override
    public boolean runnerStepIf() {
        RewardContext contextBean = this.getContextBean(RewardContext.class);
        if(!contextBean.getRechargeDate().before(beforeDate)
            && !contextBean.getRechargeDate().after(afterDate)) {
            return true;
        } else {
            return false;
        }
    }

}
