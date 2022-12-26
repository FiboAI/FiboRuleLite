package com.fibo.rule.complex.node;

import com.fibo.rule.complex.context.ComplexContext;
import com.fibo.rule.complex.request.ComplexRequest;
import com.fibo.rule.core.node.FiboNode;
import com.fibo.rule.core.node.FiboSwitchNode;

import java.util.HashMap;
import java.util.Map;

/**
 * <p></p>
 *
 * @author JPX
 * @since 2022-12-07 16:21
 */
public class ComplexF extends FiboSwitchNode {

    @Override
    public String runnerStepSwitch() {
        ComplexRequest req = this.getRequestData();
        ComplexContext contextBean = this.getContextBean(ComplexContext.class);
        contextBean.setF(req.getF());
        return req.getF();
    }

    @Override
    public Map<String, String> switchBranchs() {
        Map<String, String> qmap = new HashMap<>();
        qmap.put("I", "I");
        qmap.put("J", "J");
        qmap.put("K", "K");
        return null;
    }
}
