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
@FiboBean(name = "金额奖励", desc = "金额奖励")
public class MoneyAwardNode extends FiboNode {

    @FiboField(name = "奖励金额", desc = "奖励金额")
    private BigDecimal awardMoney;

    @Override
    public void runnerStep() {
        ResultVo resultVo = this.getContextBean(ResultVo.class);
        resultVo.setAwardMoney(awardMoney);
        resultVo.setStepDesc("获得金额奖励：" + awardMoney + ";");
    }
}
