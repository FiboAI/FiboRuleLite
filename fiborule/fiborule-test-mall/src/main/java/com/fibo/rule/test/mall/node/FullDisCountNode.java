package com.fibo.rule.test.mall.node;

import cn.hutool.core.util.StrUtil;
import com.fibo.rule.core.annotation.FiboBean;
import com.fibo.rule.core.annotation.FiboField;
import com.fibo.rule.core.node.FiboNode;
import com.fibo.rule.test.mall.enums.AmountTypeEnum;
import com.fibo.rule.test.mall.context.PriceContext;
import com.fibo.rule.test.mall.vo.DiscountVo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>打折券计算</p>
 *
 * @author JPX
 * @since 2022-12-14 14:15
 */
@Data
@FiboBean(name = "打折券计算", desc = "打折券计算")
public class FullDisCountNode extends FiboNode {

    @FiboField(name = "满足金额", desc = "满足金额")
    private BigDecimal fullValue;
    @FiboField(name = "折扣", desc = "折扣")
    private BigDecimal discountValue;

    @Override
    public void runnerStep() {
        //获取context
        PriceContext priceContext = this.getContextBean(PriceContext.class);
        BigDecimal originalPrice = priceContext.getOriginalPrice();
        //不满足条件
        if(originalPrice.compareTo(fullValue) < 0) {
            return;
        }
        BigDecimal subtractValue = originalPrice.subtract(originalPrice.multiply(discountValue));
        List<DiscountVo> discountList = priceContext.getDiscountList();
        //添加打折记录
        discountList.add(new DiscountVo(AmountTypeEnum.FULL_DISCOUNT,
                BigDecimal.ZERO.subtract(subtractValue),
                StrUtil.format( "{}(满{}打{}折)", AmountTypeEnum.FULL_DISCOUNT.getName(), fullValue, discountValue.multiply(new BigDecimal(10)))));
    }
}
