package com.fibo.rule.test.market.node;

import com.fibo.rule.core.annotation.FiboBean;
import com.fibo.rule.core.node.FiboNode;
import com.fibo.rule.test.market.context.MarketContext;
import com.fibo.rule.test.market.vo.ResultVo;
import lombok.Data;

/**
 * <p>初始化节点</p>
 *
 * @author JPX
 * @since 2023-01-05 15:58
 */
@Data
@FiboBean(name = "初始化节点", desc = "初始化节点")
public class ContextInitNode extends FiboNode {
    @Override
    public void runnerStep() {
        MarketContext marketContext = this.getContextBean(MarketContext.class);
        marketContext.setTradeVo(this.getRequestData());
        marketContext.setResult(new ResultVo());
    }
}
