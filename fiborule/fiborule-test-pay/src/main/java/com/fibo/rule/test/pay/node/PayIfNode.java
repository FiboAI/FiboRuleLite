package com.fibo.rule.test.pay.node;

import cn.hutool.core.util.ObjectUtil;
import com.fibo.rule.core.annotation.FiboBean;
import com.fibo.rule.core.annotation.FiboField;
import com.fibo.rule.core.node.FiboIfNode;
import com.fibo.rule.test.pay.context.PayRequest;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@FiboBean(name = "是否满足条件", desc = "是否满足条件")
public class PayIfNode extends FiboIfNode {

    @FiboField(name = "充值金额", desc = "充值金额")
    private BigDecimal payAmount;
    @FiboField(name = "活动开始时间", desc = "活动开始时间")
    private LocalDate beginTime;
    @FiboField(name = "活动结束时间", desc = "活动结束时间")
    private LocalDate endTime;

    @Override
    public boolean runnerStepIf() {
        //获取请求参数
        PayRequest payRequest = this.getRequestData();
        if (ObjectUtil.isNotNull(payRequest) && null != payRequest.getPayAmount() && null != payRequest.getPayTime()) {
            if (payRequest.getPayAmount().compareTo(payAmount) >= 0 &&
                    payRequest.getPayTime().compareTo(beginTime) >= 0 &&
                    payRequest.getPayTime().compareTo(endTime) <= 0) {
                return true;
            }
        }
        return false;
    }
}
