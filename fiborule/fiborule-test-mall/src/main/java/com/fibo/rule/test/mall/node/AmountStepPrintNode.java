package com.fibo.rule.test.mall.node;

import com.fibo.rule.core.annotation.FiboBean;
import com.fibo.rule.core.node.FiboNode;
import com.fibo.rule.test.mall.context.PriceContext;
import com.fibo.rule.test.mall.service.SendService;
import com.fibo.rule.test.mall.vo.GoodsVo;
import com.fibo.rule.test.mall.vo.AmountStepVo;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.MessageFormat;

/**
 * 计算步骤日志生成
 */
@Slf4j
@FiboBean(name = "计算步骤日志生成", desc = "计算步骤日志生成")
public class AmountStepPrintNode extends FiboNode {

    @Resource
    private SendService sendService;

    @Override
    public void runnerStep() {
        //获取context
        PriceContext priceContext = this.getContextBean(PriceContext.class);
        StringBuilder logStr = new StringBuilder();

        logStr.append(MessageFormat.format("订单号[{0}]的价格计算的明细结果:\n", priceContext.getOrderNo()));
        logStr.append("|====================================================================\n");
        for(GoodsVo goodesVo : priceContext.getGoodesList()){
            logStr.append(MessageFormat.format("|   {0} [{1}] [{2}]   {3} X {4}\n",
                    goodesVo.getSkuName(),
                    goodesVo.getGoodsCode(),
                    goodesVo.getSkuCode(),
                    goodesVo.getGoodsPrice().setScale(2, RoundingMode.HALF_UP).toString(),
                    goodesVo.getCount()));
        }

        logStr.append("|====================================================================\n");
        for(AmountStepVo step : priceContext.getAmountStepList()){
            logStr.append(MessageFormat.format("|   [{0} : {1}]\n",step.getStepDesc(),step.getPriceChange().setScale(2, BigDecimal.ROUND_HALF_UP).toString()));
        }
        logStr.append(MessageFormat.format("|   [最终价 : {0}]\n",priceContext.getFinalPrice().setScale(2, BigDecimal.ROUND_HALF_UP).toString()));
        logStr.append("|====================================================================\n");
        log.info(logStr.toString());
        priceContext.setPrintLog(logStr.toString());
        sendService.sendOriginalPrice(priceContext.getOrderNo(), priceContext.getOriginalPrice());
        sendService.sendFinalPrice(priceContext.getOrderNo(), priceContext.getFinalPrice());
    }
}
