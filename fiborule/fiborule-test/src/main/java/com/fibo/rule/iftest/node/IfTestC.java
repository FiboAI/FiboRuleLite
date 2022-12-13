package com.fibo.rule.iftest.node;

import com.fibo.rule.core.annotation.FiboBean;
import com.fibo.rule.core.node.FiboNode;
import com.fibo.rule.request.TestRequest;
import com.fibo.rule.iftest.context.IfTestContext;

/**
 * <p></p>
 *
 * @author JPX
 * @since 2022-11-30 10:45
 */
@FiboBean(name = "IfTestC", desc = "IfTestC")
public class IfTestC extends FiboNode {
    @Override
    public void runnerStep(String nodeCode) {
        TestRequest req = this.getRequestData();
        IfTestContext contextBean = this.getContextBean(IfTestContext.class);
        contextBean.setC(req.getC());
    }
}
