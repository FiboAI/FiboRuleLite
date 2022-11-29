package com.fibo.rule.core.engine.condition;

import cn.hutool.core.util.ObjectUtil;
import com.fibo.rule.core.context.Contextmanager;
import com.fibo.rule.core.context.FiboContext;
import com.fibo.rule.core.engine.element.FiboEngineNode;
import com.fibo.rule.core.engine.element.FiboRunnable;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>switch组件执行</p>
 *
 * @author JPX
 * @since 2022-11-18 11:00
 */
public class FiboSwitchCondition extends FiboCondition {

    private Map<String, FiboRunnable> fiboRunnableMap = new HashMap<>();

    @Override
    public void runnerBranch(Integer contextIndex) {
        FiboContext context = Contextmanager.getContext(contextIndex);
        String result = context.getSwitchResult(this.getSwitchNode().getNodeCode());
        FiboRunnable switchBranch = fiboRunnableMap.get(result);
        if(ObjectUtil.isNotNull(switchBranch)) {
            switchBranch.runner(contextIndex);
        }
    }

    private FiboEngineNode getSwitchNode() {
        return (FiboEngineNode) this.getFiboRunnable();
    }

    public void addSwitchFiboRunnalbe(String caseValue, FiboRunnable fiboRunnable) {
        fiboRunnableMap.put(caseValue, fiboRunnable);
    }

}
