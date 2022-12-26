import com.alibaba.fastjson.JSONArray;
import com.fibo.rule.common.dto.EngineDto;
import com.fibo.rule.common.dto.EngineNodeDto;
import com.fibo.rule.core.engine.EngineBuilder;
import com.fibo.rule.core.engine.EngineManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * <p></p>
 *
 * @author JPX
 * @since 2022-11-25 9:45
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = EngineBuilderTest.class)
@EnableAutoConfiguration
public class EngineBuilderTest {

    @Test
    public void test1() {
        EngineDto engineDto = new EngineDto();
        engineDto.setId(1l);
        engineDto.setEngineName("test");
        String nodes = "[{\"id\":1,\"nodeName\":\"start\",\"nodeCode\":\"start\",\"engineId\":1,\"nodeType\":1,\"preNodes\":\"\",\"nextNodes\":\"A\",\"nodeConfig\":\"\",\"nodeClazz\":\"\"},{\"id\":2,\"nodeName\":\"A\",\"nodeCode\":\"A\",\"engineId\":1,\"nodeType\":3,\"preNodes\":\"start\",\"nextNodes\":\"B\",\"nodeConfig\":\"\",\"nodeClazz\":\"com.fibo.rule.test.node.ContextInitNode\"},{\"id\":3,\"nodeName\":\"B\",\"nodeCode\":\"B\",\"engineId\":1,\"nodeType\":4,\"preNodes\":\"A\",\"nextNodes\":\"C,D\",\"nodeConfig\":\"{\\\"beforeDate\\\":\\\"2022-10-01\\\",\\\"afterDate\\\":\\\"2022-10-07\\\"}\",\"nodeClazz\":\"com.fibo.rule.test.node.RechargeDateJudgeNode\"},{\"id\":4,\"nodeName\":\"C\",\"nodeCode\":\"C\",\"engineId\":1,\"nodeType\":3,\"preNodes\":\"B\",\"nextNodes\":\"E\",\"nodeConfig\":\"{\\\"threshold\\\":5,\\\"lineValue\\\":\\\"Y\\\"}\",\"nodeClazz\":\"com.fibo.rule.test.node.RewardMoneyNode\"},{\"id\":5,\"nodeName\":\"D\",\"nodeCode\":\"D\",\"engineId\":1,\"nodeType\":3,\"preNodes\":\"B\",\"nextNodes\":\"E\",\"nodeConfig\":\"{\\\"threshold\\\":5,\\\"lineValue\\\":\\\"N\\\"}\",\"nodeClazz\":\"com.fibo.rule.test.node.RewardMoneyNode\"},{\"id\":6,\"nodeName\":\"E\",\"nodeCode\":\"E\",\"engineId\":1,\"nodeType\":3,\"preNodes\":\"C,D\",\"nextNodes\":\"end\",\"nodeConfig\":\"{\\\"threshold\\\":5}\",\"nodeClazz\":\"com.fibo.rule.test.node.RewardMoneyNode\"},{\"id\":7,\"nodeName\":\"end\",\"nodeCode\":\"end\",\"engineId\":1,\"nodeType\":2,\"preNodes\":\"E\",\"nextNodes\":\"\",\"nodeConfig\":\"\",\"nodeClazz\":\"\"}]";
        List<EngineNodeDto> nodeDtoList = JSONArray.parseArray(nodes, EngineNodeDto.class);
        engineDto.setNodeList(nodeDtoList);
        EngineBuilder.createEngine(engineDto).build();
        System.out.println(EngineManager.getEngine(1l));
    }

