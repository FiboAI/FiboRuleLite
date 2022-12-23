package com.fibo.rule.complexTest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.fibo.rule.common.dto.EngineDto;
import com.fibo.rule.common.dto.EngineNodeDto;
import com.fibo.rule.complex.context.ComplexContext;
import com.fibo.rule.complex.request.ComplexRequest;
import com.fibo.rule.core.engine.EngineBuilder;
import com.fibo.rule.core.engine.EngineResponse;
import com.fibo.rule.core.runner.FiboApplication;
import com.fibo.rule.iftest.context.IfTestContext;
import com.fibo.rule.request.TestRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ComplexTest.class)
@EnableAutoConfiguration
public class ComplexTest {

    @Resource
    private FiboApplication fiboApplication;

    @Before
    public void beforeTest() {
        EngineDto engineDto = new EngineDto();
        engineDto.setId(1l);
        engineDto.setEngineName("complexTest");
        String nodes = "[\n" +
                "    {\"id\":1,\"nodeName\":\"start\",\"nodeCode\":\"start\",\"engineId\":1,\"nodeType\":1,\"preNodes\":\"\",\"nextNodes\":\"A\",\"nodeConfig\":\"\",\"nodeClazz\":\"\"},\n" +
                "    {\"id\":2,\"nodeName\":\"A\",\"beanName\":\"ComplexA\",\"nodeCode\":\"A\",\"engineId\":1,\"nodeType\":3,\"preNodes\":\"start\",\"nextNodes\":\"B\",\"nodeConfig\":\"\",\"nodeClazz\":\"com.fibo.rule.complex.node.ComplexA\"},\n" +
                "    {\"id\":3,\"nodeName\":\"B\",\"beanName\":\"\",\"nodeCode\":\"B\",\"engineId\":1,\"nodeType\":6,\"preNodes\":\"A\",\"nextNodes\":\"C,D,E\",\"nodeConfig\":\"\",\"nodeClazz\":\"\"},\n" +
                "\n" +
                "    {\"id\":4,\"nodeName\":\"C\",\"beanName\":\"ComplexC\",\"nodeCode\":\"C\",\"engineId\":1,\"nodeType\":3,\"preNodes\":\"B\",\"nextNodes\":\"F\",\"nodeConfig\":\"\",\"nodeClazz\":\"com.fibo.rule.complex.node.ComplexC\"},\n" +
                "    {\"id\":5,\"nodeName\":\"F\",\"beanName\":\"ComplexF\",\"nodeCode\":\"F\",\"engineId\":1,\"nodeType\":5,\"preNodes\":\"C\",\"nextNodes\":\"I,J,K\",\"nextNodeValue\":\"[{\\\"key\\\":\\\"I\\\",\\\"value\\\":\\\"I\\\"},{\\\"key\\\":\\\"J\\\",\\\"value\\\":\\\"J\\\"},{\\\"key\\\":\\\"K\\\",\\\"value\\\":\\\"K\\\"}]\",\"nodeConfig\":\"\",\"nodeClazz\":\"com.fibo.rule.complex.node.ComplexF\"},\n" +
                "    {\"id\":6,\"nodeName\":\"I\",\"beanName\":\"ComplexI\",\"nodeCode\":\"I\",\"engineId\":1,\"nodeType\":3,\"preNodes\":\"F\",\"nextNodes\":\"U\",\"nodeConfig\":\"\",\"nodeClazz\":\"com.fibo.rule.complex.node.ComplexI\"},\n" +
                "    {\"id\":7,\"nodeName\":\"J\",\"beanName\":\"ComplexJ\",\"nodeCode\":\"J\",\"engineId\":1,\"nodeType\":3,\"preNodes\":\"F\",\"nextNodes\":\"Q\",\"nodeConfig\":\"\",\"nodeClazz\":\"com.fibo.rule.complex.node.ComplexJ\"},\n" +
                "    {\"id\":8,\"nodeName\":\"Q\",\"beanName\":\"\",\"nodeCode\":\"Q\",\"engineId\":1,\"nodeType\":6,\"preNodes\":\"J\",\"nextNodes\":\"R,S\",\"nodeConfig\":\"\",\"nodeClazz\":\"\"},\n" +
                "    {\"id\":9,\"nodeName\":\"R\",\"beanName\":\"ComplexR\",\"nodeCode\":\"R\",\"engineId\":1,\"nodeType\":3,\"preNodes\":\"Q\",\"nextNodes\":\"Q1\",\"nodeConfig\":\"\",\"nodeClazz\":\"com.fibo.rule.complex.node.ComplexR\"},\n" +
                "    {\"id\":10,\"nodeName\":\"S\",\"beanName\":\"ComplexS\",\"nodeCode\":\"S\",\"engineId\":1,\"nodeType\":3,\"preNodes\":\"Q\",\"nextNodes\":\"Q1\",\"nodeConfig\":\"\",\"nodeClazz\":\"com.fibo.rule.complex.node.ComplexS\"},\n" +
                "    {\"id\":11,\"nodeName\":\"Q1\",\"beanName\":\"\",\"nodeCode\":\"Q1\",\"engineId\":1,\"nodeType\":7,\"preNodes\":\"R,S\",\"nextNodes\":\"U\",\"nodeConfig\":\"\",\"nodeClazz\":\"\"},\n" +
                "    {\"id\":12,\"nodeName\":\"K\",\"beanName\":\"ComplexK\",\"nodeCode\":\"K\",\"engineId\":1,\"nodeType\":3,\"preNodes\":\"F\",\"nextNodes\":\"U\",\"nodeConfig\":\"\",\"nodeClazz\":\"com.fibo.rule.complex.node.ComplexK\"},\n" +
                "    {\"id\":13,\"nodeName\":\"U\",\"beanName\":\"ComplexU\",\"nodeCode\":\"U\",\"engineId\":1,\"nodeType\":3,\"preNodes\":\"Q1\",\"nextNodes\":\"B1\",\"nodeConfig\":\"\",\"nodeClazz\":\"com.fibo.rule.complex.node.ComplexU\"},\n" +
                "\n" +
                "    {\"id\":14,\"nodeName\":\"D\",\"beanName\":\"ComplexD\",\"nodeCode\":\"D\",\"engineId\":1,\"nodeType\":3,\"preNodes\":\"B\",\"nextNodes\":\"G\",\"nodeConfig\":\"\",\"nodeClazz\":\"com.fibo.rule.complex.node.ComplexD\"},\n" +
                "    {\"id\":15,\"nodeName\":\"G\",\"beanName\":\"\",\"nodeCode\":\"G\",\"engineId\":1,\"nodeType\":6,\"preNodes\":\"D\",\"nextNodes\":\"L,M,N\",\"nodeConfig\":\"\",\"nodeClazz\":\"\"},\n" +
                "    {\"id\":16,\"nodeName\":\"L\",\"beanName\":\"ComplexL\",\"nodeCode\":\"L\",\"engineId\":1,\"nodeType\":3,\"preNodes\":\"G\",\"nextNodes\":\"G1\",\"nodeConfig\":\"\",\"nodeClazz\":\"com.fibo.rule.complex.node.ComplexL\"},\n" +
                "    {\"id\":17,\"nodeName\":\"M\",\"beanName\":\"ComplexM\",\"nodeCode\":\"M\",\"engineId\":1,\"nodeType\":3,\"preNodes\":\"G\",\"nextNodes\":\"G1\",\"nodeConfig\":\"\",\"nodeClazz\":\"com.fibo.rule.complex.node.ComplexM\"},\n" +
                "    {\"id\":18,\"nodeName\":\"N\",\"beanName\":\"ComplexN\",\"nodeCode\":\"N\",\"engineId\":1,\"nodeType\":3,\"preNodes\":\"G\",\"nextNodes\":\"G1\",\"nodeConfig\":\"\",\"nodeClazz\":\"com.fibo.rule.complex.node.ComplexN\"},\n" +
                "    {\"id\":19,\"nodeName\":\"G1\",\"beanName\":\"\",\"nodeCode\":\"G1\",\"engineId\":1,\"nodeType\":7,\"preNodes\":\"L,M,N\",\"nextNodes\":\"B1\",\"nodeConfig\":\"\",\"nodeClazz\":\"\"},\n" +
                "\n" +
                "    {\"id\":20,\"nodeName\":\"E\",\"beanName\":\"ComplexE\",\"nodeCode\":\"E\",\"engineId\":1,\"nodeType\":3,\"preNodes\":\"B\",\"nextNodes\":\"H\",\"nodeConfig\":\"\",\"nodeClazz\":\"com.fibo.rule.complex.node.ComplexE\"},\n" +
                "    {\"id\":21,\"nodeName\":\"H\",\"beanName\":\"\",\"nodeCode\":\"H\",\"engineId\":1,\"nodeType\":4,\"preNodes\":\"E\",\"nextNodes\":\"O,P\",\"nextNodeValue\":\"[{\\\"key\\\":\\\"Y\\\",\\\"value\\\":\\\"O\\\"},{\\\"key\\\":\\\"N\\\",\\\"value\\\":\\\"P\\\"}]\",\"nodeConfig\":\"{\\\"valueH\\\":\\\"H\\\"}\",\"nodeClazz\":\"com.fibo.rule.complex.node.ComplexH\"},\n" +
                "    {\"id\":22,\"nodeName\":\"O\",\"beanName\":\"ComplexO\",\"nodeCode\":\"O\",\"engineId\":1,\"nodeType\":3,\"preNodes\":\"H\",\"nextNodes\":\"T\",\"nodeConfig\":\"\",\"nodeClazz\":\"com.fibo.rule.complex.node.ComplexO\"},\n" +
                "    {\"id\":23,\"nodeName\":\"P\",\"beanName\":\"ComplexP\",\"nodeCode\":\"P\",\"engineId\":1,\"nodeType\":3,\"preNodes\":\"H\",\"nextNodes\":\"T\",\"nodeConfig\":\"\",\"nodeClazz\":\"com.fibo.rule.complex.node.ComplexP\"},\n" +
                "    {\"id\":24,\"nodeName\":\"T\",\"beanName\":\"\",\"nodeCode\":\"T\",\"engineId\":1,\"nodeType\":3,\"preNodes\":\"O,P\",\"nextNodes\":\"B1\",\"nodeConfig\":\"\",\"nodeClazz\":\"com.fibo.rule.complex.node.ComplexT\"},\n" +
                "\n" +
                "    {\"id\":25,\"nodeName\":\"B1\",\"beanName\":\"\",\"nodeCode\":\"B1\",\"engineId\":1,\"nodeType\":7,\"preNodes\":\"U,G1,T\",\"nextNodes\":\"V\",\"nodeConfig\":\"\",\"nodeClazz\":\"\"},\n" +
                "    {\"id\":26,\"nodeName\":\"V\",\"beanName\":\"ComplexV\",\"nodeCode\":\"V\",\"engineId\":1,\"nodeType\":3,\"preNodes\":\"B1\",\"nextNodes\":\"end\",\"nodeConfig\":\"\",\"nodeClazz\":\"com.fibo.rule.complex.node.ComplexV\"},\n" +
                "    {\"id\":27,\"nodeName\":\"end\",\"nodeCode\":\"end\",\"engineId\":1,\"nodeType\":2,\"preNodes\":\"E\",\"nextNodes\":\"\",\"nodeConfig\":\"\",\"nodeClazz\":\"\"}\n" +
                "  ]";
        List<EngineNodeDto> nodeDtoList = JSONArray.parseArray(nodes, EngineNodeDto.class);
        engineDto.setNodeList(nodeDtoList);
        EngineBuilder.createEngine(engineDto).build();
    }

