package com.fibo.rule.complex.node;

import com.fibo.rule.complex.context.ComplexContext;
import com.fibo.rule.complex.request.ComplexRequest;
import com.fibo.rule.core.node.FiboNode;

/**
 * <p></p>
 *
 * @author JPX
 * @since 2022-12-07 16:21
 */
public class ComplexI extends FiboNode {
    @Override
    public void runnerStep() {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ComplexRequest req = this.getRequestData();
        ComplexContext contextBean = this.getContextBean(ComplexContext.class);
        contextBean.setI(req.getI());
    }
}
