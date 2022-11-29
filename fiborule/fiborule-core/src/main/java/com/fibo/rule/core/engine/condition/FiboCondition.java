package com.fibo.rule.core.engine.condition;

import cn.hutool.core.util.ObjectUtil;
import com.fibo.rule.core.engine.element.FiboRunnable;
import lombok.Data;

/**
 * <p>引擎执行组件抽象类</p>
 *
 * @author JPX
 * @since 2022-11-18 10:55
 */
@Data
public abstract class FiboCondition implements FiboRunnable {

    /**当前节点*/
    private FiboRunnable fiboRunnable;
    /**后续节点*/
    private FiboRunnable nextRunnable;

    @Override
    public void runner(Integer contextIndex) {
        //当前节点执行
        runnerNow(contextIndex);
        //分支节点执行
        this.runnerBranch(contextIndex);
        //后续节点执行
        runnerNext(contextIndex);
    }

    public abstract void runnerBranch(Integer contextIndex);

    private void runnerNow(Integer contextIndex) {
        if(ObjectUtil.isNotNull(this.getFiboRunnable())) {
            this.getFiboRunnable().runner(contextIndex);
        }
    }

    private void runnerNext(Integer contextIndex) {
        if(ObjectUtil.isNotNull(this.getNextRunnable())) {
            this.getNextRunnable().runner(contextIndex);
        }
    }

}
