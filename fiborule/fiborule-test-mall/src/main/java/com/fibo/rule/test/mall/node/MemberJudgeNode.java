package com.fibo.rule.test.mall.node;

import cn.hutool.core.util.ObjectUtil;
import com.fibo.rule.core.annotation.FiboBean;
import com.fibo.rule.core.node.FiboIfNode;
import com.fibo.rule.test.mall.context.PriceContext;
import lombok.Data;

/**
 * <p>是否会员</p>
 *
 * @author JPX
 * @since 2022-12-14 14:15
 */
@Data
@FiboBean(name = "是否会员", desc = "是否会员")
public class MemberJudgeNode extends FiboIfNode {
    @Override
    public boolean runnerStepIf() {
        //获取context
        PriceContext priceContext = this.getContextBean(PriceContext.class);
        if(ObjectUtil.isNotNull(priceContext.isVip())) {
            return priceContext.isVip();
        }
        return false;
    }
}
