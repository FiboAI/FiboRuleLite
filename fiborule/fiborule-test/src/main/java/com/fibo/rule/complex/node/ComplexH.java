package com.fibo.rule.complex.node;

import com.fibo.rule.complex.context.ComplexContext;
import com.fibo.rule.complex.request.ComplexRequest;
import com.fibo.rule.core.annotation.FiboField;
import com.fibo.rule.core.node.FiboIfNode;
import com.fibo.rule.core.node.FiboNode;
import lombok.Data;

/**
 * <p></p>
 *
 * @author JPX
 * @since 2022-12-07 16:21
 */
@Data
public class ComplexH extends FiboIfNode {

    @FiboField(name = "valueH", desc = "valueH")
    private String valueH;

    @Override
    public boolean runnerStepIf() {
        ComplexRequest req = this.getRequestData();
        ComplexContext contextBean = this.getContextBean(ComplexContext.class);
        contextBean.setH(req.getH());
        if(valueH.equals(req.getH())) {
            return true;
        }
        return false;
    }
}
