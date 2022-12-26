package com.fibo.rule.test.mall.node;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.fibo.rule.core.annotation.FiboBean;
import com.fibo.rule.core.node.FiboNode;
import com.fibo.rule.test.mall.context.PriceContext;
import com.fibo.rule.test.mall.enums.AmountTypeEnum;
import com.fibo.rule.test.mall.vo.AmountStepVo;
import com.fibo.rule.test.mall.vo.GoodsVo;
import com.fibo.rule.test.mall.vo.OrderVo;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>订单信息初始化-计算原始订单金额</p>
 *
 * @author JPX
 * @since 2022-12-14 13:57
 */
@FiboBean(name = "订单原始金额计算", desc = "订单原始金额计算")
public class OrderInitNode extends FiboNode {
    @Override
    public void runnerStep() {
        //获取请求参数
        OrderVo orderVo = this.getRequestData();
        //获取context
        PriceContext priceContext = this.getContextBean(PriceContext.class);
        //设置参数
        priceContext.setOrderNo(orderVo.getOrderNo());
        priceContext.setAboard(orderVo.isAboard());
        priceContext.setVip(orderVo.isVip());
        priceContext.setGoodesList(orderVo.getGoodsList());
        //计算原始订单金额
        BigDecimal originalPrice = new BigDecimal(0);
        if(CollUtil.isNotEmpty(orderVo.getGoodsList())) {
            for (GoodsVo goodsVo : orderVo.getGoodsList()) {
                if(ObjectUtil.isNull(goodsVo.getGoodsPrice()) || ObjectUtil.isNull(goodsVo.getCount())) {
                    return;
                }
                originalPrice = originalPrice.add(goodsVo.getGoodsPrice().multiply(new BigDecimal(goodsVo.getCount())));
            }
        }
        priceContext.setOriginalPrice(originalPrice);
        //金额计算步骤
        List<AmountStepVo> amountStepList = priceContext.getAmountStepList();
        amountStepList.add(new AmountStepVo(AmountTypeEnum.ORIGINAL,
                null,
                originalPrice,
                originalPrice,
                AmountTypeEnum.ORIGINAL.getName()));
    }
}
