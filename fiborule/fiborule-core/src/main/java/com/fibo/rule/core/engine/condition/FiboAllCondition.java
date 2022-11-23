package com.fibo.rule.core.engine.condition;

import com.fibo.rule.core.engine.element.FiboRunnable;

import java.util.List;

/**
 * <p>全部并行执行</p>
 *
 * @author JPX
 * @since 2022-11-18 11:00
 */
public class FiboAllCondition extends FiboCondition {

    private List<FiboRunnable> runnableList;

    private boolean isAny = false;

    @Override
    public void runner(Integer contextIndex) throws Exception {

    }
}
