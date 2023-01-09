package com.fibo.rule.test.market.node;

import cn.hutool.core.date.DateUtil;
import com.fibo.rule.core.annotation.FiboBean;
import com.fibo.rule.core.annotation.FiboField;
import com.fibo.rule.core.node.FiboIfNode;
import com.fibo.rule.core.node.FiboNode;
import com.fibo.rule.test.market.context.MarketContext;
import com.fibo.rule.test.market.vo.CashBackVo;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>返现金额计算</p>
 *
 * @author JPX
 * @since 2023-01-05 15:58
 */
@Data
@FiboBean(name = "返现金额计算节点", desc = "返现金额计算")
public class CashBackNode extends FiboNode {

    @FiboField(name = "返现百分比", desc = "返现百分比")
    private BigDecimal cashBackPercent;
    @FiboField(name = "返现单笔限额", desc = "返现单笔限额")
    private BigDecimal cashBackQuota;
    @FiboField(name = "返现方式（1-红包、2-消费冲抵）", desc = "返现方式：1：红包，2：消费冲抵")
    private Integer payType;

    @Override
    public void runnerStep() {
        MarketContext marketContext = this.getContextBean(MarketContext.class);
        BigDecimal cashBack = marketContext.getTradeVo().getTradeAmount().multiply(cashBackPercent);
        if(cashBack.compareTo(cashBackQuota) > 0) {
            cashBack = cashBackQuota;
        }
        marketContext.getResult().setHit(true);
        marketContext.getResult().setCashBack(new CashBackVo());
        marketContext.getResult().getCashBack().setCashBackAmount(cashBack);
        marketContext.getResult().getCashBack().setCashbackPayType(payType);
    }
}
