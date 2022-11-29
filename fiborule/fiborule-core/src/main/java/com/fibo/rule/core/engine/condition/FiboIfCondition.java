package com.fibo.rule.core.engine.condition;

import cn.hutool.core.util.ObjectUtil;
import com.fibo.rule.core.context.Contextmanager;
import com.fibo.rule.core.context.FiboContext;
import com.fibo.rule.core.engine.element.FiboEngineNode;
import com.fibo.rule.core.engine.element.FiboRunnable;
import lombok.Data;

/**
 * <p>IF组件执行</p>
 *
 * @author JPX
 * @since 2022-11-18 11:00
 */
@Data
public class FiboIfCondition extends FiboCondition {

    private FiboRunnable trueBranch;
    private FiboRunnable falseBranch;

    @Override
    public void runnerBranch(Integer contextIndex) {
        FiboContext context = Contextmanager.getContext(contextIndex);
        boolean result = context.getIfResult(this.getIfNode().getNodeCode());
        if(result) {
            if(ObjectUtil.isNotNull(trueBranch)) {
                trueBranch.runner(contextIndex);
            }
        } else {
            if(ObjectUtil.isNotNull(falseBranch)) {
                falseBranch.runner(contextIndex);
            }
        }
    }

    private FiboEngineNode getIfNode() {
        return (FiboEngineNode) this.getFiboRunnable();
    }
}
