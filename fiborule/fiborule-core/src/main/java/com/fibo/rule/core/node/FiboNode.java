package com.fibo.rule.core.node;

import cn.hutool.core.date.StopWatch;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.ttl.TransmittableThreadLocal;
import com.fibo.rule.common.enums.NodeTypeEnum;
import com.fibo.rule.core.context.Contextmanager;
import com.fibo.rule.core.context.FiboContext;
import com.fibo.rule.core.engine.step.NodeStep;
import com.fibo.rule.core.monitor.MonitorManager;
import com.fibo.rule.core.monitor.NodeStatistics;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>普通节点抽象类</p>
 *
 * @author JPX
 * @since 2022-11-18 10:34
 */
@Slf4j
@Data
public abstract class FiboNode {

//    private Long nodeId;
//    private String nodeName;
    private String beanName;
//    private String nodeCode;
    private String nodeClazz;
    private NodeTypeEnum type;

    //当前context的index
    private final TransmittableThreadLocal<Integer> contextIndexTL = new TransmittableThreadLocal<>();

    /**
     * 执行方法
     * @param nodeId
     * @param nodeName
     * @param nodeCode
     */
    public void runner(Long nodeId, String nodeName, String nodeCode) {
        FiboContext context = this.getContext();

        //新增步骤信息
        NodeStep nodeStep = new NodeStep(nodeId, nodeName, beanName, nodeCode, nodeClazz, type);
        context.addStep(nodeStep);

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        try {
            //执行逻辑
            this.runnerStep(nodeCode);
            //设置步骤为true
            nodeStep.setSuccess(true);
        } catch (Exception e) {
            nodeStep.setSuccess(false);
            nodeStep.setException(e);
            throw e;
        } finally {
            stopWatch.stop();
            final long timeSpent = stopWatch.getTotalTimeMillis();
            log.debug("[{}]:节点[{}-{}]执行用时{}毫秒", context.getRequestId(), nodeId, beanName, timeSpent);
            nodeStep.setTimeSpent(timeSpent);
            if(ObjectUtil.isNotNull(MonitorManager.loadInstance())) {
                NodeStatistics statistics = new NodeStatistics(this.getClass().getSimpleName(), timeSpent);
                MonitorManager.loadInstance().addStatistics(statistics);
            }
        }
    }

    public abstract void runnerStep(String nodeCode);

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
