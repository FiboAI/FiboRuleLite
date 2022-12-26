package com.fibo.rule.test.pay.node;

import com.fibo.rule.core.annotation.FiboBean;
import com.fibo.rule.core.annotation.FiboField;
import com.fibo.rule.core.node.FiboNode;
import com.fibo.rule.test.pay.vo.ResultVo;
import lombok.Data;

import java.math.BigDecimal;


@Data
@FiboBean(name = "积分奖励", desc = "积分奖励")
public class PointsAwardNode extends FiboNode {

    @FiboField(name = "积分奖励", desc = "积分奖励")
    private BigDecimal awardPoints;

    @Override
    public void runnerStep() {
        //获取context
        ResultVo resultVo = this.getContextBean(ResultVo.class);
        resultVo.setAwardPoints(awardPoints);
        resultVo.setStepDesc("获得积分奖励：" + awardPoints + ";");
    }
}
