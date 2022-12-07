package com.fibo.rule.alltest;

import com.alibaba.fastjson.JSONArray;
import com.fibo.rule.alltest.context.AllTestContext;
import com.fibo.rule.common.dto.EngineDto;
import com.fibo.rule.common.dto.EngineNodeDto;
import com.fibo.rule.core.engine.EngineBuilder;
import com.fibo.rule.core.engine.EngineResponse;
import com.fibo.rule.core.exception.EngineNotFoundException;
import com.fibo.rule.core.runner.FiboApplication;
import com.fibo.rule.request.TestRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.function.ThrowingRunnable;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AllTest.class)
@EnableAutoConfiguration
public class AllTest {

    @Resource
    private FiboApplication fiboApplication;

    @Before
    public void beforeTest() {
        EngineDto engineDto = new EngineDto();
        engineDto.setId(3l);
        engineDto.setEngineName("test3");
        String nodes = "[{\"id\":1,\"nodeName\":\"start\",\"nodeCode\":\"start\",\"engineId\":1,\"nodeType\":1,\"preNodes\":\"\",\"nextNodes\":\"A\",\"nodeConfig\":\"\",\"nodeClazz\":\"\"},{\"id\":2,\"nodeName\":\"A\",\"beanName\":\"AllTestA\",\"nodeCode\":\"A\",\"engineId\":1,\"nodeType\":3,\"preNodes\":\"start\",\"nextNodes\":\"ALL1\",\"nodeConfig\":\"\",\"nodeClazz\":\"com.fibo.rule.alltest.node.AllTestA\"},{\"id\":3,\"nodeName\":\"ALL1\",\"beanName\":\"\",\"nodeCode\":\"ALL1\",\"engineId\":1,\"nodeType\":6,\"preNodes\":\"A\",\"nextNodes\":\"B,C,D\",\"nodeConfig\":\"\",\"nodeClazz\":\"\"},{\"id\":4,\"nodeName\":\"B\",\"beanName\":\"AllTestB\",\"nodeCode\":\"B\",\"engineId\":1,\"nodeType\":3,\"preNodes\":\"ALL1\",\"nextNodes\":\"ALL2\",\"nodeConfig\":\"\",\"nodeClazz\":\"com.fibo.rule.alltest.node.AllTestB\"},{\"id\":5,\"nodeName\":\"C\",\"beanName\":\"AllTestC\",\"nodeCode\":\"C\",\"engineId\":1,\"nodeType\":3,\"preNodes\":\"ALL1\",\"nextNodes\":\"ALL2\",\"nodeConfig\":\"\",\"nodeClazz\":\"com.fibo.rule.alltest.node.AllTestC\"},{\"id\":6,\"nodeName\":\"D\",\"beanName\":\"AllTestD\",\"nodeCode\":\"D\",\"engineId\":1,\"nodeType\":3,\"preNodes\":\"ALL1\",\"nextNodes\":\"ALL2\",\"nodeConfig\":\"\",\"nodeClazz\":\"com.fibo.rule.alltest.node.AllTestD\"},{\"id\":7,\"nodeName\":\"ALL2\",\"beanName\":\"\",\"nodeCode\":\"ALL2\",\"engineId\":1,\"nodeType\":7,\"preNodes\":\"B,C,D\",\"nextNodes\":\"E\",\"nodeConfig\":\"\",\"nodeClazz\":\"\"},{\"id\":8,\"nodeName\":\"E\",\"beanName\":\"AllTestE\",\"nodeCode\":\"E\",\"engineId\":1,\"nodeType\":3,\"preNodes\":\"C,D\",\"nextNodes\":\"end\",\"nodeConfig\":\"\",\"nodeClazz\":\"com.fibo.rule.alltest.node.AllTestE\"},{\"id\":9,\"nodeName\":\"end\",\"nodeCode\":\"end\",\"engineId\":1,\"nodeType\":2,\"preNodes\":\"E\",\"nextNodes\":\"\",\"nodeConfig\":\"\",\"nodeClazz\":\"\"}]";
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
        req.setF("F");
        EngineResponse engineResponse = fiboApplication.runner(3l, req, AllTestContext.class);
        Assert.assertTrue(engineResponse.isSuccess());
//        Assert.assertEquals("[2]:[AllTestA]==>[4]:[AllTestB]==>[5]:[AllTestC]==>[6]:[AllTestD]==>[8]:[AllTestE]", engineResponse.getExecuteStepStr());
        Assert.assertEquals("A", engineResponse.getContextBean(AllTestContext.class).getA());
        Assert.assertEquals("B", engineResponse.getContextBean(AllTestContext.class).getB());
        Assert.assertEquals("C", engineResponse.getContextBean(AllTestContext.class).getC());
        Assert.assertEquals("D", engineResponse.getContextBean(AllTestContext.class).getD());
        Assert.assertEquals("E", engineResponse.getContextBean(AllTestContext.class).getE());
        log.info(engineResponse.getExecuteStepStr());
    }

    @Test(expected = EngineNotFoundException.class)
    public void test1() throws Exception {
        TestRequest req = new TestRequest();
        req.setA("A");
        req.setB("B");
        req.setC("C");
        req.setD("D");
        req.setE("E");
        req.setF("F");
        EngineResponse engineResponse = fiboApplication.runner(2l, req, AllTestContext.class);
        Assert.assertFalse(engineResponse.isSuccess());
        throw engineResponse.getCause();
    }

}