    @Test
    public void test() {
        ComplexRequest req = new ComplexRequest();
        req.setF("I");
        EngineResponse engineResponse = fiboApplication.runner(1l, req, ComplexContext.class);
        Assert.assertTrue(engineResponse.isSuccess());
        log.info(engineResponse.getExecuteStepStr());
        ComplexContext contextBean = engineResponse.getContextBean(ComplexContext.class);
        Assert.assertEquals("A", contextBean.getA());
        Assert.assertEquals("C", contextBean.getC());
        Assert.assertEquals("D", contextBean.getD());
        Assert.assertEquals("E", contextBean.getE());
        Assert.assertEquals("I", contextBean.getF());
        Assert.assertEquals("H", contextBean.getH());
        Assert.assertEquals("I", contextBean.getI());
        Assert.assertEquals(null, contextBean.getJ());
        Assert.assertEquals(null, contextBean.getK());
        Assert.assertEquals("L", contextBean.getL());
        Assert.assertEquals("M", contextBean.getM());
        Assert.assertEquals("N", contextBean.getN());
        Assert.assertEquals("O", contextBean.getO());
        Assert.assertEquals(null, contextBean.getP());
        Assert.assertEquals(null, contextBean.getR());
        Assert.assertEquals(null, contextBean.getS());
        Assert.assertEquals("T", contextBean.getT());
        Assert.assertEquals("U", contextBean.getU());
        Assert.assertEquals("V", contextBean.getV());
    }

