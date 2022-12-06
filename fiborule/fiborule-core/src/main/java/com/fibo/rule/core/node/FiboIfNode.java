package com.fibo.rule.core.node;

import com.fibo.rule.common.constant.EngineConstant;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>IF节点抽象类</p>
 *
 * @author JPX
 * @since 2022-11-18 10:42
 */
public abstract class FiboIfNode extends FiboNode {

    @Override
    public void runnerStep() {
        boolean result = this.runnerStepIf();
        //将结果放到参数池中
        this.getContext().setIfResult(this.getNodeCode(), result);
    }

    public abstract boolean runnerStepIf();

    public Map<String, String> ifBranchs() {
        Map<String, String> branchMap = new HashMap<>();
        branchMap.put(EngineConstant.STR_Y, EngineConstant.STR_Y_CN);
        branchMap.put(EngineConstant.STR_N, EngineConstant.STR_N_CN);
        return branchMap;
    }

}
