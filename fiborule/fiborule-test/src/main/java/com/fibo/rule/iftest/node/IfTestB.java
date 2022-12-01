package com.fibo.rule.iftest.node;

import com.fibo.rule.core.annotation.FiboBean;
import com.fibo.rule.core.annotation.FiboField;
import com.fibo.rule.core.node.FiboIfNode;
import com.fibo.rule.core.node.FiboNode;
import com.fibo.rule.iftest.context.IfTestContext;
import com.fibo.rule.request.TestRequest;
import lombok.Data;

/**
 * <p></p>
 *
 * @author JPX
 * @since 2022-11-30 10:45
 */
@Data
@FiboBean(name = "IfTestB", desc = "IfTestB")
public class IfTestB extends FiboIfNode {

    @FiboField(name = "valueB", desc = "valueB")
    private String valueB;

    @Override
    public boolean runnerStepIf() {
        TestRequest req = this.getRequestData();
        IfTestContext contextBean = this.getContextBean(IfTestContext.class);
        contextBean.setB(req.getB());
        if(valueB.equals(req.getB())) {
            return true;
        }
        return false;
    }
}
