package com.fibo.rule.test.market.context;

import com.fibo.rule.test.market.vo.ResultVo;
import com.fibo.rule.test.market.vo.TradeVo;
import lombok.Data;

/**
 *<p>营销活动结果</p>
 *
 *@author JPX
 *@since 2022/12/14 11:16
 */
@Data
public class MarketContext {

    /**交易信息*/
    private TradeVo tradeVo;
    /**活动结果*/
    private ResultVo result;

}
