package com.fibo.rule.core.node;

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

}
