package com.fibo.rule.test.mall.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * <p>商品信息</p>
 *
 * @author JPX
 * @since 2022-12-14 11:05
 */
@Data
public class GoodsVo {

    /**商品id*/
    private Long goodsId;
    /**商品编码*/
    private String goodsCode;
    /**SKU ID*/
    private Long skuId;
    /**SKU CODE*/
    private String skuCode;
    /**SKU名称*/
    private String skuName;
    /**价格*/
    private BigDecimal goodsPrice;
    /**数量*/
    private Integer count;

}
