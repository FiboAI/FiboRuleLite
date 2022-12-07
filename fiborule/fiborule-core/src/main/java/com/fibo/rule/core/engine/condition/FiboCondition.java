package com.fibo.rule.core.engine.condition;

import com.fibo.rule.core.engine.element.FiboRunnable;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>引擎执行组件抽象类</p>
 *
 * @author JPX
 * @since 2022-11-18 10:55
 */
public abstract class FiboCondition implements FiboRunnable {

    private Long id;

    private String name;

    /**可执行节点*/
    private List<FiboRunnable> runnableList = new ArrayList<>();

    public List<FiboRunnable> getRunnableList() {
        return runnableList;
    }

    public void setRunnableList(List<FiboRunnable> runnableList) {
        this.runnableList = runnableList;
    }

    public void addRunnable(FiboRunnable runnable) {
        this.runnableList.add(runnable);
    }

    public Long getRunnableId() {
        return id;
    }

    public String getRunnableName() {
        return name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
