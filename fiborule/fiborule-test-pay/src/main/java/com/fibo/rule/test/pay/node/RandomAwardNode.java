package com.fibo.rule.test.pay.node;

import cn.hutool.core.util.StrUtil;
import com.fibo.rule.core.annotation.FiboBean;
import com.fibo.rule.core.annotation.FiboField;
import com.fibo.rule.core.node.FiboNode;
import com.fibo.rule.test.pay.vo.ResultVo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;


@Data
@FiboBean(name = "随机奖励", desc = "随机奖励")
public class RandomAwardNode extends FiboNode {

    @FiboField(name = "随机奖励", desc = "随机奖励")
    private String randomAward;

    @Override
    public void runnerStep() {
        //获取context
        ResultVo priceContext = this.getContextBean(ResultVo.class);
        priceContext.setRandomAward(randomAward);
        priceContext.setStepDesc(priceContext.getStepDesc() + "获得随机奖励:" + randomAward + ";");
    }
}
