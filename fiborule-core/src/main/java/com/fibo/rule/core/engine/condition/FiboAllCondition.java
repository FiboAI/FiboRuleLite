package com.fibo.rule.core.engine.condition;

import com.fibo.rule.core.engine.element.FiboRunnable;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>全部并行执行</p>
 *
 * @author JPX
 * @since 2022-11-18 11:00
 */
public class FiboAllCondition extends FiboCondition {

    private List<FiboRunnable> runnableList = new ArrayList<>();

    @Override
    public void runnerBranch(Integer contextIndex) {

    }

    public void addRunnable(FiboRunnable fiboRunnable) {
        runnableList.add(fiboRunnable);
    }

}
