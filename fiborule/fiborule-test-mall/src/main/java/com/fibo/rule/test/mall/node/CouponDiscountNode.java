package com.fibo.rule.test.mall.node;

import cn.hutool.core.util.StrUtil;
import com.fibo.rule.core.annotation.FiboBean;
import com.fibo.rule.core.annotation.FiboField;
import com.fibo.rule.core.node.FiboNode;
import com.fibo.rule.test.mall.context.PriceContext;
import com.fibo.rule.test.mall.enums.AmountTypeEnum;
import com.fibo.rule.test.mall.vo.DiscountVo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>抵扣券计算</p>
 *
 * @author JPX
 * @since 2022-12-14 14:15
 */
@Data
@FiboBean(name = "抵扣券计算", desc = "抵扣劵计算")
public class CouponDiscountNode extends FiboNode {

    @FiboField(name = "抵扣劵金额", desc = "抵扣劵金额")
    private BigDecimal couponValue;

    @Override
    public void runnerStep() {
        //获取context
        PriceContext priceContext = this.getContextBean(PriceContext.class);
        List<DiscountVo> discountList = priceContext.getDiscountList();
        //添加折扣记录
        discountList.add(new DiscountVo(AmountTypeEnum.COUPON_DISCOUNT,
                BigDecimal.ZERO.subtract(couponValue),
                StrUtil.format( "{}(减{})", AmountTypeEnum.COUPON_DISCOUNT.getName(), couponValue)));
    }
}