    @Test
    public void test1() {
        ComplexRequest req = new ComplexRequest();
        req.setF("J");
        req.setH("H1");
        EngineResponse engineResponse = fiboApplication.runner(1l, req, ComplexContext.class);
        Assert.assertTrue(engineResponse.isSuccess());
        log.info(engineResponse.getExecuteStepStr());
        ComplexContext contextBean = engineResponse.getContextBean(ComplexContext.class);
        Assert.assertEquals("A", contextBean.getA());
        Assert.assertEquals("C", contextBean.getC());
        Assert.assertEquals("D", contextBean.getD());
        Assert.assertEquals("E", contextBean.getE());
        Assert.assertEquals("J", contextBean.getF());
        Assert.assertEquals("H1", contextBean.getH());
        Assert.assertEquals(null, contextBean.getI());
        Assert.assertEquals("J", contextBean.getJ());
        Assert.assertEquals(null, contextBean.getK());
        Assert.assertEquals("L", contextBean.getL());
        Assert.assertEquals("M", contextBean.getM());
        Assert.assertEquals("N", contextBean.getN());
        Assert.assertEquals(null, contextBean.getO());
        Assert.assertEquals("P", contextBean.getP());
        Assert.assertEquals("R", contextBean.getR());
        Assert.assertEquals("S", contextBean.getS());
        Assert.assertEquals("T", contextBean.getT());
        Assert.assertEquals("U", contextBean.getU());
        Assert.assertEquals("V", contextBean.getV());
    }

