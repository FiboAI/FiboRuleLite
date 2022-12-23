package com.fibo.rule.test.mall.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.fibo.rule.common.dto.EngineDto;
import com.fibo.rule.common.dto.EngineNodeDto;
import com.fibo.rule.core.engine.EngineBuilder;
import com.fibo.rule.core.engine.EngineResponse;
import com.fibo.rule.core.runner.FiboApplication;
import com.fibo.rule.test.mall.context.PriceContext;
import com.fibo.rule.test.mall.vo.GoodsVo;
import com.fibo.rule.test.mall.vo.OrderVo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
public class MallExampleController {

    @Resource
    private FiboApplication fiboApplication;

    @RequestMapping(value = "/release", method = RequestMethod.POST)
    @ResponseBody
    public String release(){
        EngineDto engineDto = new EngineDto();
        engineDto.setId(1l);
        engineDto.setEngineName("mall-test");
        String nodes = "[\n" +
                "    {\"id\":1,\"nodeName\":\"start\",\"nodeCode\":\"start\",\"engineId\":1,\"nodeType\":1,\"preNodes\":\"\",\"nextNodes\":\"OrderInitNode\",\"nodeConfig\":\"\",\"nodeClazz\":\"\"},\n" +
                "    {\"id\":2,\"nodeName\":\"OrderInitNode\",\"beanName\":\"订单原始金额计算\",\"nodeCode\":\"OrderInitNode\",\"engineId\":1,\"nodeType\":3,\"preNodes\":\"start\",\"nextNodes\":\"all\",\"nodeConfig\":\"\",\"nodeClazz\":\"com.fibo.rule.test.mall.node.OrderInitNode\"},\n" +
                "    {\"id\":3,\"nodeName\":\"all\",\"beanName\":\"\",\"nodeCode\":\"all\",\"engineId\":1,\"nodeType\":6,\"preNodes\":\"OrderInitNode\",\"nextNodes\":\"CouponDiscountNode,FullDecrementNode,FullDisCountNode,MemberDisCountNode\",\"nodeConfig\":\"\",\"nodeClazz\":\"\"},\n" +
                "    {\"id\":4,\"nodeName\":\"CouponDiscountNode\",\"beanName\":\"抵扣券计算\",\"nodeCode\":\"CouponDiscountNode\",\"engineId\":1,\"nodeType\":3,\"preNodes\":\"all\",\"nextNodes\":\"poly\",\"nodeConfig\":\"{\\\"couponValue\\\":\\\"15\\\"}\",\"nodeClazz\":\"com.fibo.rule.test.mall.node.CouponDiscountNode\"},\n" +
                "    {\"id\":5,\"nodeName\":\"FullDecrementNode\",\"beanName\":\"满减券计算\",\"nodeCode\":\"FullDecrementNode\",\"engineId\":1,\"nodeType\":3,\"preNodes\":\"all\",\"nextNodes\":\"poly\",\"nodeConfig\":\"{\\\"fullValue\\\":\\\"100\\\",\\\"decrementValue\\\":\\\"5\\\"}\",\"nodeClazz\":\"com.fibo.rule.test.mall.node.FullDecrementNode\"},\n" +
                "    {\"id\":6,\"nodeName\":\"FullDisCountNode\",\"beanName\":\"打折券计算\",\"nodeCode\":\"FullDisCountNode\",\"engineId\":1,\"nodeType\":3,\"preNodes\":\"all\",\"nextNodes\":\"poly\",\"nodeConfig\":\"{\\\"fullValue\\\":\\\"200\\\",\\\"discountValue\\\":\\\"0.9\\\"}\",\"nodeClazz\":\"com.fibo.rule.test.mall.node.FullDisCountNode\"},\n" +
                "    {\"id\":7,\"nodeName\":\"MemberDisCountNode\",\"beanName\":\"会员折扣计算\",\"nodeCode\":\"MemberDisCountNode\",\"engineId\":1,\"nodeType\":3,\"preNodes\":\"all\",\"nextNodes\":\"poly\",\"nodeConfig\":\"{\\\"discountValue\\\":\\\"0.9\\\"}\",\"nodeClazz\":\"com.fibo.rule.test.mall.node.MemberDisCountNode\"},\n" +
                "    {\"id\":8,\"nodeName\":\"poly\",\"beanName\":\"\",\"nodeCode\":\"poly\",\"engineId\":1,\"nodeType\":7,\"preNodes\":\"CouponDiscountNode,FullDecrementNode,FullDisCountNode,MemberDisCountNode\",\"nextNodes\":\"DisCountCollectNode\",\"nodeConfig\":\"\",\"nodeClazz\":\"\"},\n" +
                "    {\"id\":9,\"nodeName\":\"DisCountCollectNode\",\"beanName\":\"最大折扣金额计算\",\"nodeCode\":\"DisCountCollectNode\",\"engineId\":1,\"nodeType\":3,\"preNodes\":\"poly\",\"nextNodes\":\"MemberJudgeNode\",\"nodeConfig\":\"\",\"nodeClazz\":\"com.fibo.rule.test.mall.node.DisCountCollectNode\"},\n" +
                "    {\"id\":10,\"nodeName\":\"MemberJudgeNode\",\"beanName\":\"是否会员\",\"nodeCode\":\"MemberJudgeNode\",\"engineId\":1,\"nodeType\":4,\"preNodes\":\"DisCountCollectNode\",\"nextNodes\":\"AddressJudgeNode,FinalAmountNode\",\"nextNodeValue\":\"[{\\\"key\\\":\\\"Y\\\",\\\"value\\\":\\\"FinalAmountNode\\\"},{\\\"key\\\":\\\"N\\\",\\\"value\\\":\\\"AddressJudgeNode\\\"}]\",\"nodeConfig\":\"\",\"nodeClazz\":\"com.fibo.rule.test.mall.node.MemberJudgeNode\"},\n" +
                "    {\"id\":11,\"nodeName\":\"AddressJudgeNode\",\"beanName\":\"是否海外地址\",\"nodeCode\":\"AddressJudgeNode\",\"engineId\":1,\"nodeType\":4,\"preNodes\":\"MemberJudgeNode\",\"nextNodes\":\"HomeFreightNode,AbroadFreightNode\",\"nextNodeValue\":\"[{\\\"key\\\":\\\"Y\\\",\\\"value\\\":\\\"AbroadFreightNode\\\"},{\\\"key\\\":\\\"N\\\",\\\"value\\\":\\\"HomeFreightNode\\\"}]\",\"nodeConfig\":\"\",\"nodeClazz\":\"com.fibo.rule.test.mall.node.AddressJudgeNode\"},\n" +
                "    {\"id\":12,\"nodeName\":\"HomeFreightNode\",\"beanName\":\"国内运费计算\",\"nodeCode\":\"HomeFreightNode\",\"engineId\":1,\"nodeType\":3,\"preNodes\":\"AddressJudgeNode\",\"nextNodes\":\"FinalAmountNode\",\"nodeConfig\":\"{\\\"fullValue\\\":\\\"99\\\",\\\"freightValue\\\":\\\"10\\\"}\",\"nodeClazz\":\"com.fibo.rule.test.mall.node.HomeFreightNode\"},\n" +
                "    {\"id\":13,\"nodeName\":\"AbroadFreightNode\",\"beanName\":\"海外运费计算\",\"nodeCode\":\"AbroadFreightNode\",\"engineId\":1,\"nodeType\":3,\"preNodes\":\"AddressJudgeNode\",\"nextNodes\":\"FinalAmountNode\",\"nodeConfig\":\"{\\\"freightValue\\\":\\\"15\\\"}\",\"nodeClazz\":\"com.fibo.rule.test.mall.node.AbroadFreightNode\"},\n" +
                "    {\"id\":14,\"nodeName\":\"FinalAmountNode\",\"beanName\":\"最终金额计算\",\"nodeCode\":\"FinalAmountNode\",\"engineId\":1,\"nodeType\":3,\"preNodes\":\"HomeFreightNode,AbroadFreightNode,MemberJudgeNode\",\"nextNodes\":\"AmountStepPrintNode\",\"nodeConfig\":\"\",\"nodeClazz\":\"com.fibo.rule.test.mall.node.FinalAmountNode\"},\n" +
                "    {\"id\":15,\"nodeName\":\"AmountStepPrintNode\",\"beanName\":\"计算步骤日志生成\",\"nodeCode\":\"AmountStepPrintNode\",\"engineId\":1,\"nodeType\":3,\"preNodes\":\"FinalAmountNode\",\"nextNodes\":\"end\",\"nodeConfig\":\"\",\"nodeClazz\":\"com.fibo.rule.test.mall.node.AmountStepPrintNode\"},\n" +
                "    {\"id\":16,\"nodeName\":\"end\",\"nodeCode\":\"end\",\"engineId\":1,\"nodeType\":2,\"preNodes\":\"AmountStepPrintNode\",\"nextNodes\":\"\",\"nodeConfig\":\"\",\"nodeClazz\":\"\"}\n" +
                "  ]";
        List<EngineNodeDto> nodeDtoList = JSONArray.parseArray(nodes, EngineNodeDto.class);
        engineDto.setNodeList(nodeDtoList);
        EngineBuilder.createEngine(engineDto).build();
        return "success";
    }

    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    @ResponseBody
    public String submit(){
        EngineResponse engineResponse = fiboApplication.runner(1l, mockReq(), PriceContext.class);
        return JSON.toJSONString(engineResponse.getContextBean(PriceContext.class));
    }

