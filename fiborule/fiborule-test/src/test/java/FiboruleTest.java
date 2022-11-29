import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.fibo.rule.common.dto.EngineDto;
import com.fibo.rule.common.dto.EngineNodeDto;
import com.fibo.rule.core.engine.EngineBuilder;
import com.fibo.rule.core.engine.EngineResponse;
import com.fibo.rule.core.runner.FiboApplication;
import com.fibo.rule.test.context.RewardContext;
import com.fibo.rule.test.request.RechargeRequestVo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = FiboruleTest.class)
@EnableAutoConfiguration
public class FiboruleTest {

    @Resource
    private FiboApplication fiboApplication;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Test
    public void testRunner() throws ParseException {
        EngineDto engineDto = new EngineDto();
        engineDto.setId(1l);
        engineDto.setEngineName("test");
        String nodes = "[{\"id\":1,\"nodeName\":\"start\",\"nodeCode\":\"start\",\"engineId\":1,\"nodeType\":1,\"preNodes\":\"\",\"nextNodes\":\"line1\",\"nodeConfig\":\"\",\"nodeClazz\":\"\"},{\"id\":2,\"nodeName\":\"line1\",\"nodeCode\":\"line1\",\"engineId\":1,\"nodeType\":3,\"preNodes\":\"start\",\"nextNodes\":\"A\",\"nodeConfig\":\"\",\"nodeClazz\":\"\"},{\"id\":3,\"nodeName\":\"A\",\"nodeCode\":\"A\",\"engineId\":1,\"nodeType\":4,\"preNodes\":\"line1\",\"nextNodes\":\"line2\",\"nodeConfig\":\"\",\"nodeClazz\":\"com.fibo.rule.test.node.ContextInitNode\"},{\"id\":4,\"nodeName\":\"line2\",\"nodeCode\":\"line2\",\"engineId\":1,\"nodeType\":3,\"preNodes\":\"A\",\"nextNodes\":\"B\",\"nodeConfig\":\"\",\"nodeClazz\":\"\"},{\"id\":5,\"nodeName\":\"B\",\"nodeCode\":\"B\",\"engineId\":1,\"nodeType\":5,\"preNodes\":\"line2\",\"nextNodes\":\"line3,line4\",\"nodeConfig\":\"{\\\"beforeDate\\\":\\\"2022-10-01\\\",\\\"afterDate\\\":\\\"2022-10-07\\\"}\",\"nodeClazz\":\"com.fibo.rule.test.node.RechargeDateJudgeNode\"},{\"id\":6,\"nodeName\":\"line3\",\"nodeCode\":\"line3\",\"engineId\":1,\"nodeType\":3,\"preNodes\":\"B\",\"nextNodes\":\"C\",\"nodeConfig\":\"{\\\"lineValue\\\":\\\"Y\\\"}\",\"nodeClazz\":\"\"},{\"id\":7,\"nodeName\":\"line4\",\"nodeCode\":\"line4\",\"engineId\":1,\"nodeType\":3,\"preNodes\":\"B\",\"nextNodes\":\"end\",\"nodeConfig\":\"{\\\"lineValue\\\":\\\"N\\\"}\",\"nodeClazz\":\"\"},{\"id\":8,\"nodeName\":\"C\",\"nodeCode\":\"C\",\"engineId\":1,\"nodeType\":5,\"preNodes\":\"line3\",\"nextNodes\":\"line5,line6\",\"nodeConfig\":\"{\\\"threshold\\\":100}\",\"nodeClazz\":\"com.fibo.rule.test.node.RechargeJudgeNode\"},{\"id\":9,\"nodeName\":\"line5\",\"nodeCode\":\"line5\",\"engineId\":1,\"nodeType\":3,\"preNodes\":\"C\",\"nextNodes\":\"D\",\"nodeConfig\":\"{\\\"lineValue\\\":\\\"Y\\\"}\",\"nodeClazz\":\"\"},{\"id\":10,\"nodeName\":\"line6\",\"nodeCode\":\"line6\",\"engineId\":1,\"nodeType\":3,\"preNodes\":\"C\",\"nextNodes\":\"E\",\"nodeConfig\":\"{\\\"lineValue\\\":\\\"N\\\"}\",\"nodeClazz\":\"\"},{\"id\":11,\"nodeName\":\"D\",\"nodeCode\":\"D\",\"engineId\":1,\"nodeType\":4,\"preNodes\":\"line5\",\"nextNodes\":\"line7\",\"nodeConfig\":\"{\\\"threshold\\\":5}\",\"nodeClazz\":\"com.fibo.rule.test.node.RewardMoneyNode\"},{\"id\":12,\"nodeName\":\"line7\",\"nodeCode\":\"line7\",\"engineId\":1,\"nodeType\":3,\"preNodes\":\"D\",\"nextNodes\":\"end\",\"nodeConfig\":\"\",\"nodeClazz\":\"\"},{\"id\":13,\"nodeName\":\"E\",\"nodeCode\":\"E\",\"engineId\":1,\"nodeType\":5,\"preNodes\":\"line6\",\"nextNodes\":\"line8,line9\",\"nodeConfig\":\"{\\\"beforeDate\\\":\\\"2022-10-05\\\",\\\"afterDate\\\":\\\"2022-10-07\\\"}\",\"nodeClazz\":\"com.fibo.rule.test.node.RechargeDateJudgeNode\"},{\"id\":14,\"nodeName\":\"line8\",\"nodeCode\":\"line8\",\"engineId\":1,\"nodeType\":3,\"preNodes\":\"E\",\"nextNodes\":\"F\",\"nodeConfig\":\"{\\\"lineValue\\\":\\\"Y\\\"}\",\"nodeClazz\":\"\"},{\"id\":15,\"nodeName\":\"line9\",\"nodeCode\":\"line9\",\"engineId\":1,\"nodeType\":3,\"preNodes\":\"E\",\"nextNodes\":\"end\",\"nodeConfig\":\"{\\\"lineValue\\\":\\\"N\\\"}\",\"nodeClazz\":\"\"},{\"id\":16,\"nodeName\":\"F\",\"nodeCode\":\"F\",\"engineId\":1,\"nodeType\":5,\"preNodes\":\"line8\",\"nextNodes\":\"line10\",\"nodeConfig\":\"{\\\"threshold\\\":50}\",\"nodeClazz\":\"com.fibo.rule.test.node.RechargeJudgeNode\"},{\"id\":17,\"nodeName\":\"line10\",\"nodeCode\":\"line10\",\"engineId\":1,\"nodeType\":3,\"preNodes\":\"F\",\"nextNodes\":\"G\",\"nodeConfig\":\"{\\\"lineValue\\\":\\\"Y\\\"}\",\"nodeClazz\":\"\"},{\"id\":18,\"nodeName\":\"line11\",\"nodeCode\":\"line11\",\"engineId\":1,\"nodeType\":3,\"preNodes\":\"F\",\"nextNodes\":\"end\",\"nodeConfig\":\"{\\\"lineValue\\\":\\\"N\\\"}\",\"nodeClazz\":\"\"},{\"id\":19,\"nodeName\":\"G\",\"nodeCode\":\"G\",\"engineId\":1,\"nodeType\":4,\"preNodes\":\"line10\",\"nextNodes\":\"line12\",\"nodeConfig\":\"{\\\"threshold\\\":10}\",\"nodeClazz\":\"com.fibo.rule.test.node.RewardIntegralNode\"},{\"id\":20,\"nodeName\":\"line12\",\"nodeCode\":\"line12\",\"engineId\":1,\"nodeType\":3,\"preNodes\":\"G\",\"nextNodes\":\"end\",\"nodeConfig\":\"\",\"nodeClazz\":\"\"},{\"id\":21,\"nodeName\":\"end\",\"nodeCode\":\"end\",\"engineId\":1,\"nodeType\":2,\"preNodes\":\"line4,line7,line9,line11,line12\",\"nextNodes\":\"\",\"nodeConfig\":\"\",\"nodeClazz\":\"\"}]";
        List<EngineNodeDto> nodeDtoList = JSONArray.parseArray(nodes, EngineNodeDto.class);
        engineDto.setNodeList(nodeDtoList);
        EngineBuilder.createEngine(engineDto).build();


        RechargeRequestVo req = new RechargeRequestVo();
        req.setRechargeDate(simpleDateFormat.parse("2022-10-05"));
        req.setRechargeNum(new BigDecimal("50"));
        EngineResponse engineResponse = fiboApplication.runner(1l, req, RewardContext.class);
        System.out.println(JSON.toJSONString(engineResponse));
    }

}
