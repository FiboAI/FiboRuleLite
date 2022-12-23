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
 * <p>会员折扣计算</p>
 *
 * @author JPX
 * @since 2022-12-14 14:15
 */
@Data
@FiboBean(name = "会员折扣计算", desc = "会员折扣计算")
public class MemberDisCountNode extends FiboNode {

    @FiboField(name = "折扣", desc = "折扣")
    private BigDecimal discountValue;

    @Override
    public void runnerStep() {
        //获取context
        PriceContext priceContext = this.getContextBean(PriceContext.class);
        if(!priceContext.isVip()) {
            return;
        }
        BigDecimal originalPrice = priceContext.getOriginalPrice();
        BigDecimal subtractValue = originalPrice.subtract(originalPrice.multiply(discountValue));
        List<DiscountVo> discountList = priceContext.getDiscountList();
        //添加会员折扣记录
        discountList.add(new DiscountVo(AmountTypeEnum.MEMBER_DISCOUNT,
                BigDecimal.ZERO.subtract(subtractValue),
                StrUtil.format( "{}(打{}折)", AmountTypeEnum.MEMBER_DISCOUNT.getName(), discountValue.multiply(new BigDecimal(10)))));
    }
}
