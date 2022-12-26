package com.fibo.rule.core.engine.condition;

import com.fibo.rule.core.engine.element.FiboRunnable;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>串行执行</p>
 *
 * @author JPX
 * @since 2022-11-18 11:00
 */
@Slf4j
public class FiboSerialCondition extends FiboCondition {

    @Override
    public void runner(Integer contextIndex) {
        for (FiboRunnable runnable : this.getRunnableList()) {
            runnable.runner(contextIndex);
        }
    }

}
