package com.fibo.rule.alltest.node;

import com.fibo.rule.alltest.context.AllTestContext;
import com.fibo.rule.core.node.FiboNode;
import com.fibo.rule.request.TestRequest;

/**
 * <p></p>
 *
 * @author JPX
 * @since 2022-11-30 10:45
 */
public class AllTestD extends FiboNode {
    @Override
    public void runnerStep() {
        TestRequest req = this.getRequestData();
        AllTestContext contextBean = this.getContextBean(AllTestContext.class);
        contextBean.setD(req.getD());
    }
}
