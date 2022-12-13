package com.fibo.rule.iftest.node;

import com.alibaba.fastjson.JSONObject;
import com.fibo.rule.core.annotation.FiboBean;
import com.fibo.rule.core.annotation.FiboField;
import com.fibo.rule.core.node.FiboIfNode;
import com.fibo.rule.iftest.context.IfTestContext;
import com.fibo.rule.request.TestRequest;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

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
    @FiboField(name = "bigDecimal", desc = "bigDecimal")
    private BigDecimal bigDecimal;
    @FiboField(name = "integer", desc = "integer")
    private Integer integer;
    @FiboField(name = "i", desc = "i")
    private int i;
    @FiboField(name = "date", desc = "date")
    private Date date;
    @FiboField(name = "jsonObject", desc = "jsonObject")
    private JSONObject jsonObject;

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
