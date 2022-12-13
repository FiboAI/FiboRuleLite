package com.fibo.rule.core.node;

/**
 * <p>IF节点抽象类</p>
 *
 * @author JPX
 * @since 2022-11-18 10:42
 */
public abstract class FiboIfNode extends FiboNode {

    /**
     * 需要用到nodeCode参数，重写带参数runnerStep方法
     * @param nodeCode
     */
    @Override
    void runnerStep(String nodeCode) {
        boolean result = this.runnerStepIf();
        //将结果放到参数池中
        this.getContext().setIfResult(nodeCode, result);
    }

    @Override
    public void runnerStep() {}

    public abstract boolean runnerStepIf();

}