    @Test
    public void test2() {
        EngineDto engineDto = new EngineDto();
        engineDto.setId(1l);
        engineDto.setEngineName("test");
        String nodes = "[{\"id\":1,\"nodeName\":\"start\",\"nodeCode\":\"start\",\"engineId\":1,\"nodeType\":1,\"preNodes\":\"\",\"nextNodes\":\"A\",\"nodeConfig\":\"\",\"nodeClazz\":\"\"},{\"id\":2,\"nodeName\":\"A\",\"nodeCode\":\"A\",\"engineId\":1,\"nodeType\":3,\"preNodes\":\"start\",\"nextNodes\":\"B\",\"nodeConfig\":\"\",\"nodeClazz\":\"com.fibo.rule.test.node.ContextInitNode\"},{\"id\":3,\"nodeName\":\"B\",\"nodeCode\":\"B\",\"engineId\":1,\"nodeType\":6,\"preNodes\":\"A\",\"nextNodes\":\"C,D,E\",\"nodeConfig\":\"\",\"nodeClazz\":\"\"},{\"id\":4,\"nodeName\":\"C\",\"nodeCode\":\"C\",\"engineId\":1,\"nodeType\":3,\"preNodes\":\"B\",\"nextNodes\":\"F\",\"nodeConfig\":\"{\\\"threshold\\\":5,\\\"lineValue\\\":\\\"Y\\\"}\",\"nodeClazz\":\"com.fibo.rule.test.node.RewardMoneyNode\"},{\"id\":5,\"nodeName\":\"D\",\"nodeCode\":\"D\",\"engineId\":1,\"nodeType\":3,\"preNodes\":\"B\",\"nextNodes\":\"F\",\"nodeConfig\":\"{\\\"threshold\\\":5,\\\"lineValue\\\":\\\"N\\\"}\",\"nodeClazz\":\"com.fibo.rule.test.node.RewardMoneyNode\"},{\"id\":6,\"nodeName\":\"F\",\"nodeCode\":\"F\",\"engineId\":1,\"nodeType\":3,\"preNodes\":\"C,D\",\"nextNodes\":\"K\",\"nodeConfig\":\"{\\\"threshold\\\":5,\\\"lineValue\\\":\\\"N\\\"}\",\"nodeClazz\":\"com.fibo.rule.test.node.RewardMoneyNode\"},{\"id\":7,\"nodeName\":\"E\",\"nodeCode\":\"E\",\"engineId\":1,\"nodeType\":3,\"preNodes\":\"B\",\"nextNodes\":\"G\",\"nodeConfig\":\"{\\\"threshold\\\":5}\",\"nodeClazz\":\"com.fibo.rule.test.node.RewardMoneyNode\"},{\"id\":8,\"nodeName\":\"G\",\"nodeCode\":\"G\",\"engineId\":1,\"nodeType\":4,\"preNodes\":\"E\",\"nextNodes\":\"H,I\",\"nodeConfig\":\"{\\\"beforeDate\\\":\\\"2022-10-01\\\",\\\"afterDate\\\":\\\"2022-10-07\\\"}\",\"nodeClazz\":\"com.fibo.rule.test.node.RechargeDateJudgeNode\"},{\"id\":9,\"nodeName\":\"H\",\"nodeCode\":\"H\",\"engineId\":1,\"nodeType\":3,\"preNodes\":\"G\",\"nextNodes\":\"J\",\"nodeConfig\":\"{\\\"threshold\\\":5,\\\"lineValue\\\":\\\"Y\\\"}\",\"nodeClazz\":\"com.fibo.rule.test.node.RewardMoneyNode\"},{\"id\":10,\"nodeName\":\"I\",\"nodeCode\":\"I\",\"engineId\":1,\"nodeType\":3,\"preNodes\":\"G\",\"nextNodes\":\"J\",\"nodeConfig\":\"{\\\"threshold\\\":5,\\\"lineValue\\\":\\\"N\\\"}\",\"nodeClazz\":\"com.fibo.rule.test.node.RewardMoneyNode\"},{\"id\":11,\"nodeName\":\"J\",\"nodeCode\":\"J\",\"engineId\":1,\"nodeType\":3,\"preNodes\":\"H,I\",\"nextNodes\":\"K\",\"nodeConfig\":\"{\\\"threshold\\\":5,\\\"lineValue\\\":\\\"N\\\"}\",\"nodeClazz\":\"com.fibo.rule.test.node.RewardMoneyNode\"},{\"id\":12,\"nodeName\":\"K\",\"nodeCode\":\"K\",\"engineId\":1,\"nodeType\":7,\"preNodes\":\"F,J\",\"nextNodes\":\"L\",\"nodeConfig\":\"\",\"nodeClazz\":\"\"},{\"id\":13,\"nodeName\":\"L\",\"nodeCode\":\"L\",\"engineId\":1,\"nodeType\":3,\"preNodes\":\"K\",\"nextNodes\":\"end\",\"nodeConfig\":\"{\\\"threshold\\\":5,\\\"lineValue\\\":\\\"N\\\"}\",\"nodeClazz\":\"com.fibo.rule.test.node.RewardMoneyNode\"},{\"id\":14,\"nodeName\":\"end\",\"nodeCode\":\"end\",\"engineId\":1,\"nodeType\":2,\"preNodes\":\"L\",\"nextNodes\":\"\",\"nodeConfig\":\"\",\"nodeClazz\":\"\"}]";
        List<EngineNodeDto> nodeDtoList = JSONArray.parseArray(nodes, EngineNodeDto.class);
        engineDto.setNodeList(nodeDtoList);
        EngineBuilder.createEngine(engineDto).build();
        System.out.println(EngineManager.getEngine(1l));
    }

}
