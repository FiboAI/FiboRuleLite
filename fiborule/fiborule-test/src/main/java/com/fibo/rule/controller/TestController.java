package com.fibo.rule.controller;

import com.alibaba.fastjson.JSON;
import com.fibo.rule.complex.context.ComplexContext;
import com.fibo.rule.complex.request.ComplexRequest;
import com.fibo.rule.core.engine.EngineResponse;
import com.fibo.rule.core.runner.FiboApplication;
import com.fibo.rule.iftest.context.IfTestContext;
import com.fibo.rule.request.TestRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p></p>
 *
 * @author JPX
 * @since 2022-12-06 15:31
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @Resource
    private FiboApplication fiboApplication;

    @RequestMapping(value = "/test", method = RequestMethod.POST)
    public String test() {
        TestRequest req = new TestRequest();
        req.setA("A");
        req.setB("B");
        req.setC("C");
        req.setD("D");
        req.setE("E");
        EngineResponse engineResponse = fiboApplication.runner(1l, req, IfTestContext.class);
        return JSON.toJSONString(engineResponse);
    }

    @RequestMapping(value = "/test1", method = RequestMethod.POST)
    public String test1() {
        ComplexRequest req = new ComplexRequest();
        req.setF("I");
        EngineResponse engineResponse = fiboApplication.runner(2l, req, ComplexContext.class);
        return JSON.toJSONString(engineResponse);
    }

}
