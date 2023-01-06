package com.fibo.rule.test.market.vo;

import lombok.Data;

/**
 * <p>营销活动结果</p>
 *
 * @author JPX
 * @since 2023-01-05 10:56
 */
@Data
public class ResultVo {

    /**是否命中*/
    private boolean isHit;
    /**返现结果*/
    private CashBackVo cashBack;
    /**未命中原因*/
    private String unHitCause;

}
