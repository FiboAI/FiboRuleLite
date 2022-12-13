package com.fibo.rule.core.node;

/**
 * <p>IF节点抽象类</p>
 *
 * @author JPX
 * @since 2022-11-18 10:42
 */
public abstract class FiboIfNode extends FiboNode {

    @Override
    public void runnerStep(String nodeCode) {
        boolean result = this.runnerStepIf();
        //将结果放到参数池中
        this.getContext().setIfResult(nodeCode, result);
    }

    public abstract boolean runnerStepIf();

}
