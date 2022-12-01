package com.fibo.rule.switchtest;

import com.alibaba.fastjson.JSONArray;
import com.fibo.rule.common.dto.EngineDto;
import com.fibo.rule.common.dto.EngineNodeDto;
import com.fibo.rule.core.engine.EngineBuilder;
import com.fibo.rule.core.engine.EngineResponse;
import com.fibo.rule.core.runner.FiboApplication;
import com.fibo.rule.iftest.context.IfTestContext;
import com.fibo.rule.request.TestRequest;
import com.fibo.rule.switchtest.context.SwitchTestContext;
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
@SpringBootTest(classes = SwitchTest.class)
@EnableAutoConfiguration
public class SwitchTest {

    @Resource
    private FiboApplication fiboApplication;

    @Before
    public void beforeTest() {
        EngineDto engineDto = new EngineDto();
        engineDto.setId(2l);
        engineDto.setEngineName("test2");
        String nodes = "[{\"id\":1,\"nodeName\":\"start\",\"nodeCode\":\"start\",\"engineId\":1,\"nodeType\":1,\"preNodes\":\"\",\"nextNodes\":\"A\",\"nodeConfig\":\"\",\"nodeClazz\":\"\"},{\"id\":2,\"nodeName\":\"A\",\"beanName\":\"SwitchTestA\",\"nodeCode\":\"A\",\"engineId\":1,\"nodeType\":3,\"preNodes\":\"start\",\"nextNodes\":\"B\",\"nodeConfig\":\"\",\"nodeClazz\":\"com.fibo.rule.switchtest.node.SwitchTestA\"},{\"id\":3,\"nodeName\":\"B\",\"beanName\":\"SwitchTestB\",\"nodeCode\":\"B\",\"engineId\":1,\"nodeType\":5,\"preNodes\":\"A\",\"nextNodes\":\"C,D,F\",\"nodeConfig\":\"\",\"nodeClazz\":\"com.fibo.rule.switchtest.node.SwitchTestB\"},{\"id\":4,\"nodeName\":\"C\",\"beanName\":\"SwitchTestC\",\"nodeCode\":\"C\",\"engineId\":1,\"nodeType\":3,\"preNodes\":\"B\",\"nextNodes\":\"E\",\"nodeConfig\":\"{\\\"lineValue\\\":\\\"C\\\"}\",\"nodeClazz\":\"com.fibo.rule.switchtest.node.SwitchTestC\"},{\"id\":5,\"nodeName\":\"D\",\"beanName\":\"SwitchTestD\",\"nodeCode\":\"D\",\"engineId\":1,\"nodeType\":3,\"preNodes\":\"B\",\"nextNodes\":\"E\",\"nodeConfig\":\"{\\\"lineValue\\\":\\\"D\\\"}\",\"nodeClazz\":\"com.fibo.rule.switchtest.node.SwitchTestD\"},{\"id\":6,\"nodeName\":\"F\",\"beanName\":\"SwitchTestF\",\"nodeCode\":\"F\",\"engineId\":1,\"nodeType\":3,\"preNodes\":\"B\",\"nextNodes\":\"E\",\"nodeConfig\":\"{\\\"lineValue\\\":\\\"F\\\"}\",\"nodeClazz\":\"com.fibo.rule.switchtest.node.SwitchTestF\"},{\"id\":7,\"nodeName\":\"E\",\"beanName\":\"SwitchTestE\",\"nodeCode\":\"E\",\"engineId\":1,\"nodeType\":3,\"preNodes\":\"C,D,F\",\"nextNodes\":\"end\",\"nodeConfig\":\"\",\"nodeClazz\":\"com.fibo.rule.switchtest.node.SwitchTestE\"},{\"id\":8,\"nodeName\":\"end\",\"nodeCode\":\"end\",\"engineId\":1,\"nodeType\":2,\"preNodes\":\"E\",\"nextNodes\":\"\",\"nodeConfig\":\"\",\"nodeClazz\":\"\"}]";
        List<EngineNodeDto> nodeDtoList = JSONArray.parseArray(nodes, EngineNodeDto.class);
        engineDto.setNodeList(nodeDtoList);
        EngineBuilder.createEngine(engineDto).build();
    }

