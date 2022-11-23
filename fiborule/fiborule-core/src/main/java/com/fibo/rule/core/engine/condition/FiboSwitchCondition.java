package com.fibo.rule.core.engine.condition;

import cn.hutool.core.util.ObjectUtil;
import com.fibo.rule.core.context.Contextmanager;
import com.fibo.rule.core.context.FiboContext;
import com.fibo.rule.core.engine.element.FiboEngineNode;
import com.fibo.rule.core.engine.element.FiboRunnable;
import lombok.Data;

import java.util.Map;

/**
 * <p>switch组件执行</p>
 *
 * @author JPX
 * @since 2022-11-18 11:00
 */
@Data
public class FiboSwitchCondition extends FiboCondition {

    private Map<String, FiboRunnable> fiboRunnableMap;

    @Override
    public void runner(Integer contextIndex) throws Exception {
        this.getSwitchNode().runner(contextIndex);
        FiboContext context = Contextmanager.getContext(contextIndex);
        String result = context.getSwitchResult(this.getSwitchNode().getNodeCode());
        FiboRunnable fiboRunnable = fiboRunnableMap.get(result);
        // TODO: 2022/11/21 空判断
        fiboRunnable.runner(contextIndex);
        if(ObjectUtil.isNotNull(this.getNextRunnable())) {
            this.getNextRunnable().runner(contextIndex);
        }
    }

    public FiboEngineNode getSwitchNode() {
        return (FiboEngineNode) this.getFiboRunnable();
    }

}
