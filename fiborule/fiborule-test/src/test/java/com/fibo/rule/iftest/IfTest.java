package com.fibo.rule.iftest;

import com.alibaba.fastjson.JSONArray;
import com.fibo.rule.common.dto.EngineDto;
import com.fibo.rule.common.dto.EngineNodeDto;
import com.fibo.rule.core.engine.EngineBuilder;
import com.fibo.rule.core.engine.EngineResponse;
import com.fibo.rule.core.runner.FiboApplication;
import com.fibo.rule.request.TestRequest;
import com.fibo.rule.iftest.context.IfTestContext;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = IfTest.class)
@EnableAutoConfiguration
public class IfTest {

    @Resource
    private FiboApplication fiboApplication;

    @Before
    public void beforeTest() {
        EngineDto engineDto = new EngineDto();
        engineDto.setId(1l);
        engineDto.setEngineName("test1");
        String nodes = "[{\"id\":1,\"nodeName\":\"start\",\"nodeCode\":\"start\",\"engineId\":1,\"nodeType\":1,\"preNodes\":\"\",\"nextNodes\":\"A\",\"nodeConfig\":\"\",\"nodeClazz\":\"\"},{\"id\":2,\"nodeName\":\"A\",\"beanName\":\"IfTestA\",\"nodeCode\":\"A\",\"engineId\":1,\"nodeType\":3,\"preNodes\":\"start\",\"nextNodes\":\"B\",\"nodeConfig\":\"\",\"nodeClazz\":\"com.fibo.rule.iftest.node.IfTestA\"},{\"id\":3,\"nodeName\":\"B\",\"beanName\":\"IfTestB\",\"nodeCode\":\"B\",\"engineId\":1,\"nodeType\":4,\"preNodes\":\"A\",\"nextNodes\":\"C,D\",\"nodeConfig\":\"{\\\"valueB\\\":\\\"B\\\"}\",\"nodeClazz\":\"com.fibo.rule.iftest.node.IfTestB\"},{\"id\":4,\"nodeName\":\"C\",\"beanName\":\"IfTestC\",\"nodeCode\":\"C\",\"engineId\":1,\"nodeType\":3,\"preNodes\":\"B\",\"nextNodes\":\"E\",\"nodeConfig\":\"{\\\"lineValue\\\":\\\"Y\\\"}\",\"nodeClazz\":\"com.fibo.rule.iftest.node.IfTestC\"},{\"id\":5,\"nodeName\":\"D\",\"beanName\":\"IfTestD\",\"nodeCode\":\"D\",\"engineId\":1,\"nodeType\":3,\"preNodes\":\"B\",\"nextNodes\":\"E\",\"nodeConfig\":\"{\\\"lineValue\\\":\\\"N\\\"}\",\"nodeClazz\":\"com.fibo.rule.iftest.node.IfTestD\"},{\"id\":6,\"nodeName\":\"E\",\"beanName\": \"IfTestE\",\"nodeCode\":\"E\",\"engineId\":1,\"nodeType\":3,\"preNodes\":\"C,D\",\"nextNodes\":\"end\",\"nodeConfig\":\"\",\"nodeClazz\":\"com.fibo.rule.iftest.node.IfTestE\"},{\"id\":7,\"nodeName\":\"end\",\"nodeCode\":\"end\",\"engineId\":1,\"nodeType\":2,\"preNodes\":\"E\",\"nextNodes\":\"\",\"nodeConfig\":\"\",\"nodeClazz\":\"\"}]";
        List<EngineNodeDto> nodeDtoList = JSONArray.parseArray(nodes, EngineNodeDto.class);
        engineDto.setNodeList(nodeDtoList);
        EngineBuilder.createEngine(engineDto).build();
    }

    @Test
    public void test() {
        TestRequest req = new TestRequest();
        req.setA("A");
        req.setB("B");
        req.setC("C");
        req.setD("D");
        req.setE("E");
        EngineResponse engineResponse = fiboApplication.runner(1l, req, IfTestContext.class);
        Assert.assertTrue(engineResponse.isSuccess());
        Assert.assertEquals("[2]:[A]==>[3]:[B]==>[4]:[C]==>[6]:[E]", engineResponse.getExecuteStepStr());
        Assert.assertEquals("A", engineResponse.getContextBean(IfTestContext.class).getA());
        Assert.assertEquals("B", engineResponse.getContextBean(IfTestContext.class).getB());
        Assert.assertNull(engineResponse.getContextBean(IfTestContext.class).getD());
        Assert.assertEquals("C", engineResponse.getContextBean(IfTestContext.class).getC());
        Assert.assertEquals("E", engineResponse.getContextBean(IfTestContext.class).getE());
    }

    @Test
    public void test2() {
        TestRequest req = new TestRequest();
        req.setA("A");
        req.setB("B1");
        req.setC("C");
        req.setD("D");
        req.setE("E");
        EngineResponse engineResponse = fiboApplication.runner(1l, req, IfTestContext.class);
        Assert.assertTrue(engineResponse.isSuccess());
        Assert.assertEquals("[2]:[A]==>[3]:[B]==>[5]:[D]==>[6]:[E]", engineResponse.getExecuteStepStr());
        Assert.assertEquals("A", engineResponse.getContextBean(IfTestContext.class).getA());
        Assert.assertEquals("B1", engineResponse.getContextBean(IfTestContext.class).getB());
        Assert.assertNull(engineResponse.getContextBean(IfTestContext.class).getC());
        Assert.assertEquals("D", engineResponse.getContextBean(IfTestContext.class).getD());
        Assert.assertEquals("E", engineResponse.getContextBean(IfTestContext.class).getE());
    }

}
