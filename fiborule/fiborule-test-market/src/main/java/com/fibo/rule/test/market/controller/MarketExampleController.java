package com.fibo.rule.test.market.controller;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.fibo.rule.core.engine.EngineResponse;
import com.fibo.rule.core.runner.FiboApplication;
import com.fibo.rule.test.market.context.MarketContext;
import com.fibo.rule.test.market.vo.TradeVo;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

@Slf4j
@RestController
public class MarketExampleController {

    @Resource
    private FiboApplication fiboApplication;

    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    @ResponseBody
    public String submit(@RequestBody Map<String, Object> requestBody) throws ParseException {
        TradeVo tradeVo = JSON.parseObject(Convert.toStr(requestBody.get("requestBody")), TradeVo.class);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse(tradeVo.getTradeDate());
        tradeVo.setLocalTradeDate(DateUtil.toLocalDateTime(date));
        EngineResponse engineResponse = fiboApplication.runner(Convert.toLong(requestBody.get("engineId")), tradeVo, MarketContext.class);
        return JSON.toJSONString(engineResponse);
    }

    @RequestMapping(value = "/getMockReq", method = RequestMethod.POST)
    @ResponseBody
    public String getMockReq() throws ParseException {
        return JSON.toJSONString(mockReq());
    }

    private TradeVo mockReq() throws ParseException {
        TradeVo tradeVo = new TradeVo();
        tradeVo.setBankTradeCode("1000561650337995000");
        tradeVo.setTradeAmount(new BigDecimal(1000));
        tradeVo.setTradeDate("2023-01-05");
        tradeVo.setCardNumber("652352******7425");
        tradeVo.setCustomerNumber("9888888888");
        tradeVo.setMerchantCode("437034323252737");
        tradeVo.setMerchantName("商户一");
        tradeVo.setCardProductName("");
        tradeVo.setCardProductNumber("");
        tradeVo.setMerchantCityCode("330000");
        tradeVo.setMerchantCountryCode("103");
        tradeVo.setTradePlaceProvince("156");
        tradeVo.setTradePlaceCity("330000");
        return tradeVo;
    }
}
