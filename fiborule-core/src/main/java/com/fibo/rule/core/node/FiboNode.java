package com.fibo.rule.core.node;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.fibo.rule.core.context.Contextmanager;
import com.fibo.rule.core.context.FiboContext;
import lombok.Data;

/**
 * <p>普通节点抽象类</p>
 *
 * @author JPX
 * @since 2022-11-18 10:34
 */
@Data
public abstract class FiboNode {

    private Long nodeId;
    private String nodeName;
    private String nodeCode;

    //当前slot的index
    private final TransmittableThreadLocal<Integer> contextIndexTL = new TransmittableThreadLocal<>();

    /**
     * 执行方法
     */
    public void runner(Integer contextIndex) {
        this.runnerStep();
    }

    public abstract void runnerStep();

    public FiboContext getContext(){
        return Contextmanager.getContext(this.contextIndexTL.get());
    }

    public void setContextIndex(Integer contextIndex) {
        this.contextIndexTL.set(contextIndex);
    }

    public void removeContextIndex(){
        this.contextIndexTL.remove();
    }

    public <T> T getContextBean(Class<T> contextBeanClazz) {
        return this.getContext().getContextBean(contextBeanClazz);
    }

    public <T> T getRequestData() {
        return this.getContext().getRequestData();
    }

}
