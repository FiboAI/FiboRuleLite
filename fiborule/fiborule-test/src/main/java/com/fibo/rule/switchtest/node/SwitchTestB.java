package com.fibo.rule.switchtest.node;

import com.fibo.rule.core.annotation.FiboBean;
import com.fibo.rule.core.annotation.FiboField;
import com.fibo.rule.core.node.FiboIfNode;
import com.fibo.rule.core.node.FiboSwitchNode;
import com.fibo.rule.iftest.context.IfTestContext;
import com.fibo.rule.request.TestRequest;
import com.fibo.rule.switchtest.context.SwitchTestContext;
import lombok.Data;

import java.util.Arrays;
import java.util.List;

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
    public List<String> switchBranchs() {
        return Arrays.asList("C", "D", "E");
    }
}
