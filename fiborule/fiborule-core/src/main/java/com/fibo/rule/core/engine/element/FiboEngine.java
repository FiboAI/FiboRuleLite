package com.fibo.rule.core.engine.element;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.fibo.rule.core.context.Contextmanager;
import com.fibo.rule.core.context.FiboContext;
import com.fibo.rule.core.engine.condition.FiboCondition;
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

    private FiboCondition fiboCondition;

    @Override
    public void runner(Integer contextIndex) {
        if(ObjectUtil.isNull(fiboCondition)) {
            throw new EngineSystemException(StrUtil.format("引擎[{}]没有可执行节点", engineId));
        }
        FiboContext context = Contextmanager.getContext(contextIndex);
        try {
            context.setEngineId(engineId);
            fiboCondition.runner(contextIndex);
        } catch (Exception e) {
            context.setException(e);
            throw e;
        }
    }
}