    @Test
    public void test() {
        TestRequest req = new TestRequest();
        req.setA("A");
        req.setB("C");
        req.setC("C");
        req.setD("D");
        req.setE("E");
        req.setF("F");
        EngineResponse engineResponse = fiboApplication.runner(2l, req, SwitchTestContext.class);
        Assert.assertTrue(engineResponse.isSuccess());
        Assert.assertEquals("[2]:[SwitchTestA]==>[3]:[SwitchTestB]==>[4]:[SwitchTestC]==>[7]:[SwitchTestE]", engineResponse.getExecuteStepStr());
        Assert.assertEquals("A", engineResponse.getContextBean(SwitchTestContext.class).getA());
        Assert.assertEquals("C", engineResponse.getContextBean(SwitchTestContext.class).getB());
        Assert.assertEquals("C", engineResponse.getContextBean(SwitchTestContext.class).getC());
        Assert.assertNull(engineResponse.getContextBean(SwitchTestContext.class).getD());
        Assert.assertNull(engineResponse.getContextBean(SwitchTestContext.class).getF());
        Assert.assertEquals("E", engineResponse.getContextBean(SwitchTestContext.class).getE());
    }

    @Test
    public void test2() {
        TestRequest req = new TestRequest();
        req.setA("A");
        req.setB("D");
        req.setC("C");
        req.setD("D");
        req.setE("E");
        req.setF("F");
        EngineResponse engineResponse = fiboApplication.runner(2l, req, SwitchTestContext.class);
        Assert.assertTrue(engineResponse.isSuccess());
        Assert.assertEquals("[2]:[SwitchTestA]==>[3]:[SwitchTestB]==>[5]:[SwitchTestD]==>[7]:[SwitchTestE]", engineResponse.getExecuteStepStr());
        Assert.assertEquals("A", engineResponse.getContextBean(SwitchTestContext.class).getA());
        Assert.assertEquals("D", engineResponse.getContextBean(SwitchTestContext.class).getB());
        Assert.assertEquals("D", engineResponse.getContextBean(SwitchTestContext.class).getD());
        Assert.assertNull(engineResponse.getContextBean(SwitchTestContext.class).getC());
//        Assert.assertNull(engineResponse.getContextBean(IfTestContext.class).getD());
        Assert.assertNull(engineResponse.getContextBean(SwitchTestContext.class).getF());
        Assert.assertEquals("E", engineResponse.getContextBean(SwitchTestContext.class).getE());
    }

    @Test
    public void test3() {
        TestRequest req = new TestRequest();
        req.setA("A");
        req.setB("F");
        req.setC("C");
        req.setD("D");
        req.setE("E");
        req.setF("F");
        EngineResponse engineResponse = fiboApplication.runner(2l, req, SwitchTestContext.class);
        Assert.assertTrue(engineResponse.isSuccess());
        Assert.assertEquals("[2]:[SwitchTestA]==>[3]:[SwitchTestB]==>[6]:[SwitchTestF]==>[7]:[SwitchTestE]", engineResponse.getExecuteStepStr());
        Assert.assertEquals("A", engineResponse.getContextBean(SwitchTestContext.class).getA());
        Assert.assertEquals("F", engineResponse.getContextBean(SwitchTestContext.class).getB());
        Assert.assertEquals("F", engineResponse.getContextBean(SwitchTestContext.class).getF());
        Assert.assertNull(engineResponse.getContextBean(SwitchTestContext.class).getC());
        Assert.assertNull(engineResponse.getContextBean(SwitchTestContext.class).getD());
//        Assert.assertNull(engineResponse.getContextBean(IfTestContext.class).getF());
        Assert.assertEquals("E", engineResponse.getContextBean(SwitchTestContext.class).getE());
    }

}
