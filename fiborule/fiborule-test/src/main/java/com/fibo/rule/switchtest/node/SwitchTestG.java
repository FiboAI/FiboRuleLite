package com.fibo.rule.switchtest.node;

import com.fibo.rule.core.annotation.FiboBean;
import com.fibo.rule.core.node.FiboSwitchNode;
import com.fibo.rule.request.TestRequest;
import com.fibo.rule.switchtest.context.SwitchTestContext;

import java.util.HashMap;
import java.util.Map;

/**
 * <p></p>
 *
 * @author JPX
 * @since 2022-11-30 10:45
 */
@FiboBean(name = "SwitchTestG", desc = "SwitchTestG")
public class SwitchTestG extends FiboSwitchNode {

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
        branchMap.put("H", "H_branch");
        branchMap.put("I", "D_branch");
        branchMap.put("J", "E_branch");
        return branchMap;
    }
}
