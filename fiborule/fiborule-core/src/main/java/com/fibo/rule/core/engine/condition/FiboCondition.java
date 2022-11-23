package com.fibo.rule.core.engine.condition;

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

    private FiboRunnable fiboRunnable;

    private FiboRunnable nextRunnable;

}