    @Test
    public void test2() {
        ComplexRequest req = new ComplexRequest();
        req.setF("K");
        EngineResponse engineResponse = fiboApplication.runner(1l, req, ComplexContext.class);
        Assert.assertTrue(engineResponse.isSuccess());
        log.info(engineResponse.getExecuteStepStr());
        ComplexContext contextBean = engineResponse.getContextBean(ComplexContext.class);
        Assert.assertEquals("A", contextBean.getA());
        Assert.assertEquals("C", contextBean.getC());
        Assert.assertEquals("D", contextBean.getD());
        Assert.assertEquals("E", contextBean.getE());
        Assert.assertEquals("K", contextBean.getF());
        Assert.assertEquals("H", contextBean.getH());
        Assert.assertEquals(null, contextBean.getI());
        Assert.assertEquals(null, contextBean.getJ());
        Assert.assertEquals("K", contextBean.getK());
        Assert.assertEquals("L", contextBean.getL());
        Assert.assertEquals("M", contextBean.getM());
        Assert.assertEquals("N", contextBean.getN());
        Assert.assertEquals("O", contextBean.getO());
        Assert.assertEquals(null, contextBean.getP());
        Assert.assertEquals(null, contextBean.getR());
        Assert.assertEquals(null, contextBean.getS());
        Assert.assertEquals("T", contextBean.getT());
        Assert.assertEquals("U", contextBean.getU());
        Assert.assertEquals("V", contextBean.getV());
    }

}
