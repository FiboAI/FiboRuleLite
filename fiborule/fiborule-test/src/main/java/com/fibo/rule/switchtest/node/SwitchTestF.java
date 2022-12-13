package com.fibo.rule.switchtest.node;

import com.fibo.rule.core.node.FiboNode;
import com.fibo.rule.request.TestRequest;
import com.fibo.rule.switchtest.context.SwitchTestContext;

/**
 * <p></p>
 *
 * @author JPX
 * @since 2022-11-30 10:45
 */
public class SwitchTestF extends FiboNode {
    @Override
    public void runnerStep() {
        TestRequest req = this.getRequestData();
        SwitchTestContext contextBean = this.getContextBean(SwitchTestContext.class);
        contextBean.setF(req.getF());
    }
}
