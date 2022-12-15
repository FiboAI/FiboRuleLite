package com.fibo.rule.test.mall.enums;

/**
 *<p>金额类型</p>
 *
 *@author JPX
 *@since 2022/12/14 13:26
 */
public enum AmountTypeEnum {

    ORIGINAL(0, "原始价格"),
    COUPON_DISCOUNT(1, "抵扣券"),
    FULL_DECREMENT(2, "满减券"),
    FULL_DISCOUNT(3, "打折券"),
    MEMBER_DISCOUNT(4, "会员折扣"),
    POSTAGE(5, "国内运费"),
    OVERSEAS_POSTAGE(6, "海外运费"),
    POSTAGE_FREE(7, "运费减免");

    private Integer code;

    private String name;

    AmountTypeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName(){
        return name;
    }

}
