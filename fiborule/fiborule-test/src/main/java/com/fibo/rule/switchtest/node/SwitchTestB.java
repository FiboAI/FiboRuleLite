package com.fibo.rule.switchtest.node;

import com.fibo.rule.common.constant.EngineConstant;
import com.fibo.rule.core.annotation.FiboBean;
import com.fibo.rule.core.annotation.FiboField;
import com.fibo.rule.core.node.FiboIfNode;
import com.fibo.rule.core.node.FiboSwitchNode;
import com.fibo.rule.iftest.context.IfTestContext;
import com.fibo.rule.request.TestRequest;
import com.fibo.rule.switchtest.context.SwitchTestContext;
import lombok.Data;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p></p>
 *
 * @author JPX
 * @since 2022-11-30 10:45
 */
@FiboBean(name = "SwitchTestB", desc = "SwitchTestB")
public class SwitchTestB extends FiboSwitchNode {

    @Override
    public String runnerStepSwitch() {
        TestRequest req = this.getRequestData();
        SwitchTestContext contextBean = this.getContextBean(SwitchTestContext.class);
        contextBean.setB(req.getB());
        return req.getB();
    }

    @Override
    public Map<String, String> switchBranchs() {
        Map<String, String> branchMap = new HashMap<>();
        branchMap.put("C", "C");
        branchMap.put("D", "D");
        branchMap.put("E", "E");
        return branchMap;
    }
}
