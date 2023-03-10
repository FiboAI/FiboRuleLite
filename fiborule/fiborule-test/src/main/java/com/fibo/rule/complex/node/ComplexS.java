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
public class ComplexS extends FiboNode {
    @Override
    public void runnerStep() {
        ComplexRequest req = this.getRequestData();
        ComplexContext contextBean = this.getContextBean(ComplexContext.class);
        contextBean.setS(req.getS());
    }
}
