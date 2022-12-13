package com.fibo.rule.alltest.node;

import com.fibo.rule.alltest.context.AllTestContext;
import com.fibo.rule.core.annotation.FiboBean;
import com.fibo.rule.core.node.FiboNode;
import com.fibo.rule.request.TestRequest;
import lombok.Data;

/**
 * <p></p>
 *
 * @author JPX
 * @since 2022-11-30 10:45
 */
@Data
@FiboBean(name = "AllTestB", desc = "AllTestB")
public class AllTestB extends FiboNode {

    @Override
    public void runnerStep(String nodeCode) {
        TestRequest req = this.getRequestData();
        AllTestContext contextBean = this.getContextBean(AllTestContext.class);
        contextBean.setB(req.getB());
    }
}