    private OrderVo mockReq(){
        OrderVo req = new OrderVo();
        req.setOrderNo("SO2020070611120001");
        req.setAboard(false);
        req.setVip(false);
        List<GoodsVo> goodesVoList = new ArrayList<>();
        req.setGoodsList(goodesVoList);

        GoodsVo goodsVo = new GoodsVo();
        goodsVo.setGoodsId(5001L);
        goodsVo.setGoodsCode("PD5001XC");
        goodsVo.setSkuId(67001441L);
        goodsVo.setSkuCode("SKU5001XC001");
        goodsVo.setSkuName("夏季运动女式短裙M");
        goodsVo.setGoodsPrice(new BigDecimal("139.00"));
        goodsVo.setCount(2);
        goodesVoList.add(goodsVo);

        goodsVo = new GoodsVo();
        goodsVo.setGoodsId(6001L);
        goodsVo.setGoodsCode("PD6001XC");
        goodsVo.setSkuId(67002334L);
        goodsVo.setSkuCode("SKU6001XC001");
        goodsVo.setSkuName("男士迷彩短袜均码");
        goodsVo.setGoodsPrice(new BigDecimal("59.00"));
        goodsVo.setCount(3);
        goodesVoList.add(goodsVo);

        goodsVo = new GoodsVo();
        goodsVo.setGoodsId(8001L);
        goodsVo.setGoodsCode("PD8001XC");
        goodsVo.setSkuId(87002001L);
        goodsVo.setSkuCode("SKU8001XC001");
        goodsVo.setSkuName("纯棉毛巾");
        goodsVo.setGoodsPrice(new BigDecimal("28.00"));
        goodsVo.setCount(5);
        goodesVoList.add(goodsVo);

        goodsVo = new GoodsVo();
        goodsVo.setGoodsId(9001L);
        goodsVo.setGoodsCode("PD9001XC");
        goodsVo.setSkuId(97552001L);
        goodsVo.setSkuCode("SKU9001XC001");
        goodsVo.setSkuName("杀菌护手凝胶");
        goodsVo.setGoodsPrice(new BigDecimal("30"));
        goodsVo.setCount(2);
        goodesVoList.add(goodsVo);

        return req;
    }
}
