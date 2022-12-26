package com.fibo.rule.test.mall.vo;

import lombok.Data;

import java.util.List;

/**
 * <p>订单信息</p>
 *
 * @author JPX
 * @since 2022-12-14 11:04
 */
@Data
public class OrderVo {

    private Long id;
    /**订单号*/
    private String orderNo;
    /**是否境外*/
    private boolean isAboard;
    /**是否会员*/
    private boolean isVip;
    /**商品信息*/
    private List<GoodsVo> goodsList;

}
