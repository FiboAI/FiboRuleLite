package com.fibo.rule.test.market.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>交易信息</p>
 *
 * @author JPX
 * @since 2023-01-05 11:02
 */
@Data
public class TradeVo {

    /**交易流水号*/
    private String bankTradeCode;
    /**
     * 持卡人交易类别代码 00：消费，01：取现，10：转出交易，20：退货，21：存款，28：转入交易，29：代付交易，30：余额查询，39：留作将来使用，40：转账交易
     */
    private String tradeType;
    /**交易金额*/
    private BigDecimal tradeAmount;
    /**交易时间*/
    private LocalDateTime localTradeDate;
    /**交易时间*/
    private String tradeDate;
    /**卡号*/
    private String cardNumber;
    /**客户编号*/
    private String customerNumber;
    /**商户编号*/
    private String merchantCode;
    /**商户名称*/
    private String merchantName;
    /**卡片产品名称*/
    private String cardProductName;
    /**卡片产品编号*/
    private String cardProductNumber;
    /**卡片受理方城市或商户所在城市*/
    private String merchantCityCode;
    /**ATM或商户所在国家代码*/
    private String merchantCountryCode;
    /**交易地域-省*/
    private String tradePlaceProvince;
    /**交易地域-城市*/
    private String tradePlaceCity;

}
