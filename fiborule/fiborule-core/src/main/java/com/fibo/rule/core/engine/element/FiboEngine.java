package com.fibo.rule.core.engine.element;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.fibo.rule.core.context.Contextmanager;
import com.fibo.rule.core.context.FiboContext;
import com.fibo.rule.core.engine.condition.FiboCondition;
import com.fibo.rule.core.engine.condition.FiboSerialCondition;
import com.fibo.rule.core.exception.EngineSystemException;
import lombok.Data;

/**
 * <p>引擎可执行类</p>
 *
 * @author JPX
 * @since 2022-11-18 10:47
 */
@Data
public class FiboEngine implements FiboRunnable {

    private Long engineId;

    private String engineName;

    private FiboSerialCondition serialCondition;

    @Override
    public void runner(Integer contextIndex) {
        if(ObjectUtil.isNull(serialCondition)) {
            throw new EngineSystemException(StrUtil.format("引擎[{}]没有可执行节点", engineId));
        }
        FiboContext context = Contextmanager.getContext(contextIndex);
        try {
            context.setEngineId(engineId);
            serialCondition.runner(contextIndex);
        } catch (Exception e) {
            context.setException(e);
            throw e;
        }
    }

    @Override
    public Long getRunnableId() {
        return engineId;
    }

    @Override
    public String getRunnableName() {
        return engineName;
    }
}
