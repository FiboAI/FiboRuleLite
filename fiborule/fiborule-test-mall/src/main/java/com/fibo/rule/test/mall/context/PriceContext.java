package com.fibo.rule.test.mall.context;

import cn.hutool.core.collection.CollUtil;
import com.fibo.rule.test.mall.vo.AmountStepVo;
import com.fibo.rule.test.mall.vo.DiscountVo;
import com.fibo.rule.test.mall.vo.GoodsVo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *<p>价格计算结果</p>
 *
 *@author JPX
 *@since 2022/12/14 11:16
 */
@Data
public class PriceContext {

    /**订单号*/
    private String orderNo;
    /**是否境外*/
    private boolean isAboard;
    /**是否会员*/
    private boolean isVip;
    /**商品信息*/
    private List<GoodsVo> goodesList;
    /**原始金额*/
    private BigDecimal originalPrice;
    /**最终金额*/
    private BigDecimal finalPrice;
    /**折扣列表*/
    private List<DiscountVo> discountList = new CopyOnWriteArrayList<>();
    /**金额计算步骤*/
    private List<AmountStepVo> amountStepList= new ArrayList<>();
    /**计算步骤日志*/
    private String printLog;

    /**
     * 获取最后一个金额计算步骤
     * @return
     */
    public AmountStepVo getLastestAmountStep(){
        if(CollUtil.isEmpty(amountStepList)){
            return null;
        }else{
            return amountStepList.get(amountStepList.size()-1);
        }
    }

}
