package com.fibo.rule.test.market.node;

import cn.hutool.core.util.StrUtil;
import com.fibo.rule.core.annotation.FiboBean;
import com.fibo.rule.core.annotation.FiboField;
import com.fibo.rule.core.node.FiboIfNode;
import com.fibo.rule.test.market.context.MarketContext;
import lombok.Data;

import java.util.HashSet;
import java.util.List;

/**
 * <p>指定商户判断节点</p>
 *
 * @author JPX
 * @since 2023-01-05 15:58
 */
@Data
@FiboBean(name = "指定商户判断节点", desc = "指定商户判断")
public class MerchantJudgeNode extends FiboIfNode {

    @FiboField(name = "商户编码", desc = "商户编码")
    private String merchantCodes;

    @Override
    public boolean runnerStepIf() {
        MarketContext marketContext = this.getContextBean(MarketContext.class);
        List<String> merchants = StrUtil.split(merchantCodes, StrUtil.COMMA);
        HashSet<String> merchantSet = new HashSet<>(merchants);
        if(merchantSet.contains(marketContext.getTradeVo().getMerchantCode())) {
            return true;
        }
        //设置未命中原因
        marketContext.getResult().setHit(false);
        marketContext.getResult().setUnHitCause("指定商户未命中");
        return false;
    }
}
