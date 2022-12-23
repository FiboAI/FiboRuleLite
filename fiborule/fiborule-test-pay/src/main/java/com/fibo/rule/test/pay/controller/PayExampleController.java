package com.fibo.rule.test.pay.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.fibo.rule.common.dto.EngineDto;
import com.fibo.rule.common.dto.EngineNodeDto;
import com.fibo.rule.core.engine.EngineBuilder;
import com.fibo.rule.core.engine.EngineResponse;
import com.fibo.rule.core.runner.FiboApplication;
import com.fibo.rule.test.pay.context.PayRequest;
import com.fibo.rule.test.pay.vo.ResultVo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
public class PayExampleController {

    @Resource
    private FiboApplication fiboApplication;

    @RequestMapping(value = "/release", method = RequestMethod.POST)
    @ResponseBody
    public String release() {
        EngineDto engineDto = new EngineDto();
        engineDto.setId(3L);
        engineDto.setEngineName("pay-test");
        String nodes = "[\n" +
                "    {\"id\":1,\"nodeName\":\"start\",\"nodeCode\":\"start\",\"engineId\":1,\"nodeType\":1,\"preNodes\":\"\",\"nextNodes\":\"PayIfNode\",\"nodeConfig\":\"\",\"nodeClazz\":\"\"},\n" +
                "    {\"id\":2,\"nodeName\":\"PayIfNode1\",\"beanName\":\"是否满足条件1\",\"nodeCode\":\"PayIfNode1\",\"engineId\":1,\"nodeType\":4,\"preNodes\":\"start\",\"nextNodes\":\"PayIfNode2\",\"nodeConfig\":\"{\\\"payAmount\\\":\\\"100\\\",\\\"decrementValue\\\":\\\"5\\\"}\",\"nodeClazz\":\"com.fibo.rule.test.mall.node.OrderInitNode\"},\n" +
                "    {\"id\":3,\"nodeName\":\"PayIfNode2\",\"beanName\":\"是否满足条件2\",\"nodeCode\":\"PayIfNode2\",\"engineId\":1,\"nodeType\":4,\"preNodes\":\"PayIfNode1\",\"nextNodes\":\"CouponDiscountNode,FullDecrementNode,FullDisCountNode,MemberDisCountNode\",\"nodeConfig\":\"\",\"nodeClazz\":\"\"},\n" +
                "    {\"id\":4,\"nodeName\":\"CouponDiscountNode\",\"beanName\":\"抵扣券计算\",\"nodeCode\":\"CouponDiscountNode\",\"engineId\":1,\"nodeType\":3,\"preNodes\":\"all\",\"nextNodes\":\"poly\",\"nodeConfig\":\"{\\\"couponValue\\\":\\\"15\\\"}\",\"nodeClazz\":\"com.fibo.rule.test.mall.node.CouponDiscountNode\"},\n" +
                "    {\"id\":5,\"nodeName\":\"FullDecrementNode\",\"beanName\":\"满减券计算\",\"nodeCode\":\"FullDecrementNode\",\"engineId\":1,\"nodeType\":3,\"preNodes\":\"all\",\"nextNodes\":\"poly\",\"nodeConfig\":\"{\\\"fullValue\\\":\\\"100\\\",\\\"decrementValue\\\":\\\"5\\\"}\",\"nodeClazz\":\"com.fibo.rule.test.mall.node.FullDecrementNode\"},\n" +
                "    {\"id\":6,\"nodeName\":\"FullDisCountNode\",\"beanName\":\"打折券计算\",\"nodeCode\":\"FullDisCountNode\",\"engineId\":1,\"nodeType\":3,\"preNodes\":\"all\",\"nextNodes\":\"poly\",\"nodeConfig\":\"{\\\"fullValue\\\":\\\"200\\\",\\\"discountValue\\\":\\\"0.9\\\"}\",\"nodeClazz\":\"com.fibo.rule.test.mall.node.FullDisCountNode\"},\n" +
                "    {\"id\":7,\"nodeName\":\"MemberDisCountNode\",\"beanName\":\"会员折扣计算\",\"nodeCode\":\"MemberDisCountNode\",\"engineId\":1,\"nodeType\":3,\"preNodes\":\"all\",\"nextNodes\":\"poly\",\"nodeConfig\":\"{\\\"discountValue\\\":\\\"0.9\\\"}\",\"nodeClazz\":\"com.fibo.rule.test.mall.node.MemberDisCountNode\"},\n" +
                "    {\"id\":8,\"nodeName\":\"poly\",\"beanName\":\"\",\"nodeCode\":\"poly\",\"engineId\":1,\"nodeType\":7,\"preNodes\":\"CouponDiscountNode,FullDecrementNode,FullDisCountNode,MemberDisCountNode\",\"nextNodes\":\"DisCountCollectNode\",\"nodeConfig\":\"\",\"nodeClazz\":\"\"},\n" +
                "    {\"id\":9,\"nodeName\":\"DisCountCollectNode\",\"beanName\":\"最大折扣金额计算\",\"nodeCode\":\"DisCountCollectNode\",\"engineId\":1,\"nodeType\":3,\"preNodes\":\"poly\",\"nextNodes\":\"MemberJudgeNode\",\"nodeConfig\":\"\",\"nodeClazz\":\"com.fibo.rule.test.mall.node.DisCountCollectNode\"},\n" +
                "    {\"id\":10,\"nodeName\":\"MemberJudgeNode\",\"beanName\":\"是否会员\",\"nodeCode\":\"MemberJudgeNode\",\"engineId\":1,\"nodeType\":4,\"preNodes\":\"DisCountCollectNode\",\"nextNodes\":\"AddressJudgeNode,FinalAmountNode\",\"nodeConfig\":\"\",\"nodeClazz\":\"com.fibo.rule.test.mall.node.MemberJudgeNode\"},\n" +
                "    {\"id\":11,\"nodeName\":\"AddressJudgeNode\",\"beanName\":\"是否海外地址\",\"nodeCode\":\"AddressJudgeNode\",\"engineId\":1,\"nodeType\":4,\"preNodes\":\"MemberJudgeNode\",\"nextNodes\":\"HomeFreightNode,AbroadFreightNode\",\"nodeConfig\":\"{\\\"lineValue\\\":\\\"N\\\"}\",\"nodeClazz\":\"com.fibo.rule.test.mall.node.AddressJudgeNode\"},\n" +
                "    {\"id\":12,\"nodeName\":\"HomeFreightNode\",\"beanName\":\"国内运费计算\",\"nodeCode\":\"HomeFreightNode\",\"engineId\":1,\"nodeType\":3,\"preNodes\":\"AddressJudgeNode\",\"nextNodes\":\"FinalAmountNode\",\"nodeConfig\":\"{\\\"lineValue\\\":\\\"N\\\",\\\"fullValue\\\":\\\"99\\\",\\\"freightValue\\\":\\\"10\\\"}\",\"nodeClazz\":\"com.fibo.rule.test.mall.node.HomeFreightNode\"},\n" +
                "    {\"id\":13,\"nodeName\":\"AbroadFreightNode\",\"beanName\":\"海外运费计算\",\"nodeCode\":\"AbroadFreightNode\",\"engineId\":1,\"nodeType\":3,\"preNodes\":\"AddressJudgeNode\",\"nextNodes\":\"FinalAmountNode\",\"nodeConfig\":\"{\\\"lineValue\\\":\\\"Y\\\",\\\"freightValue\\\":\\\"15\\\"}\",\"nodeClazz\":\"com.fibo.rule.test.mall.node.AbroadFreightNode\"},\n" +
                "    {\"id\":14,\"nodeName\":\"FinalAmountNode\",\"beanName\":\"最终金额计算\",\"nodeCode\":\"FinalAmountNode\",\"engineId\":1,\"nodeType\":3,\"preNodes\":\"HomeFreightNode,AbroadFreightNode,MemberJudgeNode\",\"nextNodes\":\"AmountStepPrintNode\",\"nodeConfig\":\"{\\\"lineValue\\\":\\\"Y\\\"}\",\"nodeClazz\":\"com.fibo.rule.test.mall.node.FinalAmountNode\"},\n" +
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
    public String submit() {
        EngineResponse engineResponse = fiboApplication.runner(3L, mockReq(), ResultVo.class);
        return engineResponse.toString();
    }

    private PayRequest mockReq() {
        PayRequest req = new PayRequest();
        req.setPayAmount(new BigDecimal(100));
        req.setPayTime(LocalDate.of(2022, 12, 20));
        return req;
    }
    

}
