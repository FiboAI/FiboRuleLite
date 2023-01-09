package com.fibo.rule.test.market.node;

import cn.hutool.core.date.DateUtil;
import com.fibo.rule.core.annotation.FiboBean;
import com.fibo.rule.core.annotation.FiboField;
import com.fibo.rule.core.node.FiboIfNode;
import com.fibo.rule.test.market.context.MarketContext;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>活动时间判断节点</p>
 *
 * @author JPX
 * @since 2023-01-05 15:58
 */
@Data
@FiboBean(name = "时间判断节点", desc = "时间判断")
public class DateJudgeNode extends FiboIfNode {

    @FiboField(name = "开始时间", desc = "开始时间")
    private Date startDate;
    @FiboField(name = "结束时间", desc = "结束时间")
    private Date endDate;

    @Override
    public boolean runnerStepIf() {
        MarketContext marketContext = this.getContextBean(MarketContext.class);
        LocalDateTime localTradeDate = marketContext.getTradeVo().getLocalTradeDate();
        LocalDateTime startDateTime = DateUtil.toLocalDateTime(startDate);
        LocalDateTime endDateTime = DateUtil.toLocalDateTime(endDate);
        //判断交易时间是否在时间范围内
        if(!startDateTime.isAfter(localTradeDate) && !endDateTime.isBefore(localTradeDate)) {
            return true;
        }
        //设置未命中原因
        marketContext.getResult().setHit(false);
        marketContext.getResult().setUnHitCause("交易时间不在活动时间范围内");
        return false;
    }
}
